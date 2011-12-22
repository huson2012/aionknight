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