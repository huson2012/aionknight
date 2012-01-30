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

package gameserver.model.drop;

import gameserver.configs.main.DropConfig;
import gnu.trove.TIntObjectHashMap;
import java.util.Set;

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
		if (dropData == null || dropData.isEmpty())
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
