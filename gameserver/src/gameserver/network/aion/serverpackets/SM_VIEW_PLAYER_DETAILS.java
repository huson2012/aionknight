/**
 * This file is part of Aion-Knight [http://www.aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.List;


import gameserver.model.gameobjects.Item;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;


/**
 * @author Avol
 * modified by xj2479564
 */

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
   
	    writeD(buf, targetObjId); // unk
        writeC(buf, 11); //unk
        writeC(buf, size); // itemCount
        writeC(buf, 0);
        writeD(buf, 0);
         
        for (Item item : items) 
        {
            
            /**
             * IF ItemSlot.MAIN_HAND
             * OR ItemSlot.MAIN_OR_SUB
             * no need writeD.
             */
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