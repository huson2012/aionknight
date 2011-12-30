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

import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_SUMMON_UPDATE extends AionServerPacket
{
	private Summon summon;
	public SM_SUMMON_UPDATE(Summon summon)
	{
		this.summon = summon;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, summon.getLevel());
		writeH(buf, summon.getMode().getId());
		writeD(buf, 0);// unk
		writeD(buf, 0);// unk
		writeD(buf, summon.getLifeStats().getCurrentHp());
		writeD(buf, summon.getGameStats().getCurrentStat(StatEnum.MAXHP));
		writeD(buf, summon.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.PHYSICAL_DEFENSE));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_RESIST));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.ACCURACY));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.CRITICAL_RESIST));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_ACCURACY));		
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.PARRY));
		writeH(buf, summon.getGameStats().getCurrentStat(StatEnum.EVASION));
		writeD(buf, summon.getGameStats().getBaseStat(StatEnum.MAXHP));
		writeD(buf, summon.getGameStats().getBaseStat(StatEnum.MAIN_HAND_PHYSICAL_ATTACK));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.PHYSICAL_DEFENSE));		
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.MAGICAL_RESIST));		
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.ACCURACY));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.CRITICAL_RESIST));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.BOOST_MAGICAL_SKILL));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.MAGICAL_ACCURACY));		
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.MAGICAL_CRITICAL));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.PARRY));
		writeH(buf, summon.getGameStats().getBaseStat(StatEnum.EVASION));		
	}
}