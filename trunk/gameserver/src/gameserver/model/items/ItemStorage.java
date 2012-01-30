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

import gameserver.model.gameobjects.Item;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.TemporaryObjectsService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemStorage
{
	public static final int FIRST_AVAILABLE_SLOT = 65535;

	/**
	 * LinkedList storageItems
	 */
	private List<Item> storageItems;

	private int limit = 0;

	public ItemStorage(int limit)
	{
		this.limit = limit;
		storageItems = new LinkedList<Item>();
	}

	/**
	 * @return the storageItems
	 * 	Returns new reference to storageItems. Null values are removed.
	 */
	public List<Item> getStorageItems()
	{
		return Collections.unmodifiableList(storageItems);
	}

	/**
	 * @return the limit
	 */
	public int getLimit()
	{
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	/**
	 * @param itemId
	 * @return Item by itemId or null if there is no such item
	 */
	public Item getItemFromStorageByItemId(int itemId)
	{
		for(Item item : storageItems)
		{
			ItemTemplate itemTemplate = item.getItemTemplate();
			if(itemTemplate.getTemplateId() == itemId)
			{
				return item;
			}
		}

		return null;
	}
	
	/**
	 * 
	 * @param itemId
	 * @return list of items with specified itemId
	 */
	public List<Item> getItemsFromStorageByItemId(int itemId)
	{
		List<Item> itemList = new ArrayList<Item>();
		
		for(Item item : storageItems)
		{
			ItemTemplate itemTemplate = item.getItemTemplate();
			if(itemTemplate.getTemplateId() == itemId)
			{
				itemList.add(item);
			}
		}

		return itemList;
	}
	
	/**
	 * @param itemObjId
	 * @return Item
	 */
	public Item getItemFromStorageByItemObjId(int itemObjId)
	{
		for(Item item : storageItems)
		{
			if(item.getObjectId() == itemObjId)
			{
				return item;
			}
		}
		return null;
	}

	/**
	 * @param itemId
	 * @return int
	 */
	public int getSlotIdByItemId(int itemId)
	{
		for(Item item : storageItems)
		{
			ItemTemplate itemTemplate = item.getItemTemplate();
			if(itemTemplate.getTemplateId() == itemId)
			{
				return item.getEquipmentSlot();
			}
		}
		return -1;
	}
	
	/**
	 * @param objId
	 * @return int
	 */
	public int getSlotIdByObjId(int objId)
	{
		for(Item item : storageItems)
		{
			if(item.getObjectId() == objId)
			{
				return item.getEquipmentSlot();
			}
		}

		return -1;
	}

	/**
	 * If storage is null - return "-1"
	 * 
	 * @return index of available slot 
	 */
	public int getNextAvailableSlot()
	{
		return FIRST_AVAILABLE_SLOT;
	}

	/**
	 * Add item logic:
	 * - If there is already existing item - try to increase stack count
	 * - If stack is full - put to next available slot
	 * 
	 * - Return null if item was not added
	 * - Return Item as the result of successful operation
	 * 
	 *  DEPRECATED ??
	 *  
	 * @param item
	 * @return Item
	 */
	public Item addItemToStorage(Item item)
	{
		Item existingItem = getItemFromStorageByItemId(item.getItemTemplate().getTemplateId());

		if(existingItem != null && existingItem.getItemCount() < existingItem.getItemTemplate().getMaxStackCount())
		{
			int maxValue = existingItem.getItemTemplate().getMaxStackCount();
			long sum = item.getItemCount() + existingItem.getItemCount();
			existingItem.setItemCount(sum >  maxValue ? maxValue : sum);
			
			return existingItem;
		}
		return putToNextAvailableSlot(item);
	}
	
	/**
	 * Put item logic:
	 * - If there is available slot - put item there and return it back
	 * - If no slot available - return null
	 * 
	 * @param item
	 * @return Item
	 */
	public Item putToNextAvailableSlot(Item item)
	{
		if (!isFull() && storageItems.add(item))
		{
			if(item.getTempItemSettedTime() > 0)
				TemporaryObjectsService.getInstance().addObject(item);
			
			return item;
		}
		else
			return null;
	}

	/**
	 * Return true if remove operation is successful
	 * Return false if remove encountered some problems
	 * 
	 * @param item
	 * @return true or false
	 */
	public boolean removeItemFromStorage(Item item)
	{
		if(item.getTempItemSettedTime() > 0)
			TemporaryObjectsService.getInstance().removeObject(item);
		
		return storageItems.remove(item);
	}

	public boolean isFull()
	{
		return storageItems.size() >= limit;
	}

	/**
	 * @return int
	 */
	public int getNumberOfFreeSlots()
	{
		return limit - storageItems.size();
	}
	
	/**
	 * Number of items in storage
	 * 
	 * @return
	 */
	public int size()
	{
		return storageItems.size();
	}
}
