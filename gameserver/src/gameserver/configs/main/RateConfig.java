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

package gameserver.configs.main;

import commons.configuration.Property;

public class RateConfig
{
	@Property(key = "gameserver.rate.display.rates", defaultValue = "false")
	public static boolean DISPLAY_RATE;

	@Property(key = "gameserver.rate.regular.group.xp", defaultValue = "1")
	public static int GROUPXP_RATE;

	@Property(key = "gameserver.rate.premium.group.xp", defaultValue = "2")
	public static int PREMIUM_GROUPXP_RATE;

	@Property(key = "gameserver.rate.regular.xp", defaultValue = "1")
	public static int XP_RATE;

	@Property(key = "gameserver.rate.premium.xp", defaultValue = "2")
	public static int PREMIUM_XP_RATE;

	@Property(key = "gameserver.rate.regular.quest.xp", defaultValue = "1")
	public static int QUEST_XP_RATE;

	@Property(key = "gameserver.rate.premium.quest.xp", defaultValue = "2")
	public static int PREMIUM_QUEST_XP_RATE;

	@Property(key = "gameserver.rate.regular.gathering.xp", defaultValue = "1")
	public static float	GATHERING_XP_RATE;

	@Property(key = "gameserver.rate.premium.gathering.xp", defaultValue = "1")
	public static float	PREMIUM_GATHERING_XP_RATE;

	@Property(key = "gameserver.rate.regular.gathering.lvl", defaultValue = "1")
	public static float	GATHERING_LVL_RATE;

	@Property(key = "gameserver.rate.premium.gathering.lvl", defaultValue = "1")
	public static float	PREMIUM_GATHERING_LVL_RATE;

	@Property(key = "gameserver.rate.regular.crafting.xp", defaultValue = "1")
	public static float	CRAFTING_XP_RATE;

	@Property(key = "gameserver.rate.premium.crafting.xp", defaultValue = "1")
	public static float	PREMIUM_CRAFTING_XP_RATE;

	@Property(key = "gameserver.rate.regular.crafting.lvl", defaultValue = "1")
	public static float	CRAFTING_LVL_RATE;

	@Property(key = "gameserver.rate.premium.crafting.lvl", defaultValue = "1")
	public static float	PREMIUM_CRAFTING_LVL_RATE;

	@Property(key = "gameserver.rate.regular.quest.kinah", defaultValue = "1")
	public static int QUEST_KINAH_RATE;

	@Property(key = "gameserver.rate.premium.quest.kinah", defaultValue = "2")
	public static int PREMIUM_QUEST_KINAH_RATE;

	@Property(key = "gameserver.rate.regular.drop", defaultValue = "1")
	public static int DROP_RATE;

	@Property(key = "gameserver.rate.premium.drop", defaultValue = "2")
	public static int PREMIUM_DROP_RATE;

	@Property(key = "gameserver.rate.chest.regular.drop", defaultValue = "1")
	public static int CHEST_DROP_RATE;

	@Property(key = "gameserver.rate.chest.premium.drop", defaultValue = "2")
	public static int PREMIUM_CHEST_DROP_RATE;

	@Property(key = "gameserver.rate.regular.ap.player", defaultValue = "1")
	public static float	AP_PLAYER_RATE;

	@Property(key = "gameserver.rate.premium.ap.player", defaultValue = "2")
	public static float	PREMIUM_AP_PLAYER_RATE;

	@Property(key = "gameserver.rate.regular.ap.npc", defaultValue = "1")
	public static float	AP_NPC_RATE;

	@Property(key = "gameserver.rate.premium.ap.npc", defaultValue = "2")
	public static float	PREMIUM_AP_NPC_RATE;

	@Property(key = "gameserver.rate.regular.kinah", defaultValue = "1")
	public static int KINAH_RATE;

	@Property(key = "gameserver.rate.premium.kinah", defaultValue = "2")
	public static int PREMIUM_KINAH_RATE;
}