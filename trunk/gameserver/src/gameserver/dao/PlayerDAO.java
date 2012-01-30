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

import gameserver.model.Race;
import gameserver.model.account.PlayerAccountData;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import java.sql.Timestamp;
import java.util.List;

/**
 * Class that is responsible for storing/loading player data
 */

public abstract class PlayerDAO implements IDFactoryAwareDAO
{
	/**
	 * Returns true if name is used, false in other case
	 * @param name name to check
	 * @return true if name is used, false in other case
	 */
	public abstract boolean isNameUsed(String name);

	/**
	 * Stores player to db
	 * @param player
	 */
	public abstract void storePlayer(Player player);

	/**
	 * This method is used to store only newly created characters
	 * @param pcd player to save in database
	 * @return true if every things went ok.
	 */
	public abstract boolean saveNewPlayer(PlayerCommonData pcd, int accountId, String accountName);
	public abstract PlayerCommonData loadPlayerCommonData(int playerObjId);

	/**
	 * Removes player and all related data (Done by CASCADE DELETION)
	 * @param playerId player to delete
	 */
	public abstract void deletePlayer(int playerId);
	public abstract void updateDeletionTime(int objectId, Timestamp deletionDate);
	public abstract void storeCreationTime(int objectId, Timestamp creationDate);

	/**
	 * Loads creation and deletion time from database, for particular player and sets these values in given
	 * PlayerAccountDataobject.
	 * @param acData
	 */
	public abstract void setCreationDeletionTime(PlayerAccountData acData);

	/**
	 * Returns a list of objectId of players that are on the account with given accountId
	 * @param accountId
	 * @return List<Integer>
	 */
	public abstract List<Integer> getPlayerOidsOnAccount(int accountId);

	/**
	 * Stores the last online time
	 * @param objectId Object ID of player to store
	 * @param lastOnline Last online time of player to store
	 */
	public abstract void storeLastOnlineTime(final int objectId, final Timestamp lastOnline);
	
	/**
	 * Store online or offline player status
	 * @param player
	 * @param online
	 */
	public abstract void onlinePlayer(final Player player, final boolean online);
	
	/**
	 * Set all players offline status
	 * @param online
	 */
	public abstract void setPlayersOffline(final boolean online);
	
	/**
	 * get commondata by name for MailService
	 * @param name
	 * @return
	 */
	public abstract PlayerCommonData loadPlayerCommonDataByName(String name);
	
	/**
	 * Returns Player's Account ID
	 * @param name
	 * @return
	 */
	public abstract int getAccountIdByName(final String name);
	
	/**
	 * Identifier name for all PlayerDAO classes
	 * @return PlayerDAO.class.getName()
	 */
	
	public abstract String getPlayerNameByObjId(final int playerObjId);
	
	/**
	 * Get characters count for a given Race
	 * @param race
	 * @return the number of characters for race
	 */
	public abstract int getCharacterCountForRace(Race race);
	
	/**
	 * Return account characters count
	 * @param accountId
	 * @return
	 */
	public abstract int getCharacterCountOnAccount(int accountId);
	
	/**
	 * Return online characters count
	 * @param none
	 * @return
	 */
	public abstract int getOnlinePlayerCount();
	
	@Override
	public final String getClassName()
	{
		return PlayerDAO.class.getName();
	}
}