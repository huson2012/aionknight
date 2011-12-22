/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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