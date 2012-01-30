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

package gameserver.model.gameobjects.player;

import gameserver.model.gameobjects.Item;
import java.util.LinkedHashMap;

public class Repurchase
{
	private LinkedHashMap<Integer, Item> items;

	public Repurchase()
	{
	    items = new LinkedHashMap<Integer, Item>();
	}

	public void addItem(int itemObjId, Item item)
	{
	    items.put(itemObjId, item);
	}

	public void removeItem(int itemObjId)
	{
		if(items.containsKey(itemObjId))
		{
			LinkedHashMap<Integer, Item> newItems = new LinkedHashMap<Integer, Item>();
			for(int itemObjIds : items.keySet())
			{
				if(itemObjId != itemObjIds)
					newItems.put(itemObjIds, items.get(itemObjIds));
			}
			this.items = newItems;
		}
	}

	public Item getItem(int itemObjId)
	{
		if(items.containsKey(itemObjId))
			return items.get(itemObjId);
		
		return null;
	}

	public LinkedHashMap<Integer, Item> getRepurchaseItems()
	{
		return items;
	}
}
