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

import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_PET_MOVE extends AionServerPacket
{
	private int actionId;
	private ToyPet pet;
	
	public SM_PET_MOVE(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, pet.getUid());
		if (actionId != 0)
			writeC(buf, actionId);
		switch(actionId)
		{
			case 0:
				writeC(buf, 0);
				writeF(buf, pet.getX1());
				writeF(buf, pet.getY1());
				writeF(buf, pet.getZ1());
				writeC(buf, pet.getH());
			case 12:
				// move
				writeF(buf, pet.getX1());
				writeF(buf, pet.getY1());
				writeF(buf, pet.getZ1());
				writeC(buf, pet.getH());
				writeF(buf, pet.getX2());
				writeF(buf, pet.getY2());
				writeF(buf, pet.getZ2());
				break;
			default:
				break;					
		}
	}
}
