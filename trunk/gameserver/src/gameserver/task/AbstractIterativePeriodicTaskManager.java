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
