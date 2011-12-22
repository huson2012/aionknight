/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
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

package gameserver.task;

import commons.utils.AEFastSet;
import commons.utils.concurrent.RunnableStatsManager;
import org.apache.log4j.Logger;

public abstract class AbstractFIFOPeriodicTaskManager<T> extends AbstractPeriodicTaskManager
{
	protected static final Logger log = Logger.getLogger(AbstractFIFOPeriodicTaskManager.class);
	private final AEFastSet<T> queue = new AEFastSet<T>();
	private final AEFastSet<T> activeTasks	= new AEFastSet<T>();
	public AbstractFIFOPeriodicTaskManager(int period)
	{
		super(period);
	}

	public final void add(T t)
	{
		writeLock();
		try
		{
			queue.add(t);
		}
		finally
		{
			writeUnlock();
		}
	}

	@Override
	public final void run()
	{
		writeLock();
		try
		{
			activeTasks.addAll(queue);

			queue.clear();
		}
		finally
		{
			writeUnlock();
		}

		for(T task; (task = activeTasks.removeFirst()) != null;)
		{
			final long begin = System.nanoTime();

			try
			{
				callTask(task);
			}
			catch(RuntimeException e)
			{
				log.warn("", e);
			}
			finally
			{
				RunnableStatsManager.handleStats(task.getClass(), getCalledMethodName(), System.nanoTime() - begin);
			}
		}
	}
	protected abstract void callTask(T task);
	protected abstract String getCalledMethodName();
}