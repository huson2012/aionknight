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
import gameserver.model.items.ItemId;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

public class SM_INVENTORY_INFO extends InventoryPacket
{
	public static final int EMPTY = 0;
	public static final int FULL = 1;
	public int CUBE = 0;

	private List<Item> items;
	private int size;

	public int packetType = FULL;

	/**
	 * @param items
	 */
	public SM_INVENTORY_INFO(List<Item> items, int cubesize)
	{
		//this should prevent client crashes but need to discover when item is null
		items.removeAll(Collections.singletonList(null));
		this.items = items;
		this.size = items.size();
		this.CUBE = cubesize;
	}

	/**
	 * @param isEmpty
	 */
	public SM_INVENTORY_INFO()
	{
		this.packetType = EMPTY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(this.packetType == EMPTY)
		{
			writeD(buf, 0);
			writeH(buf, 0);
			return;
		}

		// something wrong with cube part.
		writeC(buf, 1);// TRUE/FALSE (1/0) update cube size
		writeC(buf, CUBE);// cube size from npc (so max 5 for now)
		writeC(buf, 0);// cube size from quest (so max 2 for now)
		writeC(buf, 0);// unk?
		writeH(buf, size); // number of entries

		for(Item item : items)
		{
			writeGeneralInfo(buf, item);//16 bytes

			ItemTemplate itemTemplate = item.getItemTemplate();

			if(itemTemplate.getTemplateId() == ItemId.KINAH.value())
			{
				writeKinah(buf, item);
				writeC(buf, 0);
			}
			else if (itemTemplate.isWeapon())
			{
				writeWeaponInfo(buf, item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
			else if (itemTemplate.isArmor())
			{
				writeArmorInfo(buf,item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
			else if(itemTemplate.isStigma())
			{
				writeStigmaInfo(buf, item);
			}
			else
			{
				writeGeneralItemInfo(buf, item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
		}
	}
}
