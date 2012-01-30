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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockList implements Iterable<BlockedPlayer>
{
	/**
	 * The maximum number of users a block list can contain
	 */
	public static final int						MAX_BLOCKS	= 10;
	
	//Indexes blocked players by their player ID
	private final Map<Integer, BlockedPlayer> blockedList;
	
	/**
	 * Constructs a new (empty) blocked list
	 */
	public BlockList()
	{
		this( new ConcurrentHashMap<Integer, BlockedPlayer>());
	}
	
	/**
	 * Constructs a new blocked list with the given initial items
	 * @param initialList
	 * 			A map of blocked players indexed by their object IDs
	 */
	public BlockList(Map<Integer, BlockedPlayer> initialList)
	{
		this.blockedList = new ConcurrentHashMap<Integer, BlockedPlayer>(initialList);
		
	}
	
	/**
	 * Adds a player to the blocked users list<br />
	 * <ul><li>Does not send packets or update the database</li></ul>
	 * @param playerToBlock
	 * 				The player to be blocked
	 * @param reason
	  				The reason for blocking this user
	 */
	public void add(BlockedPlayer plr)
	{
		blockedList.put(plr.getObjId(), plr);
	}
	
	/**
	 * Removes a player from the blocked users list<br />
	 * <ul><li>Does not send packets or update the database</li></ul>
	 * @param objIdOfPlayer
	 */
	public void remove(int objIdOfPlayer)
	{
		blockedList.remove(objIdOfPlayer);
	}
	
	/**
	 * Returns the blocked player with this name if they exist
	 * @param name
	 * @return CommonData of player with this name, nullIf not blocked
	 */
	public BlockedPlayer getBlockedPlayer(String name)
	{

        for (BlockedPlayer player : blockedList.values())
        {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
		return null;
	}
	
	public BlockedPlayer getBlockedPlayer(int playerObjId)
	{
		return blockedList.get(playerObjId);
	}
	
	public boolean contains(int playerObjectId)
	{
		return blockedList.containsKey(playerObjectId);
	}
	
	/**
	 * Returns the number of blocked players in this list
	 * @return blockedList.size()
	 */
	public int getSize()
	{
		return blockedList.size();
	}
	
	public boolean isFull()
	{
		return getSize() >= MAX_BLOCKS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<BlockedPlayer> iterator()
	{
		return blockedList.values().iterator();
	}
}
