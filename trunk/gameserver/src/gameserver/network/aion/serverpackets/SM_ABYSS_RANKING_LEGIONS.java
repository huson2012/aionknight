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
		int count = 0;
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