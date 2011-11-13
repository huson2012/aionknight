/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.templates.bonus;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import commons.utils.Rnd;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.ItemService;


/**
 * @author Rolandas
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MaterialBonus")
public class MaterialBonus extends SimpleCheckItemBonus
{

	static final InventoryBonusType type = InventoryBonusType.MATERIAL;
	
	/** (non-Javadoc)
	 * @see gameserver.itemengine.bonus.AbstractInventoryBonus#canApply(gameserver.model.gameobjects.player.Player, int)
	 */
	@Override
	public boolean canApply(Player player, int itemId, int questId)
	{
		// Currently only one such quest bonus exists
		if(!super.canApply(player, itemId, questId))
			return false;
		return questId == 4009;
	}
	
	/** (non-Javadoc)
	 * @see com.zettacore.gameserver.itemengine.bonus.AbstractInventoryBonus#apply(gameserver.model.gameobjects.player.Player, gameserver.model.gameobjects.Item)
	 */
	@Override
	public boolean apply(Player player, Item item)
	{
		List<Integer> itemIds =
			DataManager.ITEM_DATA.getBonusItems(type, bonusLevel, bonusLevel + 10);
		if(itemIds.size() == 0)
			return true;
		
		// TODO: check if it relates to player skills
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
