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

import gameserver.model.DuelResult;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_DUEL extends AionServerPacket
{
	private String		playerName;
	private DuelResult	result;
	private int			requesterObjId;
	private int			type;

	private SM_DUEL(int type)
	{
		this.type = type;
	}
	
	public static SM_DUEL SM_DUEL_STARTED (int requesterObjId)
	{
		SM_DUEL packet = new SM_DUEL(0x00);
		packet.setRequesterObjId(requesterObjId);
		return packet;
	}
	
	private void setRequesterObjId (int requesterObjId) {
		this.requesterObjId = requesterObjId;
	}
	
	public static SM_DUEL SM_DUEL_RESULT (DuelResult result, String playerName)
	{
		SM_DUEL packet = new SM_DUEL(0x01);
		packet.setPlayerName(playerName);
		packet.setResult(result);
		return packet;
	}
	
	private void setPlayerName (String playerName) {
		this.playerName = playerName;
	}

	private void setResult (DuelResult result) {
		this.result = result;
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, type);

		switch(type)
		{
			case 0x00:
				writeD(buf, requesterObjId);
				break;
			case 0x01:
				writeC(buf, result.getResultId()); // unknown
				writeD(buf, result.getMsgId());
				writeS(buf, playerName);
				break;
			case 0xE0:
				break;
			default:
				throw new IllegalArgumentException("invalid SM_DUEL packet type " + type);
		}
	}
}
