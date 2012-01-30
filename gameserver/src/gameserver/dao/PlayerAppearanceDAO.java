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
import gameserver.model.gameobjects.player.PlayerAppearance;

/**
 * Class that is responsible for loading/storing player appearance
 */
public abstract class PlayerAppearanceDAO implements DAO
{
	/**
	 * Returns unique identifier for PlayerAppearanceDAO
	 * @return unique identifier for PlayerAppearanceDAO
	 */
	@Override
	public final String getClassName()
	{
		return PlayerAppearanceDAO.class.getName();
	}

	/**
	 * Loads player apperance DAO by player ID.<br>
	 * Returns nullIf not found in database
	 * @param playerId player id
	 * @return player appearance or null
	 */
	public abstract PlayerAppearance load(int playerId);

	/**
	 * Saves player appearance in database.
	 * Actually calls {@link #store(int, gameserver.model.gameobjects.player.PlayerAppearance)}
	 * @param player whos appearance to store
	 * @return true, if sql query was successful, false overwise
	 */
	public final boolean store(Player player)
	{
		return store(player.getObjectId(), player.getPlayerAppearance());
	}

	/**
	 * Stores appearance in database
	 * 
	 * @param id player id
	 * @param playerAppearance player appearance
	 * @return true, if sql query was successful, false overwise
	 */
	public abstract boolean store(int id, PlayerAppearance playerAppearance);
}