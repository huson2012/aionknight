/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.utils.rates;

import gameserver.configs.main.RateConfig;

public class VipRates extends Rates
{
	@Override
	public int getGroupXpRate()
	{
		return RateConfig.VIP_GROUPXP_RATE;
	}

	@Override
	public float getApNpcRate()
	{
		return RateConfig.VIP_AP_NPC_RATE;
	}

	@Override
	public float getApPlayerRate()
	{
		return RateConfig.VIP_AP_PLAYER_RATE;
	}

	@Override
	public int getDropRate()
	{
		return RateConfig.VIP_DROP_RATE;
	}

	@Override
	public int getChestDropRate()
	{
		return RateConfig.VIP_CHEST_DROP_RATE;
	}

	@Override
	public int getQuestKinahRate()
	{
		return RateConfig.VIP_QUEST_KINAH_RATE;
	}

	@Override
	public int getQuestXpRate()
	{
		return RateConfig.VIP_QUEST_XP_RATE;
	}

	@Override
	public int getXpRate()
	{
		return RateConfig.VIP_XP_RATE;
	}

	@Override
	public float getCraftingXPRate()
	{
		return RateConfig.VIP_CRAFTING_XP_RATE;
	}

	@Override
	public float getCraftingLvlRate()
	{
		return RateConfig.VIP_CRAFTING_LVL_RATE;
	}

	@Override
	public float getGatheringXPRate()
	{
		return RateConfig.VIP_GATHERING_XP_RATE;
	}

	@Override
	public float getGatheringLvlRate()
	{
		return RateConfig.VIP_GATHERING_LVL_RATE;
	}

	@Override
	public int getKinahRate()
	{
		return RateConfig.VIP_KINAH_RATE;
	}

	@Override
	public int getBokerRate()
	{
		return RateConfig.VIP_BOKER_RATE;
	}
}
