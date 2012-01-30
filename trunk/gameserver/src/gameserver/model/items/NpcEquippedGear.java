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

package gameserver.model.items;

import gameserver.dataholders.loadingutils.adapters.NpcEquipmentList;
import gameserver.dataholders.loadingutils.adapters.NpcEquippedGearAdapter;
import gameserver.model.templates.item.ItemTemplate;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

@XmlJavaTypeAdapter(NpcEquippedGearAdapter.class)
public class NpcEquippedGear implements Iterable<Entry<ItemSlot,ItemTemplate>>
{
	private Map<ItemSlot, ItemTemplate>	items;
	private short						mask;
	
	private NpcEquipmentList	v;
	public NpcEquippedGear(NpcEquipmentList v)
	{
		this.v = v;
	}

	/**
	 * @return short
	 */
	public short getItemsMask()
	{
		if(items == null) init();
		return mask;
	}
	
	@Override
	public Iterator<Entry<ItemSlot, ItemTemplate>> iterator()
	{
		if(items == null) init();
		return items.entrySet().iterator();
	}

	/**
	 * Here NPC equipment mask is initialized.
	 * All NPC slot masks should be lower than 65536
	 */
	public void init()
	{
		synchronized(this)
		{
			if(items == null)
			{
				items = new TreeMap<ItemSlot, ItemTemplate>();
				for(ItemTemplate item : v.items)
				{
					List<ItemSlot> itemSlots = ItemSlot.getSlotsFor(item.getItemSlot());
					for(ItemSlot itemSlot : itemSlots)
					{
						if(items.get(itemSlot) == null)
						{
							items.put(itemSlot, item);
							mask |= itemSlot.getSlotIdMask();
							break;
						}
					}	
				}
			}
			v = null;
		}
	}
	
	/**
	 * 
	 * @param itemSlot
	 * @return
	 */
	public ItemTemplate getItem(ItemSlot itemSlot)
	{
		return items != null ? items.get(itemSlot) : null;
	}	
}
