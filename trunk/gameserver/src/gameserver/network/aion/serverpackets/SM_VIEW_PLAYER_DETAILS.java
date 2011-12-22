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

import gameserver.model.gameobjects.Item;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_VIEW_PLAYER_DETAILS extends InventoryPacket 
{
    private List<Item> items;
    private int size;
    private int targetObjId;

    public SM_VIEW_PLAYER_DETAILS(int targetObjId, List<Item> items) 
	{
        this.items = items;
        this.size = items.size();
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        writeD(buf, targetObjId); // ����������
        writeC(buf, 11); // ����������
        writeC(buf, size); // itemCount
        writeC(buf, 0);
        writeD(buf, 0);
        
        for (Item item : items) 
		{
            if((item.getEquipmentSlot()&1) == 0)
                writeD(buf, item.getObjectId());
            
            ItemTemplate itemTemplate = item.getItemTemplate();
            writeD(buf, itemTemplate.getTemplateId());
            writeH(buf, 0x24);
            writeD(buf, itemTemplate.getNameId());
            writeH(buf, 0);
            
            if (item.getItemTemplate().isArmor())
                writeArmorInfo(buf, item);
            else if (item.getItemTemplate().isWeapon())
                writeWeaponInfo(buf, item);
            else
                writeGeneralItemInfo(buf, item);
        }
    }
}