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
        writeD(buf, targetObjId); 
        writeC(buf, 11); 
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