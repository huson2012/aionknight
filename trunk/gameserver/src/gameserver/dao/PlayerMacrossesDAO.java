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
import gameserver.model.gameobjects.player.MacroList;

public abstract class PlayerMacrossesDAO implements DAO
{
	/**
	 * Returns unique identifier for PlayerMacroDAO
	 * @return unique identifier for PlayerMacroDAO
	 */
	@Override
	public final String getClassName()
	{
		return PlayerMacrossesDAO.class.getName();
	}

	/**
	 * Returns a list of macrosses for player
	 * @param playerId Player object id.
	 * @return a list of macrosses for player
	 */
	public abstract MacroList restoreMacrosses(int playerId);

	/**
	 * Add a macro information into database
	 * @param playerId player object id
	 * @param macroPosition macro order # of player
	 * @param macro macro contents.
	 */
	public abstract void addMacro(int playerId, int macroPosition, String macro);

	/**
	 * Update a macro information in database
	 */	
	public abstract void updateMacro(int playerId, int macroPosition, String macro);

	/**
	 * Remove macro in database
	 * @param playerId player object id
	 * @param macroPosition order of macro in macro list
	 */
	public abstract void deleteMacro(int playerId, int macroPosition);
}