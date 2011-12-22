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

import commons.utils.concurrent.RunnableStatsManager;
import javolution.util.FastSet;
import java.util.Set;

public abstract class AbstractIterativePeriodicTaskManager<T> extends AbstractPeriodicTaskManager
{
	private final Set<T> startList = new FastSet<T>();
	private final Set<T> stopList = new FastSet<T>();
	private final FastSet<T> activeTasks = new FastSet<T>();
	protected AbstractIterativePeriodicTaskManager(int period)
	{
		super(period);
	}

	public boolean hasTask(T task)
	{
		readLock();
		try
		{
			if(stopList.contains(task))
				return false;

			return activeTasks.contains(task) || startList.contains(task);
		}
		finally
		{
			readUnlock();
		}
	}

	public void startTask(T task)
	{
		writeLock();
		try
		{
			startList.add(task);

			stopList.remove(task);
		}
		finally
		{
			writeUnlock();
		}
	}

	public void stopTask(T task)
	{
		writeLock();
		try
		{
			stopList.add(task);

			startList.remove(task);
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
			activeTasks.addAll(startList);
			activeTasks.removeAll(stopList);

			startList.clear();
			stopList.clear();
		}
		finally
		{
			writeUnlock();
		}

		for(FastSet.Record r = activeTasks.head(), end = activeTasks.tail(); (r = r.getNext()) != end;)
		{
			final T task = activeTasks.valueOf(r);
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