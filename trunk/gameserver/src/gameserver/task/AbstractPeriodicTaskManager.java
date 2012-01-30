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

import commons.taskmanager.AbstractLockManager;
import commons.utils.Rnd;
import gameserver.GameServer;
import gameserver.GameServer.StartupHook;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

public abstract class AbstractPeriodicTaskManager extends AbstractLockManager implements Runnable, StartupHook
{
	protected static final Logger log = Logger.getLogger(AbstractPeriodicTaskManager.class);
	private final int period;
	public AbstractPeriodicTaskManager(int period)
	{
		this.period = period;
		GameServer.addStartupHook(this);
		log.info(getClass().getSimpleName() + ": Ok!");
	}

	@Override
	public final void onStartup()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 1000 + Rnd.get(period),
		Rnd.get(period - 5, period + 5));
	}
	@Override
	public abstract void run();
}
