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

import gameserver.utils.ThreadPoolManager;

import java.util.concurrent.locks.ReentrantLock;

public abstract class FIFOExecutableQueue implements Runnable
{
	private static final byte NONE = 0;
	private static final byte QUEUED = 1;
	private static final byte RUNNING = 2;
	private final ReentrantLock lock = new ReentrantLock();
	private volatile byte state = NONE;
	protected final void execute()
	{
		lock();
		try
		{
			if (state != NONE)
				return;
			
			state = QUEUED;
		}
		finally
		{
			unlock();
		}
		
		ThreadPoolManager.getInstance().execute(this);
	}
	
	public final void lock()
	{
		lock.lock();
	}
	
	public final void unlock()
	{
		lock.unlock();
	}
	
	public final void run()
	{
		try
		{
			while (!isEmpty())
			{
				setState(QUEUED, RUNNING);
				
				try
				{
					while (!isEmpty())
						removeAndExecuteFirst();
				}
				finally
				{
					setState(RUNNING, QUEUED);
				}
			}
		}
		finally
		{
			setState(QUEUED, NONE);
		}
	}
	
	private void setState(byte expected, byte value)
	{
		lock();
		try
		{
			if (state != expected)
				throw new IllegalStateException("state: " + state + ", expected: " + expected);
		}
		finally
		{
			state = value;
			unlock();
		}
	}
	protected abstract boolean isEmpty();
	protected abstract void removeAndExecuteFirst();
}