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
