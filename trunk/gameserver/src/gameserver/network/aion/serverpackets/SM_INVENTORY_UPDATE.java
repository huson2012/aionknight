/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.items.ItemId;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SM_INVENTORY_UPDATE extends InventoryPacket
{
	private List<Item> items;
	private int size;
	private int mode;

	public SM_INVENTORY_UPDATE(List<Item> items)
	{
		this.items = items;
		this.size = items.size();
		this.mode = 25;
	}
	
	public SM_INVENTORY_UPDATE(Item item, boolean isNew)
	{
		this.items = new ArrayList<Item>();
		this.items.add(item);
		this.size = 1;
		this.mode = isNew ? 25 : 17;
	}

	/**
	 * {@inheritDoc} dc
	 */

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{	
		writeH(buf, mode); // Drop to storage animation (bit mask). 1 - min animation; 17 - Like "Drop" to inv without message in chat; 25 - full anim with surrounding "new" border and message in chat 
		writeH(buf, size); // number of entries
		for(Item item : items)
		{
			writeGeneralInfo(buf, item);

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
				writeArmorInfo(buf, item);
				writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
				writeC(buf, 0);
			}
			else if (itemTemplate.isStigma())
			{
				writeStigmaInfo(buf,item);
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