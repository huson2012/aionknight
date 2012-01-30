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

package gameserver.model.trade;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Exchange
{
	private Player activeplayer;
	private Player targetPlayer;
	private boolean confirmed;
	private boolean locked;
	private long kinahCount;
	private Map<Integer, ExchangeItem> items = new HashMap<Integer, ExchangeItem>();
	private Set<Item> itemsToUpdate	= new HashSet<Item>();

	public Exchange(Player activeplayer, Player targetPlayer)
	{
		super();
		this.activeplayer = activeplayer;
		this.targetPlayer = targetPlayer;
	}

	public void confirm()
	{
		confirmed = true;
	}

	/**
	 * @return the confirmed
	 */
	public boolean isConfirmed()
	{
		return confirmed;
	}

	public void lock()
	{
		this.locked = true;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * @param exchangeItem
	 */
	public void addItem(int parentItemObjId, ExchangeItem exchangeItem)
	{
		this.items.put(parentItemObjId, exchangeItem);
	}

	/**
	 * @param countToAdd
	 */
	public void addKinah(long countToAdd)
	{
		this.kinahCount += countToAdd;
	}

	/**
	 * @return the activeplayer
	 */
	public Player getActiveplayer()
	{
		return activeplayer;
	}

	/**
	 * @return the targetPlayer
	 */
	public Player getTargetPlayer()
	{
		return targetPlayer;
	}

	/**
	 * @return the kinahCount
	 */
	public long getKinahCount()
	{
		return kinahCount;
	}

	/**
	 * @return the items
	 */
	public Map<Integer, ExchangeItem> getItems()
	{
		return items;
	}

	public boolean isExchangeListFull()
	{
		return items.size() > 18;
	}

	/**
	 * @return the itemsToUpdate
	 */
	public Set<Item> getItemsToUpdate()
	{
		return itemsToUpdate;
	}
	
	/**
	 * 
	 * @param item
	 */
	public void addItemToUpdate(Item item)
	{
		itemsToUpdate.add(item);
	}
}
