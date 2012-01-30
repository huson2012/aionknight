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

package gameserver.utils.rates;

import gameserver.configs.main.RateConfig;

public class PremiumRates extends Rates
{
	@Override
	public int getGroupXpRate()
	{
		return RateConfig.PREMIUM_GROUPXP_RATE;
	}

	@Override
	public float getApNpcRate()
	{
		return RateConfig.PREMIUM_AP_NPC_RATE;
	}

	@Override
	public float getApPlayerRate()
	{
		return RateConfig.PREMIUM_AP_PLAYER_RATE;
	}

	@Override
	public int getDropRate()
	{
		return RateConfig.PREMIUM_DROP_RATE;
	}

	@Override
	public int getChestDropRate()
	{
		return RateConfig.PREMIUM_CHEST_DROP_RATE;
	}

	@Override
	public int getQuestKinahRate()
	{
		return RateConfig.PREMIUM_QUEST_KINAH_RATE;
	}

	@Override
	public int getQuestXpRate()
	{
		return RateConfig.PREMIUM_QUEST_XP_RATE;
	}

	@Override
	public int getXpRate()
	{
		return RateConfig.PREMIUM_XP_RATE;
	}

	@Override
	public float getCraftingXPRate()
	{
		return RateConfig.PREMIUM_CRAFTING_XP_RATE;
	}

	@Override
	public float getCraftingLvlRate()
	{
		return RateConfig.PREMIUM_CRAFTING_LVL_RATE;
	}

	@Override
	public float getGatheringXPRate()
	{
		return RateConfig.PREMIUM_GATHERING_XP_RATE;
	}

	@Override
	public float getGatheringLvlRate()
	{
		return RateConfig.PREMIUM_GATHERING_LVL_RATE;
	}

	@Override
	public int getKinahRate()
	{
		return RateConfig.PREMIUM_KINAH_RATE;
	}

	@Override
	public int getBokerRate()
	{
		return RateConfig.PREMIUM_BOKER_RATE;
	}
}
