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

public class SM_WAREHOUSE_INFO extends InventoryPacket 
{
    private int warehouseType;
    private List<Item> itemList;
    private boolean firstPacket;
    private int expandLvl;

    public SM_WAREHOUSE_INFO(List<Item> items, int warehouseType, int expandLvl, boolean firstPacket) 
	{
        this.warehouseType = warehouseType;
        this.expandLvl = expandLvl;
        this.firstPacket = firstPacket;
        if (items == null)
            this.itemList = Collections.emptyList();
        else
            this.itemList = items;
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        writeC(buf, warehouseType);
        writeC(buf, firstPacket ? 1 : 0);
        writeC(buf, expandLvl); //warehouse expand (0 - 9)
        writeH(buf, 0);
        writeH(buf, itemList.size());
        for (Item item : itemList) {
            writeGeneralInfo(buf, item);

            ItemTemplate itemTemplate = item.getItemTemplate();

            if (itemTemplate.getTemplateId() == ItemId.KINAH.value())
                writeKinah(buf, item);

            else if (itemTemplate.isWeapon()){
                writeWeaponInfo(buf, item);
                writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
            }
            else if (itemTemplate.isArmor()){
                writeArmorInfo(buf, item);
                writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
            }
            else{
                writeGeneralItemInfo(buf, item);
                writeH(buf, item.isEquipped() ? 255 : item.getEquipmentSlot());
            }
        }
    }

    @Override
    protected void writeGeneralInfo(ByteBuffer buf, Item item) 
	{
        writeD(buf, item.getObjectId());
        ItemTemplate itemTemplate = item.getItemTemplate();
        writeD(buf, itemTemplate.getTemplateId());
        writeC(buf, 43); //some item info (4 - weapon, 7 - armor, 8 - rings, 17 - bottles)
        writeH(buf, 36);
        writeD(buf, itemTemplate.getNameId());
        writeH(buf, 0);
    }
}