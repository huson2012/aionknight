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

package gameserver.utils.gametime;

import commons.database.dao.DAOManager;
import gameserver.dao.GameTimeDAO;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

/**
 * Manages ingame time
 */
public class GameTimeManager
{
	private static final Logger log = Logger.getLogger(GameTimeManager.class);
	private static GameTime			instance;
	private static GameTimeUpdater	updater;
	private static boolean			clockStarted	= false;

	static
	{
		GameTimeDAO dao = DAOManager.getDAO(GameTimeDAO.class);
		instance = new GameTime(dao.load());
	}

	/**
	 * Gets the current GameTime
	 * 
	 * @return GameTime
	 */
	public static GameTime getGameTime()
	{
		return instance;
	}

	/**
	 * Starts the counter that increases the clock every tick
	 * 
	 * @throws IllegalStateException
	 *            If called twice
	 */
	public static void startClock()
	{
		if(clockStarted)
		{
			throw new IllegalStateException("Clock is already started");
		}

		updater = new GameTimeUpdater(instance);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(updater, 0, 5000);
		
		clockStarted = true;
	}

	/**
	 * Saves the current time to the database
	 * 
	 * @return Success
	 */
	public static boolean saveTime()
	{
		log.info("Game time saved...");
		return DAOManager.getDAO(GameTimeDAO.class).store(instance.getTime());
	}
}
