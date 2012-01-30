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

public class SM_FRIEND_NOTIFY extends AionServerPacket
{
	/**
	 * Buddy has logged in
	 * (Or become visible)
	 */
	public static final int LOGIN 		= 0;
	/**
	 * Buddy has logged out
	 * (Or become invisible)
	 */
	public static final int LOGOUT 		= 1;
	/**
	 * Buddy has deleted you
	 */
	public static final int DELETED		= 2;
	
	private final int code;
	private final String name;
	
	/**
	 * Constructs a new notify packet
	 * @param code Message code
	 * @param name Name of friend
	 */
	public SM_FRIEND_NOTIFY(int code, String name)
	{
		this.code = code;
		this.name = name;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeS(buf, name);
		writeC(buf, code);
	}
}