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
import gameserver.services.QuestService;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_NEARBY_QUESTS extends AionServerPacket 
{
    private Integer[] questIds;
    private int size;

    public SM_NEARBY_QUESTS(List<Integer> questIds) 
	{
        this.questIds = questIds.toArray(new Integer[questIds.size()]);
        this.size = questIds.size();
    }


    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        if (questIds == null || con.getActivePlayer() == null)
            return;
        int playerLevel = con.getActivePlayer().getLevel();
		writeC(buf, 0x00); // 2.1
		writeH(buf, (-1*size) & 0xFFFF); // 2.1
        for (int id : questIds) {
            writeH(buf, id);
            if (QuestService.checkLevelRequirement(id, playerLevel))
                writeH(buf, 0);
            else
                writeH(buf, 2);
        }
    }
}