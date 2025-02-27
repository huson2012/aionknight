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

	@Property(key = "gameserver.rate.vip.group.xp", defaultValue = "3")
	public static int VIP_GROUPXP_RATE;

	@Property(key = "gameserver.rate.regular.xp", defaultValue = "1")
	public static int XP_RATE;

	@Property(key = "gameserver.rate.premium.xp", defaultValue = "2")
	public static int PREMIUM_XP_RATE;

	@Property(key = "gameserver.rate.vip.xp", defaultValue = "3")
	public static int VIP_XP_RATE;

	@Property(key = "gameserver.rate.regular.quest.xp", defaultValue = "1")
	public static int QUEST_XP_RATE;

	@Property(key = "gameserver.rate.premium.quest.xp", defaultValue = "2")
	public static int PREMIUM_QUEST_XP_RATE;

	@Property(key = "gameserver.rate.vip.quest.xp", defaultValue = "3")
	public static int VIP_QUEST_XP_RATE;

	@Property(key = "gameserver.rate.regular.gathering.xp", defaultValue = "1")
	public static float GATHERING_XP_RATE;

	@Property(key = "gameserver.rate.premium.gathering.xp", defaultValue = "2")
	public static float PREMIUM_GATHERING_XP_RATE;

	@Property(key = "gameserver.rate.vip.gathering.xp", defaultValue = "3")
	public static float VIP_GATHERING_XP_RATE;

	@Property(key = "gameserver.rate.regular.gathering.lvl", defaultValue = "1")
	public static float GATHERING_LVL_RATE;

	@Property(key = "gameserver.rate.premium.gathering.lvl", defaultValue = "2")
	public static float PREMIUM_GATHERING_LVL_RATE;

	@Property(key = "gameserver.rate.vip.gathering.lvl", defaultValue = "3")
	public static float VIP_GATHERING_LVL_RATE;

	@Property(key = "gameserver.rate.regular.crafting.xp", defaultValue = "1")
	public static float CRAFTING_XP_RATE;

	@Property(key = "gameserver.rate.premium.crafting.xp", defaultValue = "2")
	public static float PREMIUM_CRAFTING_XP_RATE;

	@Property(key = "gameserver.rate.vip.crafting.xp", defaultValue = "3")
	public static float VIP_CRAFTING_XP_RATE;

	@Property(key = "gameserver.rate.regular.crafting.lvl", defaultValue = "1")
	public static float CRAFTING_LVL_RATE;

	@Property(key = "gameserver.rate.premium.crafting.lvl", defaultValue = "2")
	public static float PREMIUM_CRAFTING_LVL_RATE;

	@Property(key = "gameserver.rate.vip.crafting.lvl", defaultValue = "3")
	public static float VIP_CRAFTING_LVL_RATE;

	@Property(key = "gameserver.rate.regular.quest.kinah", defaultValue = "1")
	public static int QUEST_KINAH_RATE;

	@Property(key = "gameserver.rate.premium.quest.kinah", defaultValue = "2")
	public static int PREMIUM_QUEST_KINAH_RATE;

	@Property(key = "gameserver.rate.vip.quest.kinah", defaultValue = "3")
	public static int VIP_QUEST_KINAH_RATE;

	@Property(key = "gameserver.rate.regular.drop", defaultValue = "1")
	public static int DROP_RATE;

	@Property(key = "gameserver.rate.premium.drop", defaultValue = "2")
	public static int PREMIUM_DROP_RATE;

	@Property(key = "gameserver.rate.vip.drop", defaultValue = "3")
	public static int VIP_DROP_RATE;

	@Property(key = "gameserver.rate.chest.regular.drop", defaultValue = "1")
	public static int CHEST_DROP_RATE;

	@Property(key = "gameserver.rate.chest.premium.drop", defaultValue = "2")
	public static int PREMIUM_CHEST_DROP_RATE;

	@Property(key = "gameserver.rate.chest.vip.drop", defaultValue = "3")
	public static int VIP_CHEST_DROP_RATE;

	@Property(key = "gameserver.rate.regular.ap.player", defaultValue = "1")
	public static float AP_PLAYER_RATE;

	@Property(key = "gameserver.rate.premium.ap.player", defaultValue = "2")
	public static float PREMIUM_AP_PLAYER_RATE;

	@Property(key = "gameserver.rate.vip.ap.player", defaultValue = "3")
	public static float VIP_AP_PLAYER_RATE;

	@Property(key = "gameserver.rate.regular.ap.npc", defaultValue = "1")
	public static float AP_NPC_RATE;

	@Property(key = "gameserver.rate.premium.ap.npc", defaultValue = "2")
	public static float PREMIUM_AP_NPC_RATE;

	@Property(key = "gameserver.rate.vip.ap.npc", defaultValue = "3")
	public static float VIP_AP_NPC_RATE;

	@Property(key = "gameserver.rate.regular.kinah", defaultValue = "1")
	public static int KINAH_RATE;

	@Property(key = "gameserver.rate.premium.kinah", defaultValue = "2")
	public static int PREMIUM_KINAH_RATE;

	@Property(key = "gameserver.rate.vip.kinah", defaultValue = "3")
	public static int VIP_KINAH_RATE;

	@Property(key = "gameserver.rate.regular.broker", defaultValue = "8")
	public static int BOKER_RATE;

	@Property(key = "gameserver.rate.premium.broker", defaultValue = "16")
	public static int PREMIUM_BOKER_RATE;

	@Property(key = "gameserver.rate.vip.broker", defaultValue = "32")
	public static int VIP_BOKER_RATE;
}