/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
				throw new IllegalStateException("State: " + state + ", expected: " + expected);
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