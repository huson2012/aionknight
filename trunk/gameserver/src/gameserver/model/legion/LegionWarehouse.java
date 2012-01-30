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

package gameserver.model.legion;

import commons.database.dao.DAOManager;
import gameserver.dao.InventoryDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.player.StorageType;

public class LegionWarehouse extends Storage
{
	private Legion legion;
	private int	user = 0;
	
	public LegionWarehouse(Legion legion)
	{
		super(StorageType.LEGION_WAREHOUSE);
		this.legion = legion;
		this.setLimit(legion.getWarehouseSlots());
	}

	public Legion getLegion()
	{
		return this.legion;
	}

	public void setOwnerLegion(Legion legion)
	{
		this.legion = legion;
	}

	@Override
	public Item putToBag(Item item)
	{
		Item resultItem = storage.putToNextAvailableSlot(item);
		if(resultItem != null)
		{
			resultItem.setItemLocation(storageType);
			DAOManager.getDAO(InventoryDAO.class).store(resultItem, resultItem.getOwnerId());
		}
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return resultItem;
	}
	
	public void setUser(int user)
	{
		this.user = user;
	}
	public int getUser()
	{
		return user;
	}
	public boolean isInUse()
	{
		return user != 0;
	}
}
