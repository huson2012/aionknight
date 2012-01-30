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

/**
 * This packet is response for CM_CHECK_NICKNAME. It sends client information if name can be used or not
 */
public class SM_NICKNAME_CHECK_RESPONSE extends AionServerPacket
{
	/**
	 * Value of response object
	 */
	private final int	value;

	/**
	 * Constructs new <tt>SM_NICKNAME_CHECK_RESPONSE</tt> packet
	 * 
	 * @param value
	 *           Response value
	 */
	public SM_NICKNAME_CHECK_RESPONSE(int value)
	{
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		/**
		 * Here is some msg: 0x00 = ok 0x0A = not ok and much more
		 */
		writeC(buf, value);
	}
}
