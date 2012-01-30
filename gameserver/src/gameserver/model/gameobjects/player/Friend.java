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

package gameserver.model.gameobjects.player;

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.FriendList.Status;

public class Friend
{
	private final PlayerCommonData pcd;
	
	
	public Friend(PlayerCommonData pcd) 
	{
		this.pcd = pcd;
	}
	
	/**
	 * Returns the status of this player 
	 * @return Friend's status
	 */
	public Status getStatus() 
	{
		if (pcd.getPlayer() == null || !pcd.isOnline())//second check is temporary
		{
			return FriendList.Status.OFFLINE;
		}
		return pcd.getPlayer().getFriendList().getStatus();
	}
	/**
	 * Returns this friend's name
	 * @return Friend's name
	 */
	public String getName()
	{
		return pcd.getName();
	}
	
	public int getLevel() 
	{
		return pcd.getLevel();
	}
	
	public String getNote() 
	{
		return pcd.getNote();
	}
	
	public PlayerClass getPlayerClass()
	{
		return pcd.getPlayerClass();
	}
	
	public int getMapId() 
	{
		return pcd.getPosition().getMapId();
	}
	
	/**
	 * Gets the last time this player was online as a unix timestamp<br />
	 * Returns 0 if the player is online now
	 * @return Unix timestamp the player was last online
	 */
	public int getLastOnlineTime() 
	{
		if (pcd.getLastOnline() == null || isOnline())
			return 0;
		
		return (int)(pcd.getLastOnline().getTime() / 1000); // Convert to int, unix time format (ms -> seconds)
	}
	
	public int getOid() 
	{
		return pcd.getPlayerObjId();
	}
	
	public Player getPlayer()
	{
		return pcd.getPlayer();
	}
	
	public boolean isOnline()
	{
		return pcd.isOnline();
	}
}
