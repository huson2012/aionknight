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
 * Sends Survey HTML data to the client.
 * This packet can be splitted over max 255 packets
 * The max length of the HTML may therefore be 255 * 65525 byte
 */
public class SM_QUESTIONNAIRE extends AionServerPacket
{
	private int	messageId;
	private byte chunk;
	private byte count;
	private String	html;

	public SM_QUESTIONNAIRE(int messageId, byte chunk, byte count, String html)
	{
		this.messageId = messageId;
		this.chunk = chunk;
		this.count = count;
		this.html = html;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, messageId);
		writeC(buf, chunk);
		writeC(buf, count);
		writeH(buf, html.length() * 2);
		writeS(buf, html);
	}
}