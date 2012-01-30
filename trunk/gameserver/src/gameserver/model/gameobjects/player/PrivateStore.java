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

import gameserver.model.trade.TradePSItem;
import java.util.LinkedHashMap;

public class PrivateStore
{
	private Player								owner;
	private LinkedHashMap<Integer, TradePSItem>	items;
	private String								storeMessage;

	/**
	 * This method binds a player to the store and creates a list of items
	 * 
	 * @param owner
	 */
	public PrivateStore(Player owner)
	{
		this.owner = owner;
		this.items = new LinkedHashMap<Integer, TradePSItem>();
	}

	/**
	 * This method will return the owner of the store
	 * 
	 * @return Player
	 */
	public Player getOwner()
	{
		return owner;
	}

	/**
	 * This method will return the items being sold
	 * 
	 * @return LinkedHashMap<Integer, TradePSItem>
	 */
	public LinkedHashMap<Integer, TradePSItem> getSoldItems()
	{
		return items;
	}

	/**
	 * This method will add an item to the list and price
	 * 
	 * @param tradeList
	 * @param price
	 */
	public void addItemToSell(int itemObjId, TradePSItem tradeItem)
	{
		items.put(itemObjId, tradeItem);
	}

	/**
	 * This method will remove an item from the list
	 * 
	 * @param item
	 */
	public void removeItem(int itemObjId)
	{
		if(items.containsKey(itemObjId))
		{
			LinkedHashMap<Integer, TradePSItem> newItems = new LinkedHashMap<Integer, TradePSItem>();
			for(int itemObjIds : items.keySet())
			{
				if(itemObjId != itemObjIds)
					newItems.put(itemObjIds, items.get(itemObjIds));
			}
			this.items = newItems;
		}
	}

	/**
	 * @param itemId
	 *           return tradeItem
	 */
	public TradePSItem getTradeItemById(int itemObjId)
	{
		if(items.containsKey(itemObjId))
			return items.get(itemObjId);
		return null;
	}

	/**
	 * @param storeMessage
	 *           the storeMessage to set
	 */
	public void setStoreMessage(String storeMessage)
	{
		this.storeMessage = storeMessage;
	}

	/**
	 * @return the storeMessage
	 */
	public String getStoreMessage()
	{
		return storeMessage;
	}
}
