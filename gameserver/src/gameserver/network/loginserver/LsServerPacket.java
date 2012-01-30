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

package gameserver.network.loginserver;

import commons.network.packet.BaseServerPacket;
import java.nio.ByteBuffer;

/**
 * Base class for every GameServer -> Login Server Packet.
 */
public abstract class LsServerPacket extends BaseServerPacket
{
	/**
	 * constructs new server packet with specified opcode.
	 * 
	 * @param opcode
	 *           packet id
	 */
	protected LsServerPacket(int opcode)
	{
		super(opcode);
	}

	/**
	 * Write this packet data for given connection, to given buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	public final void write(LoginServerConnection con, ByteBuffer buf)
	{
		buf.putShort((short) 0);
		writeImpl(con, buf);
		buf.flip();
		buf.putShort((short) buf.limit());
		buf.position(0);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	protected abstract void writeImpl(LoginServerConnection con, ByteBuffer buf);
}