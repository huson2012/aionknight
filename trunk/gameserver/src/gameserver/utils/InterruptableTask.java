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

package gameserver.utils;

import commons.utils.concurrent.ExecuteWrapper;
import java.util.concurrent.FutureTask;

public class InterruptableTask extends ExecuteWrapper
{

	FutureTask<?> task;
	
	public InterruptableTask(FutureTask<?> task, long noWarnMilliseconds)
	{
		super(task);
		this.task = task;
		this.noWarnMilliseconds = noWarnMilliseconds;
	}
	
	protected long noWarnMilliseconds = 0;
	
	@Override
	protected long getMaximumRuntimeInMillisecWithoutWarning()
	{
		return noWarnMilliseconds;
	}
	
	public boolean cancel()
	{
		return task.cancel(true);
	}	
}
