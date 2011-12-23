/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
 *
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������)
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package commons.utils.concurrent;

import java.util.concurrent.TimeUnit;
import javolution.text.TextBuilder;
import org.apache.log4j.Logger;

public class ExecuteWrapper implements Runnable
{
	private static final Logger	log	= Logger.getLogger(ExecuteWrapper.class);

	private final Runnable runnable;

	public ExecuteWrapper(Runnable runnable)
	{
		this.runnable = runnable;
	}

	@Override
	public final void run()
	{
		ExecuteWrapper.execute(runnable, getMaximumRuntimeInMillisecWithoutWarning());
	}

	protected long getMaximumRuntimeInMillisecWithoutWarning()
	{
		return Long.MAX_VALUE;
	}

	public static void execute(Runnable runnable)
	{
		execute(runnable, Long.MAX_VALUE);
	}

	public static void execute(Runnable runnable, long maximumRuntimeInMillisecWithoutWarning)
	{
		long begin = System.nanoTime();

		try
		{
			runnable.run();
		}
		catch(RuntimeException e)
		{
			log.warn("Exception in a Runnable execution: ", e);
		}
		finally
		{
			long runtimeInNanosec = System.nanoTime() - begin;
			Class<? extends Runnable> clazz = runnable.getClass();

			RunnableStatsManager.handleStats(clazz, runtimeInNanosec);

			long runtimeInMillisec = TimeUnit.NANOSECONDS.toMillis(runtimeInNanosec);

			if(runtimeInMillisec > maximumRuntimeInMillisecWithoutWarning)
			{
				TextBuilder tb = TextBuilder.newInstance();

				tb.append(clazz);
				tb.append(" - execution time: ");
				tb.append(runtimeInMillisec);
				tb.append("msec");

				log.warn(tb.toString());

				TextBuilder.recycle(tb);
			}
		}
	}
}