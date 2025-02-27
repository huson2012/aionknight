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

import commons.utils.Rnd;
import gameserver.dataholders.DataManager;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.model.templates.item.ItemRace;
import gnu.trove.TIntIntHashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "WrappedItem")
public class WrappedItem
{

	@XmlAttribute(name = "id")
	protected int itemId;
	
	@XmlAttribute(name = "type")
	protected InventoryBonusType rollType = InventoryBonusType.NONE;
	
	@XmlAttribute(name = "level")
	protected Integer level;
	
	@XmlAttribute(required = true, name = "min")
	protected int minCount;
	
	@XmlAttribute(required = true, name = "max")
	protected int maxCount;
	
	@XmlAttribute(name = "race")
	protected ItemRace race = ItemRace.ALL;
	
	public int getItemId() 
	{
		return itemId;
	}
	
	public InventoryBonusType getRollType()
	{
		return rollType;
	}
	
	public Integer getItemLevel() 
	{
		return level;
	}
	
	public int getMinCount() 
	{
		return minCount;
	}
	
	public int getMaxCount() 
	{
		return maxCount;
	}
	
	public ItemRace getRace() 
	{
		return race;
	}
	
	public TIntIntHashMap rollItem(int playerLevel, ItemRace race)
	{
		TIntIntHashMap itemCountMap = new TIntIntHashMap();
		if (this.race != ItemRace.ALL)
		{
			if (this.race.ordinal() != race.ordinal())
				return itemCountMap;
		}

		// do nothing if invalid range or min > max (max allowed to be 0, if min > 0)
		if (maxCount <= 0 && minCount <= 0 || minCount > maxCount && maxCount != 0)
			return itemCountMap;
		
		if (rollType != InventoryBonusType.NONE)
		{
			int startLevel = 0;
			if (level == null)
			{
				startLevel = playerLevel - (playerLevel % 10);
			}
			else
			{
				startLevel = level;
				if (rollType == InventoryBonusType.MEDICINE ||
					rollType == InventoryBonusType.FOOD)
					startLevel <<= 7;
				
			}
			
			int endLevel = startLevel;
			if (rollType != InventoryBonusType.COIN && rollType != InventoryBonusType.MEDAL)
			{
				// cap to level 80 from the player level
				if (rollType == InventoryBonusType.ENCHANT)
					endLevel = 80;
				else
					endLevel += 10;
				if (rollType == InventoryBonusType.MEDICINE ||
					rollType == InventoryBonusType.FOOD)
					endLevel <<= 7;
			} else
				endLevel++;
			
			int total = 0;
			
			List<Integer> itemIds = DataManager.ITEM_DATA.getBonusItems(rollType, startLevel, endLevel);
			if(itemIds.isEmpty())
				return itemCountMap;
			
			while (true)
			{
				int itemId = itemIds.get(Rnd.get(itemIds.size()));
				if (itemCountMap.containsKey(itemId))
				{
					int oldCount = itemCountMap.get(itemId);
					if (maxCount == minCount)
						itemCountMap.adjustValue(itemId, oldCount + maxCount);
					else
						itemCountMap.adjustValue(itemId, oldCount + 1);
				}
				else
				{
					if (maxCount == minCount)
						itemCountMap.put(itemId, maxCount);
					else
						itemCountMap.put(itemId, 1);
				}
				if (maxCount == minCount)
					total += maxCount;
				else
					total++;
				
				// break if allowed to have 0 or if it exceeds max
				if (total >= maxCount || maxCount != minCount && minCount == 0)
					break;
			}
		}
		else if (itemId > 0)
		{
			int max = maxCount;
			if (max == 0)
				max = minCount;
			int count = Rnd.get(minCount, max);
			if (count > 0)
				itemCountMap.put(itemId, count);
		}
		
		return itemCountMap;
	}	
}
