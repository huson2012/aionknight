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

package gameserver.dao;

import commons.database.dao.DAO;
import gameserver.model.gameobjects.player.Player;

public abstract class PlayerLifeStatsDAO implements DAO
{
	/**
	 * Returns unique identifier for PlayerLifeStatsDAO
	 * @return unique identifier for PlayerLifeStatsDAO
	 */
	@Override
	public final String getClassName()
	{
		return PlayerLifeStatsDAO.class.getName();
	}
	
	/**
	 * @param player
	 */
	public abstract void loadPlayerLifeStat(Player player);
	
	/**
	 * @param player
	 */
	public abstract void insertPlayerLifeStat(Player player);
	
	/**
	 * @param player
	 */
	public abstract void updatePlayerLifeStat(Player player);
	
	/**
	 * @param player
	 */
	public abstract void deletePlayerLifeStat(int playerId);
}