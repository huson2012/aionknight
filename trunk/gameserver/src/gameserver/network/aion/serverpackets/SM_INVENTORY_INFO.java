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