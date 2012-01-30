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

import java.util.LinkedHashMap;

public class PurchaseLimit
{
	private LinkedHashMap<Integer, Integer> items;

	public PurchaseLimit()
	{
		items = new LinkedHashMap<Integer, Integer>();
	}

	public void addItem(int itemId, int itemCount)
	{
		if(items.containsKey(itemId))
		{
			LinkedHashMap<Integer, Integer> newItems = new LinkedHashMap<Integer, Integer>();
			for(int itemIds : items.keySet())
			{
				if(itemIds != itemId)
					newItems.put(itemIds, items.get(itemIds));
				else
					newItems.put(itemIds, items.get(itemIds) + itemCount);
			}
			this.items = newItems;

		}else{
			items.put(itemId, itemCount);
		}
	}

	public void removeItem(int itemId)
	{
		if(items.containsKey(itemId))
		{
			this.items.remove(itemId);
		}
	}

	public void reset()
	{
		items = new LinkedHashMap<Integer, Integer>();
	}

	public void setItems(LinkedHashMap<Integer, Integer> items)
	{
		this.items = items;
	}

	public int getItemLimitCount(int itemId)
	{
		if(items.containsKey(itemId))
			return items.get(itemId);

		return 0;
	}

	public LinkedHashMap<Integer, Integer> getItems()
	{
		return items;
	}
}
