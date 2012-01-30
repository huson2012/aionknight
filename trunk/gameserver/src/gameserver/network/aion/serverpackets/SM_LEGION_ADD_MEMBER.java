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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LEGION_ADD_MEMBER extends AionServerPacket
{	
	private Player	player;
	private boolean isMember;
	private int msgId;
	private String text;

	public SM_LEGION_ADD_MEMBER(Player player, boolean isMember, int msgId, String text)
	{
		this.player = player;
		this.isMember = isMember;
		this.msgId = msgId;
		this.text = text;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, player.getObjectId());
		writeS(buf, player.getName());
		writeC(buf, player.getLegionMember().getRank().getRankId());
		writeC(buf, isMember ? 0x01 : 0x00);// is New Member?
		writeC(buf, player.getCommonData().getPlayerClass().getClassId());
		writeC(buf, player.getLevel());
		writeD(buf, player.getPosition().getMapId());
		writeD(buf, msgId);
		writeS(buf, text);
	}
}