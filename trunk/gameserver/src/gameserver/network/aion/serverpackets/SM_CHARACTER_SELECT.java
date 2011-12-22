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

import gameserver.configs.main.GSConfig;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CHARACTER_SELECT extends AionServerPacket
{
	private int type;
	private int messageType;
	private int wrongCount;

	public SM_CHARACTER_SELECT(int type)
	{
		this.type = type;
	}

	public SM_CHARACTER_SELECT(int type, int messageType, int wrongCount)
	{
		this.type = type;
		this.messageType = messageType;
		this.wrongCount = wrongCount;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, type);

		switch (type)
		{
			case 0:
				break;
			case 1:
				break;
			case 2:
				writeH(buf, messageType);
				writeC(buf, wrongCount > 0 ? 1 : 0);
				writeD(buf, wrongCount);
				writeD(buf, GSConfig.PASSKEY_WRONG_MAXCOUNT);
				break;
		}
	}
}