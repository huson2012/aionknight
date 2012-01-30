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

package gameserver.network.aion.serverpackets;

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_FRIEND_RESPONSE extends AionServerPacket
{
	/**
	 * The friend was successfully added to your list
	 */
	public static final int TARGET_ADDED = 0x00;
	/**
	 * The target of a friend request is offline
	 */
	public static final int TARGET_OFFLINE = 0x01;
	/**
	 * The target is already a friend
	 */
	public static final int TARGET_ALREADY_FRIEND = 0x02;
	/**
	 * The target does not exist
	 */
	public static final int TARGET_NOT_FOUND = 0x03;
	/**
	 * The friend denied your request to add him
	 */
	public static final int TARGET_DENIED = 0x04;
	/**
	 * The target's friend list is full
	 */
	public static final int TARGET_LIST_FULL = 0x05;
	/**
	 * The friend was removed from your list
	 */
	public static final int TARGET_REMOVED = 0x06;
	/**
	 * The target is in your blocked list, 
	 * and cannot be added to your friends list.
	 */
	public static final int TARGET_BLOCKED = 0x08;
	/**
	 * The target is dead and cannot be befriended yet.
	 */
	public static final int TARGET_DEAD	= 0x09;
	
	private final String player;
	private final int code;
	public SM_FRIEND_RESPONSE(String playerName, int messageType) {
		player = playerName;
		code = messageType;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeS(buf, player);
		writeC(buf, code);
	}
}