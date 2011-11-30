/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.items.ItemId;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;

import java.nio.ByteBuffer;

/**
 *

 */
public class SM_UPDATE_WAREHOUSE_ITEM extends InventoryPacket
{
	Item item;
	int warehouseType;

	public SM_UPDATE_WAREHOUSE_ITEM(Item item, int warehouseType)
	{
		this.item = item;
		this.warehouseType = warehouseType;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeGeneralInfo(buf, item);

		ItemTemplate itemTemplate = item.getItemTemplate();

		if(itemTemplate.getTemplateId() == ItemId.KINAH.value())
		{
			writeKinah(buf, item);
		}
		else if (itemTemplate.isWeapon())
		{
			writeWeaponInfo(buf, item);
			writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
		}
		else if (itemTemplate.isArmor())
		{
			writeArmorInfo(buf,item);
			writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
		}
		else
		{
			writeGeneralItemInfo(buf, item);
			writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
		}
	}

	@Override
	protected void writeGeneralInfo(ByteBuffer buf, Item item)
	{
		writeD(buf, item.getObjectId());
		writeC(buf, warehouseType);
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeH(buf, 0x24);
		writeD(buf, itemTemplate.getNameId());
		writeH(buf, 0);
	}
}
