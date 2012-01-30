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
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;

public abstract class FriendListDAO implements DAO
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getClassName()
	{
		return FriendListDAO.class.getName();
	}
	
	/**
	 * Loads the friend list for the given player
	 * @param player Player to get friend list of
	 * @return FriendList for player
	 */
	public abstract FriendList load(final Player player);
	
	/**
	 * Makes the given players friends
	 * <ul><li>Note: Adds for both players</li></ul>
	 * @param player Player who is adding
	 * @param friend Friend to add to the friend list
	 * @return Success
	 */
	public abstract boolean addFriends(final Player player, final Player friend);

	/**
	 * Deletes the friends from eachothers lists
	 * @param player Player whos is deleting
	 * @param friendName Name of friend to delete
	 * @return Success
	 */
	public abstract boolean delFriends(final int playerOid, final int friendOid);	
}