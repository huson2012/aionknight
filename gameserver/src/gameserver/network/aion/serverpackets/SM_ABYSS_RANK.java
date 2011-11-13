/**
 * This file is part of Aion-Knight <www.Aion-Knight.org>.
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

import java.nio.ByteBuffer;
import gameserver.model.gameobjects.player.AbyssRank;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.utils.stats.AbyssRankEnum;

public class SM_ABYSS_RANK extends AionServerPacket
{
	private AbyssRank rank;
	private int currentRankId;
	
	public SM_ABYSS_RANK(AbyssRank rank)
	{
		this.rank = rank;
		this.currentRankId = rank.getRank().getId();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeQ(buf, rank.getAp());
		writeD(buf, currentRankId);
		writeD(buf, rank.getTopRanking());

		int nextRankId = currentRankId < AbyssRankEnum.values().length ? currentRankId + 1 : currentRankId;
		writeD(buf, 100 * rank.getAp()/AbyssRankEnum.getRankById(nextRankId).getRequired()); //exp %
		writeD(buf, rank.getAllKill());
		writeD(buf, rank.getMaxRank());
		writeD(buf, rank.getDailyKill());
		writeQ(buf, rank.getDailyAP());
		writeD(buf, rank.getWeeklyKill());
		writeQ(buf, rank.getWeeklyAP());
		writeD(buf, rank.getLastKill());
		writeQ(buf, rank.getLastAP());
		writeC(buf, 0x00);
	}
}