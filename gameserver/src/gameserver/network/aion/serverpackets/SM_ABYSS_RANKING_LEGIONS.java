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

import gameserver.model.AbyssRankingResult;
import gameserver.model.Race;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.AbyssRankingService;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SM_ABYSS_RANKING_LEGIONS extends AionServerPacket
{
	private ArrayList<AbyssRankingResult> data;
	private Race race;
	
	public SM_ABYSS_RANKING_LEGIONS(ArrayList<AbyssRankingResult> data, Race race)
	{
		this.data = data;
		this.race = race;
	}

	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, race.getRaceId());
		writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));// Date
		writeD(buf, 0x01);
		writeD(buf, 0x01);
		writeH(buf, data.size());
		
		for (AbyssRankingResult rs : data)
		{
			writeD(buf, rs.getLegionRank());
			writeD(buf, rs.getLegionOldRank());
			writeD(buf, rs.getLegionId());
			writeD(buf, race.getRaceId());
			writeC(buf, rs.getLegionLevel());
			writeD(buf, rs.getLegionMembers());
			writeQ(buf, rs.getLegionCP());
			writeS(buf, rs.getLegionName());
			writeB(buf, new byte[80 - (rs.getLegionName().length() * 2)]);			
		}
		data = null;
	}
}