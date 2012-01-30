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

package gameserver.network.aion;

import commons.network.packet.BaseServerPacket;
import gameserver.network.Crypt;
import java.nio.ByteBuffer;

public abstract class AionServerPacket extends BaseServerPacket
{
	/**
	 * ByteBuffer that contains this packet data
	 */
	private ByteBuffer			buf;

	/**
	 * Constructs new server packet
	 */
	protected AionServerPacket()
	{
		super();
		setOpcode(ServerPacketsOpcodes.getOpcode(getClass()));
	}

	/**
	 * Write packet short opcodec, static and two additional bytes for opcode check
	 *
	 * @param buf
	 * @param value
	 */
	private final void writeOP(ByteBuffer buf, int value)
	{
		/** obfuscate packet id */
		byte[] op= new byte[2];
		op = toBytes((short)value);
		op[0]= Crypt.encodeOpcodec(value);

		buf.put(op[0]);
		buf.put(op[1]);

		/** put static server packet code */
		buf.put(Crypt.staticServerPacketCode);

		/** for checksum? */
		buf.put((byte) ~op[0]);
		buf.put((byte) ~op[1]);
	}
	
	private final byte[] toBytes(short s) {
		return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
	}

	public final void write(AionConnection con)
	{
		write(con, buf);
	}
	/**
	 * Write and encrypt this packet data for given connection, to given buffer.
	 *
	 * @param con
	 * @param buf
	 */
	public final void write(AionConnection con, ByteBuffer buf)
	{
		buf.putShort((short) 0);
		writeOP(buf, getOpcode());
		writeImpl(con, buf);
		buf.flip();
		buf.putShort((short) buf.limit());
		ByteBuffer b = buf.slice();
		buf.position(0);
		con.encrypt(b);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 *
	 * @param con
	 * @param buf
	 */
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{

	}

	/**
	 * @return the buf
	 */
	public ByteBuffer getBuf()
	{
		return buf;
	}

	/**
	 * @param buf the buf to set
	 */
	public void setBuf(ByteBuffer buf)
	{
		this.buf = buf;
	}
}
