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

import gameserver.model.Petition;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.PetitionService;
import java.nio.ByteBuffer;

public class SM_PETITION extends AionServerPacket
{
	private Petition petition;
	
    public SM_PETITION()
    {
        this.petition = null;
    }
    
    public SM_PETITION(Petition petition)
    {
    	this.petition = petition;
    }

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(petition == null)
		{
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeH(buf, 0x00);
			writeC(buf, 0x00);
		}
		else
		{
			writeC(buf, 0x01); // Action ID ?
			writeD(buf, 100); // unk (total online players ?)
			writeH(buf, PetitionService.getInstance().getWaitingPlayers(con.getActivePlayer().getObjectId())); // Users waiting for Support
			writeS(buf, Integer.toString(petition.getPetitionId())); // Ticket ID
			writeH(buf, 0x00);
			writeC(buf, 50); // Total Petitions
			writeC(buf, 49); // Remaining Petitions
			writeH(buf, PetitionService.getInstance().calculateWaitTime(petition.getPlayerObjId())); // Estimated minutes before GM reply
			writeD(buf, 0x00);
		}
	}
}
