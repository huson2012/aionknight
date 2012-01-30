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
