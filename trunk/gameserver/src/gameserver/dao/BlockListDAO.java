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
import gameserver.model.gameobjects.player.BlockList;
import gameserver.model.gameobjects.player.Player;

/**
 * Responsible for saving and loading data on players' block lists
 */
public abstract class BlockListDAO implements DAO
{
	/**
	 * Loads the blocklist for the player given
	 * @param player
	 * @return BlockList
	 */
	public abstract BlockList load(Player player);
	
	/**
	 * Adds the given object id to the list of blocked players for the given player
	 * @param playerObjId ID of player to edit the blocklist of
	 * @param objIdToBlock ID of player to add to the blocklist
	 * @return Success
	 */
	public abstract boolean addBlockedUser(int playerObjId, int objIdToBlock, String reason);

	/**
	 * Deletes the given object id from the list of blocked players for the given player
	 * @param playerObjId ID of player to edit the blocklist of
	 * @param objIdToDelete ID of player to remove from the blocklist
	 * @return Success
	 */
	public abstract boolean delBlockedUser(int playerObjId, int objIdToDelete);
	
	/**
	 * Sets the reason for blocking a player
	 * @param playerObjId Object ID of the player whos list is being edited
	 * @param blockedObjId Object ID of the player whos reason is being edited
	 * @param reason The reason to be set
	 * @return true or false
	 */
	public abstract boolean setReason(int playerObjId, int blockedObjId, String reason);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName()
	{
		return BlockListDAO.class.getName();
	}
}