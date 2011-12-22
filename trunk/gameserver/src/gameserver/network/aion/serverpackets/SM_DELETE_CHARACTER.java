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

/**
 * � ���� ����� ������ �������� ����� �� CM_DELETE_CHARACTER.
 */
public class SM_DELETE_CHARACTER extends AionServerPacket
{
	private int playerObjId;
	private int deletionTime;

	/**
	 * �������� ������ ������ SM_DELETE_CHARACTER
	 */
	public SM_DELETE_CHARACTER(int playerObjId, int deletionTime)
	{
		this.playerObjId = playerObjId;
		this.deletionTime = deletionTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(playerObjId != 0)
		{
			writeD(buf, 0x00); // ����������
			writeD(buf, playerObjId);
			writeD(buf, deletionTime);
		}
		else
		{
			writeD(buf, 0x10); // ����������
			writeD(buf, 0x00);
			writeD(buf, 0x00);
		}
	}
}