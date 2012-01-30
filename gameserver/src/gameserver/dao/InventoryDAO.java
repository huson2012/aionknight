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

package gameserver.dao;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Equipment;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.gameobjects.player.StorageType;
import java.util.List;

public abstract class InventoryDAO implements IDFactoryAwareDAO
{
	/**
	 * @param player
	 * @param StorageType
	 * @return Storage
	 */
	public abstract Storage loadStorage(Player player, StorageType storageType);

	/**
	 * @param player
	 * @return Equipment
	 */
	public abstract Equipment loadEquipment(Player player);

	/**
	 * @param playerId
	 * @return
	 */
	public abstract List<Item> loadEquipment(int playerId);

	/**
	 * @param inventory
	 */
	public abstract boolean store(Player player);

	/**
	 * @param item
	 */
	public abstract boolean store(Item item, int playerId);

	/**
	 * @param playerId
	 */
	public abstract boolean deletePlayerItems(int playerId);

	@Override
	public String getClassName()
	{
		return InventoryDAO.class.getName();
	}
}