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

import gameserver.model.gameobjects.player.AbyssRank;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.utils.stats.AbyssRankEnum;
import java.nio.ByteBuffer;

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