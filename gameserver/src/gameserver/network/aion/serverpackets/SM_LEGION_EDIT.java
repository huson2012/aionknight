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

import gameserver.model.legion.Legion;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LEGION_EDIT extends AionServerPacket
{
	private int					type;
	private Legion				legion;
	private int					unixTime;
	private String				announcement;

	public SM_LEGION_EDIT(int type)
	{
		this.type = type;
	}

	public SM_LEGION_EDIT(int type, Legion legion)
	{
		this.type = type;
		this.legion = legion;
	}

	public SM_LEGION_EDIT(int type, int unixTime)
	{
		this.type = type;
		this.unixTime = unixTime;
	}

	public SM_LEGION_EDIT(int type, int unixTime, String announcement)
	{
		this.type = type;
		this.announcement = announcement;
		this.unixTime = unixTime;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
        writeC(buf, type);
		switch(type)
		{
			/** Change Legion Level **/
			case 0x00:
				writeC(buf, legion.getLegionLevel());
				break;
			/** Change Legion Rank **/
			case 0x01:
				writeD(buf, legion.getLegionRank());
				break;
			/** Change Legion Permissions **/
			case 0x02:
	            writeC(buf, legion.getDeputyPermission1());
	            writeC(buf, legion.getDeputyPermission2());
				writeC(buf, legion.getCenturionPermission1());
				writeC(buf, legion.getCenturionPermission2());
				writeC(buf, legion.getLegionaryPermission1());
				writeC(buf, legion.getLegionaryPermission2());
                writeC(buf, legion.getVolunteerPermission1());
                writeC(buf, legion.getVolunteerPermission2());
				break;
			/** Change Legion Contributions **/
			case 0x03:
				writeD(buf, legion.getContributionPoints()); // get Contributions
				break;
			/** Change Legion Announcement **/
			case 0x05:
				writeS(buf, announcement);
				writeD(buf, unixTime);
				break;
			/** Disband Legion **/
			case 0x06:
				writeD(buf, unixTime);
				break;
			/** Recover Legion **/
			case 0x07:
				break;
			/** Refresh Legion Announcement? **/
			case 0x08:
				break;
		}
	}
}
