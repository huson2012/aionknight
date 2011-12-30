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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_SUMMON_USESKILL extends AionServerPacket
{
	private int summonId;
	private int skillId;
	private int skillLvl;
	private int targetId;
	
	/**
	 * 
	 * @param summonId
	 * @param skillId
	 * @param skillLvl
	 * @param targetId
	 */
	public SM_SUMMON_USESKILL(int summonId, int skillId, int skillLvl, int targetId)
	{
		this.summonId = summonId;
		this.skillId = skillId;
		this.skillLvl = skillLvl;
		this.targetId = targetId;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, summonId);
		writeH(buf, skillId);
		writeC(buf, skillLvl);
		writeD(buf, targetId);
	}
}