/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
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

package gameserver.model.drop;

import java.util.Set;
import gameserver.configs.main.DropConfig;
import gnu.trove.TIntObjectHashMap;

public class NpcDropStat
{
	static class ItemStat
	{
		public int		killsDone;
		public double	lootChance;
		public double	notGetChance;
		
		public ItemStat(int itemId, double chance)
		{
			this.lootChance = chance;
			this.notGetChance = 1d - chance / 100d;
		}
	}
	
	private TIntObjectHashMap<ItemStat> itemStats;
	
	public NpcDropStat(Set<DropTemplate> dropData)
	{
		itemStats = new TIntObjectHashMap<ItemStat>();
		if (dropData == null || dropData.size() == 0)
			return;
		for (DropTemplate template : dropData)
			itemStats.put(template.getItemId(), new ItemStat(template.getItemId(), 
						  template.getChance()));
	}
	
	public void updateStat(int itemId, boolean looted)
	{
		if (!itemStats.containsKey(itemId))
			return;
		ItemStat stat = itemStats.get(itemId);
		if (looted)
		{
			stat.killsDone = 0;
			stat.notGetChance = 1d - stat.lootChance / 100d;
		}
		else
		{
			stat.killsDone++;
			stat.notGetChance -= getNotGetChance(stat.killsDone, stat.lootChance);
		}
	}
	
	public double getItemLootChance(int itemId)
	{
		if (!itemStats.containsKey(itemId))
			return 0d;
		ItemStat stat = itemStats.get(itemId);
		if (stat.killsDone == 0)
			return stat.lootChance;
		else
		{
			if(DropConfig.FORMULA_TYPE == 0)
			{
				double notGetChance = stat.notGetChance - getNotGetChance(stat.killsDone + 1, stat.lootChance);
				return (1d - notGetChance) * 100d;
			}
			else
				return stat.lootChance * Math.pow(1.05, stat.killsDone);
		}
	}
	
		public final static double getNotGetChance(int kills, double initialChance) {
		double easeCoeff = 2 - Math.log10(initialChance);
		return Math.pow(kills * initialChance / 100d, Math.sqrt(easeCoeff * kills));
	}
	
	public void setItemLootChance(int itemId, double chance)
	{
		ItemStat stat = null;
		if (itemStats.containsKey(itemId))
		{
			stat = itemStats.get(itemId);
			stat.lootChance = chance;
			stat.notGetChance = 1d - stat.lootChance / 100d;
		}
		else
			itemStats.put(itemId, new ItemStat(itemId, chance));
	}
	
	public int getItemKillCount(int itemId)
	{
		if (!itemStats.containsKey(itemId))
			return 0;
		ItemStat stat = itemStats.get(itemId);
		return stat.killsDone;
	}
}