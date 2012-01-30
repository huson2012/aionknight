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

package gameserver.model.group;

import gameserver.model.templates.item.ItemQuality;

public class LootGroupRules
{
	private LootRuleType lootRule;
	private LootDistribution autodistribution;
	private int common_item_above;
	private int superior_item_above;
	private int heroic_item_above;
	private int fabled_item_above;
	private int ethernal_item_above;
	private int over_ethernal;
	private int over_over_ethernal;
	public LootGroupRules()
	{
		this.lootRule = LootRuleType.ROUNDROBIN;
		this.autodistribution = LootDistribution.NORMAL;
		common_item_above = 0;
		superior_item_above = 2;
		heroic_item_above = 2;
		fabled_item_above = 2;
		ethernal_item_above = 2;
		over_ethernal = 2;
		over_over_ethernal = 0;		
	}
	
	public LootGroupRules(LootRuleType lootRule, LootDistribution autodistribution, int commonItemAbove, int superiorItemAbove,
		int heroicItemAbove, int fabledItemAbove, int ethernalItemAbove, int overEthernal, int overOverEthernal)
	{
		super();
		this.lootRule = lootRule;
		this.autodistribution = autodistribution;
		common_item_above = commonItemAbove;
		superior_item_above = superiorItemAbove;
		heroic_item_above = heroicItemAbove;
		fabled_item_above = fabledItemAbove;
		ethernal_item_above = ethernalItemAbove;
		over_ethernal = overEthernal;
		over_over_ethernal = overOverEthernal;
	}

	public int getQualityRule(ItemQuality quality)
	{
		switch(quality)
		{
			case COMMON:
					return common_item_above;
			case RARE:
					return superior_item_above;
			case LEGEND:
					return heroic_item_above;
			case UNIQUE:
					return fabled_item_above;
			case EPIC:
					return ethernal_item_above;
			case MYTHIC:
					return over_ethernal;
		}
		return 0;
	}	

	public LootRuleType getLootRule()
	{
		return lootRule;
	}

	public LootDistribution getAutodistribution()
	{
		return autodistribution;
	}

	public int getCommon_item_above()
	{
		return common_item_above;
	}

	public int getSuperior_item_above()
	{
		return superior_item_above;
	}

	public int getHeroic_item_above()
	{
		return heroic_item_above;
	}

	public int getFabled_item_above()
	{
		return fabled_item_above;
	}

	public int getEthernal_item_above()
	{
		return ethernal_item_above;
	}

	public int getOver_ethernal()
	{
		return over_ethernal;
	}

	public int getOver_over_ethernal()
	{
		return over_over_ethernal;
	}
}
