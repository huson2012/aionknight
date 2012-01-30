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
import gameserver.model.gameobjects.stats.modifiers.StatModifier;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.ItemService;
import java.util.TreeSet;

public class FusionStone extends ItemStone
{
	
	private TreeSet<StatModifier> modifiers;
	
	public FusionStone(int itemObjId, int itemId, int slot, PersistentState persistentState)
	{
		super(itemObjId, itemId, slot, ItemStoneType.FUSIONSTONE, persistentState);
		
		ItemTemplate stoneTemplate = ItemService.getItemTemplate(itemId);
		if(stoneTemplate != null && stoneTemplate.getModifiers() != null)
		{
			this.modifiers = stoneTemplate.getModifiers();
		}	
	}
	
	/**
	 * @return modifiers
	 */
	public TreeSet<StatModifier> getModifiers()
	{
		return modifiers;
	}
	
	public StatModifier getFirstModifier()
	{
		return modifiers != null ? modifiers.first() : null;
	}	
}
