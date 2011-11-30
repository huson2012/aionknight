/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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
		log.info(getClass().getSimpleName() + ": Initialized.");
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