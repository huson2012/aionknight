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
