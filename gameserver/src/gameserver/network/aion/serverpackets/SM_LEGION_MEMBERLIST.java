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

import gameserver.model.legion.LegionMemberEx;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SM_LEGION_MEMBERLIST extends AionServerPacket
{
	private static final int			OFFLINE	= 0x00;
	private static final int			ONLINE	= 0x01;
	private ArrayList<LegionMemberEx>	legionMembers;

	/**
	 * This constructor will handle legion member info when a List of members is given
	 * 
	 * @param ArrayList
	 *           <LegionMemberEx> legionMembers
	 */
	public SM_LEGION_MEMBERLIST(ArrayList<LegionMemberEx> legionMembers)
	{
		this.legionMembers = legionMembers;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, 0x01);
		writeH(buf, (65536 - legionMembers.size()));
		for(LegionMemberEx legionMember : legionMembers)
		{
			writeD(buf, legionMember.getObjectId());
			writeS(buf, legionMember.getName());
			writeC(buf, legionMember.getPlayerClass().getClassId());
			writeD(buf, legionMember.getLevel());
			writeC(buf, legionMember.getRank().getRankId());
			writeD(buf, legionMember.getWorldId());
			writeC(buf, legionMember.isOnline() ? ONLINE : OFFLINE);
			writeS(buf, legionMember.getSelfIntro());
			writeS(buf, legionMember.getNickname());
			writeD(buf, legionMember.getLastOnline());
		}
	}
}