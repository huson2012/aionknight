/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**
 * 
 * @author Simple
 * 
 */
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