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
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.AbyssRankingService;
import gameserver.utils.PacketSendUtility;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket
{	
	private ArrayList<AbyssRankingResult> 	data;
	private ArrayList<AbyssRankingResult>	dataTemp;
	private int 							race;
	private int action = 0;
	private int section = 1;
	private Player player;
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, Race race, Player player)
	{
		this.data = data;
		dataTemp = new ArrayList<AbyssRankingResult>();
		this.race = race.getRaceId();
		this.player = player;
	}
	
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, int race, int action, int section, Player player)
	{
		this.data = data;
		dataTemp = new ArrayList<AbyssRankingResult>();
		this.race = race;
		this.action = action;
		this.section = section;
		this.player = player;
	}
	
	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		int count = 0;
		
		writeD(buf, race);
		writeD(buf, Math.round(AbyssRankingService.getInstance().getTimeOfUpdate() / 1000));
		writeD(buf, section);
		writeD(buf, action);
		
		if(data.size() > 46)
			writeH(buf, 0x2E);
		else
			writeH(buf, data.size());
			
		
		for (AbyssRankingResult rs : data)
		{
			if(count >= 46)
			{
				dataTemp.add(rs);
			}	
			else
			{
				writeD(buf, rs.getTopRanking());
				writeD(buf, rs.getPlayerRank());
				writeD(buf, rs.getOldRanking());
				writeD(buf, rs.getPlayerId());
				writeD(buf, race);
				writeD(buf, rs.getPlayerClass().getClassId());
				writeD(buf, 0);
				writeD(buf, rs.getPlayerAP());
				writeD(buf, 0);
				writeC(buf, rs.getPlayerLevel());
				writeC(buf, 0);
				writeS(buf, rs.getPlayerName());
				writeB(buf, new byte[50 - (rs.getPlayerName().length() * 2)]);

				if(rs.getLegionName() == null)
				{
				    writeS(buf, "");
				    writeB(buf, new byte[80]);
				}
				else
				{
				    writeS(buf, rs.getLegionName());
				    writeB(buf, new byte[80 - (rs.getLegionName().length() * 2)]);
				}

				count++;
			}
		}
		if (section < 64)
		{
			section *= 2 ;
			if(section == 64)
				action = 127;
			else
				action = 0;
				
			data = null;	
			PacketSendUtility.sendPacket(player, new SM_ABYSS_RANKING_PLAYERS(dataTemp, race, action, section, player));
		}
		data = null;		
	}
}