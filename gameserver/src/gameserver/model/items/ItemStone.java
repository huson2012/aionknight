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

import gameserver.model.gameobjects.PersistentState;

public class ItemStone
{
	protected int itemObjId;
	private int itemId;
	private int slot;
	private PersistentState persistentState;
	private ItemStoneType itemStoneType;
	public static enum ItemStoneType
	{
		MANASTONE,
		GODSTONE,
		FUSIONSTONE
    }

	/**
	 * 
	 * @param itemObjId
	 * @param itemId
	 * @param statEnum
	 * @param enchantValue
	 * @param persistentState
	 */
	public ItemStone(int itemObjId, int itemId, int slot, ItemStoneType itemStoneType, PersistentState persistentState)
	{
		this.itemObjId = itemObjId;
		this.itemId = itemId;
		this.slot = slot;
		this.persistentState = persistentState;
	}

	/**
	 * @return the itemObjId
	 */
	public int getItemObjId()
	{
		return itemObjId;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId()
	{
		return itemId;
	}

	/**
	 * @return the slot
	 */
	public int getSlot()
	{
		return slot;
	}
	
	/**
	 * @param slot the slot to set
	 */
	public void setSlot(int slot)
	{
		this.slot = slot;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * @return the pState
	 */
	public PersistentState getPersistentState()
	{
		return persistentState;
	}

	/**
	 * 
	 * @param persistentState
	 */
	public void setPersistentState(PersistentState persistentState)
	{
		switch(persistentState)
		{
			case DELETED:
				if(this.persistentState == PersistentState.NEW)
					this.persistentState = PersistentState.NOACTION;
				else
					this.persistentState = PersistentState.DELETED;
				break;
			case UPDATE_REQUIRED:
				if(this.persistentState == PersistentState.NEW)
					break;
			default:
				this.persistentState = persistentState;
		}
	}

	/**
	 * @return the itemStoneType
	 */
	public ItemStoneType getItemStoneType()
	{
		return itemStoneType;
	}
}
