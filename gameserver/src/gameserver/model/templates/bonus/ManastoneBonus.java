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

package gameserver.model.templates.bonus;

import commons.utils.Rnd;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.ItemService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManastoneBonus")
public class ManastoneBonus extends SimpleCheckItemBonus
{
	static final InventoryBonusType type = InventoryBonusType.MANASTONE;
	
	/** (non-Javadoc)
	 * @see gameserver.itemengine.bonus.AbstractInventoryBonus#apply(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public boolean apply(Player player, Item item)
	{
		List<Integer> itemIds =
			DataManager.ITEM_DATA.getBonusItems(type, bonusLevel, bonusLevel + 10);
		if(itemIds.isEmpty())
			return true;
		
		// TODO: check if it relates to player skills, HP or MP
		int itemId = itemIds.get(Rnd.get(itemIds.size()));
		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, 1)));
	}

	/** (non-Javadoc)
	 * @see gameserver.model.templates.bonus.AbstractInventoryBonus#getType()
	 */
	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
