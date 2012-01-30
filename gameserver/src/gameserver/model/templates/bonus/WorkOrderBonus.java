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
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.ItemService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkOrderBonus")
public class WorkOrderBonus extends SimpleCheckItemBonus
{

	static final InventoryBonusType type = InventoryBonusType.TASK;
	
	@XmlAttribute()
	protected int skillId;
	public int getSkillId()
	{
		return skillId;
	}
	
	@Override
	public boolean apply(Player player, Item item)
	{
		int skillPoints = player.getSkillList().getSkillLevel(skillId);
		
		int startLvl = ((skillId & 0xF) << 10) | Math.max(0, skillPoints / 100 * 100 - 50);
		int endLvl = ((skillId & 0xF) << 10) | skillPoints;
		
		// materials and products from -50 up to current skill level
		List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(type, startLvl, endLvl + 1);
		itemIds.addAll(itemIds); // increase chances twice;
		
		// recipes to skill level + 10, but not exceeding max limit
		startLvl = ((skillId & 0xF) << 10) | Math.max(0, skillPoints / 100 * 100 - 50);
		endLvl = skillPoints + 10;
		endLvl = ((skillId & 0xF) << 10) | Math.min(skillPoints + 100, endLvl);
		List<Integer> recipeIds = DataManager.ITEM_DATA.getBonusItems(InventoryBonusType.RECIPE, 
																	  startLvl, endLvl);
		itemIds.addAll(recipeIds);
		if(itemIds.isEmpty())
			return true;
		
		List<Integer> finalIds = new ArrayList<Integer>();
		for (Integer itemId : itemIds)
		{
			ItemTemplate template = ItemService.getItemTemplate(itemId);
			ItemRace itemRace = template.getOriginRace();
			if(String.valueOf(itemRace) != String.valueOf(player.getCommonData().getRace()) &&
				itemRace != ItemRace.ALL)
				continue;
			finalIds.add(itemId);
		}
		
		Collections.shuffle(finalIds);
		
		int itemId = finalIds.get(Rnd.get(finalIds.size()));
		int itemCount = 1;
		if(itemId >= 169400010 && itemId <= 169405025)
			itemCount = 5;
		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, itemCount)));
	}

	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
