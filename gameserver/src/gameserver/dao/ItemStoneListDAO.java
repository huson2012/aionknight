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

import commons.database.dao.DAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.FusionStone;
import gameserver.model.items.GodStone;
import gameserver.model.items.ManaStone;
import java.util.List;
import java.util.Set;

public abstract class ItemStoneListDAO implements DAO
{
	/**
	 * Loads stones of item
	 * @param item
	 */
	public abstract void load(List<Item> items);
	
	/**
	 * @param itemStones
	 */
	public abstract void storeManaStone(Set<ManaStone> itemStones);
	
	/**
	 * @param fusionStones
	 */
	public abstract void storeFusionStone(Set<FusionStone> fusionStones);

	/**
	 * Saves stones of player
	 * 
	 * @param itemStoneList
	 */
	public abstract void save(Player player);
	
	/**
	 * @param godStone
	 */
	public abstract void store(GodStone godStone);
	
	@Override
	public String getClassName()
	{
		return ItemStoneListDAO.class.getName();
	}
}