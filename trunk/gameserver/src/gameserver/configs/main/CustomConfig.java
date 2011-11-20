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

public class CustomConfig
{

	@Property(key = "gameserver.factions.speaking.mode", defaultValue = "0")
	public static int FACTIONS_SPEAKING_MODE;

	@Property(key = "gameserver.factions.whisper.mode", defaultValue = "0")
	public static int FACTIONS_WHISPER_MODE;

	@Property(key = "gameserver.factions.search.mode", defaultValue = "false")
	public static boolean FACTIONS_SEARCH_MODE;

	@Property(key = "gameserver.skill.autolearn", defaultValue = "false")
	public static boolean SKILL_AUTOLEARN;

	@Property(key = "gameserver.stigma.autolearn", defaultValue = "false")
	public static boolean STIGMA_AUTOLEARN;

	@Property(key = "gameserver.character.delete.retail", defaultValue = "true")
	public static boolean RETAIL_CHAR_DELETION;

	@Property(key = "gameserver.disable.mob.aggro", defaultValue = "false")
	public static boolean DISABLE_MOB_AGGRO;

	@Property(key = "gameserver.enable.simple.2ndclass", defaultValue = "false")
	public static boolean ENABLE_SIMPLE_2NDCLASS;
	
	@Property(key = "gameserver.enable.decor.christmas", defaultValue = "false")
	public static boolean ENABLE_DECOR_CHRISTMAS;

	@Property(key = "gameserver.unstuck.delay", defaultValue = "3600")
	public static int UNSTUCK_DELAY;

	@Property(key = "gameserver.instances.enable", defaultValue = "true")
	public static boolean ENABLE_INSTANCES;

	@Property(key = "gameserver.base.flytime", defaultValue = "60")
	public static int BASE_FLYTIME;

	@Property(key = "gameserver.cross.faction.binding", defaultValue = "false")
	public static boolean ENABLE_CROSS_FACTION_BINDING;

	@Property(key = "gameserver.pvp.maxkills", defaultValue = "5")
	public static int MAX_DAILY_PVP_KILLS;

	@Property(key = "gameserver.pvp.period", defaultValue = "24")
	public static int DAILY_PVP_PERIOD;

	@Property(key = "gameserver.channels.all.enabled", defaultValue = "false")
	public static boolean CHANNEL_ALL_ENABLED;

	@Property(key = "gameserver.channels.world.enabled", defaultValue = "false")
	public static boolean CHANNEL_WORLD_ENABLED;
	
	@Property(key = "gameserver.channels.message.interval", defaultValue = "0")
	public static byte CHANNEL_MESSAGE_INTERVAL;

	@Property(key = "gameserver.search.listall", defaultValue = "false")
	public static boolean SEARCH_LIST_ALL;

	@Property(key = "gameserver.gmtag.display", defaultValue = "false")
	public static boolean GMTAG_DISPLAY;

	@Property(key = "gameserver.gmtag.level1", defaultValue = "<GM>")
	public static String GM_LEVEL1;

	@Property(key = "gameserver.gmtag.level2", defaultValue = "<HEADGM>")
	public static String GM_LEVEL2;

	@Property(key = "gameserver.gmtag.level3", defaultValue = "<ADMIN>")
	public static String GM_LEVEL3;

	@Property(key = "gameserver.announce.gm.connection", defaultValue = "false")
	public static boolean ANNOUNCE_GM_CONNECTION;

	@Property(key = "gameserver.invis.gm.connection", defaultValue = "false")
	public static boolean INVIS_GM_CONNECTION;

	@Property(key = "gameserver.invul.gm.connection", defaultValue = "false")
	public static boolean INVUL_GM_CONNECTION;

	@Property(key = "gameserver.silence.gm.connection", defaultValue = "false")
	public static boolean SILENCE_GM_CONNECTION;

	@Property(key = "gameserver.speed.gm.connection", defaultValue = "0")
	public static int SPEED_GM_CONNECTION;

	@Property(key = "gameserver.instance.cooldown", defaultValue = "true")
	public static boolean INSTANCE_COOLDOWN;

	@Property(key = "gameserver.announce.raredrops", defaultValue = "false")
	public static boolean ANNOUNCE_RAREDROPS;

	@Property(key = "gameserver.kick.speedhack.enable", defaultValue = "true")
	public static boolean KICK_SPEEDHACK;

	@Property(key = "gameserver.kick.speedhack.pinginterval", defaultValue = "100000")
	public static long KICK_PINGINTERVAL;

	@Property(key = "gameserver.log.castspell.targethack", defaultValue = "true")
	public static boolean LOG_CASTSPELL_TARGETHACK;

	@Property(key = "gameserver.log.castspell.speedhack", defaultValue = "true")
	public static boolean LOG_CASTSPELL_SPEEDHACK;

	@Property(key = "gameserver.log.castspell.cooldownhack", defaultValue = "true")
	public static boolean LOG_CASTSPELL_COOLDOWNHACK;

    @Property(key = "gameserver.skill.chain.trigger", defaultValue = "true")
    public static boolean SKILL_CHAIN_TRIGGER;
    
    @Property(key = "gameserver.skill.chain.rate", defaultValue = "80")
    public static int SKILL_CHAIN_RATE;

	@Property(key = "gameserver.pvpreward.enable", defaultValue = "false")
	public static boolean PVPREWARD_ENABLE;

	@Property(key = "gameserver.pvpreward.kills.needed1", defaultValue = "5")
	public static int PVPREWARD_KILLS_NEEDED1;

	@Property(key = "gameserver.pvpreward.kills.needed2", defaultValue = "10")
	public static int PVPREWARD_KILLS_NEEDED2;

	@Property(key = "gameserver.pvpreward.kills.needed3", defaultValue = "15")
	public static int PVPREWARD_KILLS_NEEDED3;

	@Property(key = "gameserver.pvpreward.item.reward1", defaultValue = "186000031")
	public static int PVPREWARD_ITEM_REWARD1;

	@Property(key = "gameserver.pvpreward.item.reward2", defaultValue = "186000030")
	public static int PVPREWARD_ITEM_REWARD2;

	@Property(key = "gameserver.pvpreward.item.reward3", defaultValue = "186000096")
	public static int PVPREWARD_ITEM_REWARD3;

	@Property(key = "search.level.restriction", defaultValue = "10")
	public static int LEVEL_TO_SEARCH;

	@Property(key = "whisper.level.restriction", defaultValue = "10")
	public static int LEVEL_TO_WHISPER;

	@Property(key = "gameserver.player.experience.control", defaultValue = "false")
	public static boolean PLAYER_EXPERIENCE_CONTROL;

	@Property(key = "gameserver.disconnect.time", defaultValue = "10")
	public static int DISCONNECT_DELAY;

	@Property(key = "gameserver.enable.surveys", defaultValue = "false")
	public static boolean	ENABLE_SURVEYS;

	@Property(key = "enable.html.welcome", defaultValue = "false")
	public static boolean   ENABLE_HTML_WELCOME;

	@Property(key = "gameserver.topranking.time", defaultValue = "0:00:00")
	public static String TOP_RANKING_TIME;

	@Property(key = "gameserver.topranking.delay", defaultValue = "24")
	public static int TOP_RANKING_DELAY;

	@Property(key = "gameserver.dailyquest.time", defaultValue = "9:00:00")
	public static String DAILY_START_TIME;

	@Property(key = "gameserver.rift.race", defaultValue = "false")
	public static boolean RIFT_RACE;

	@Property(key = "gameserver.criticaleffect", defaultValue = "false")
	public static boolean CRITICAL_EFFECTS;

	@Property(key = "gameserver.geodata.related.effects", defaultValue = "false")
	public static boolean GEODATA_EFFECTS_ENABLED;

	@Property(key = "gameserver.advstigmaslot.onlvlup", defaultValue = "false")
	public static boolean ADVSTIGMA_ONLVLUP;

	@Property(key = "gameserver.droplist.master.source", defaultValue = "xml")
	public static String GAMESERVER_DROPLIST_MASTER_SOURCE;

	@Property(key = "gameserver.crafting.speedupchance", defaultValue = "15")
	public static int CRAFTING_SPEEDUP;

	@Property(key = "gameserver.regular.crafting.success", defaultValue = "33")
	public static int REGULAR_CRAFTING_SUCCESS;

	@Property(key = "gameserver.critical.crafting.success", defaultValue = "30")
	public static int CRITICAL_CRAFTING_SUCCESS;

	@Property(key = "gameserver.workorder.bonus", defaultValue = "false")
	public static boolean WORK_ORDER_BONUS;

	@Property(key = "gameserver.mastercraft.limit.disable", defaultValue = "false")
	public static boolean MASTERCRAFT_LIMIT_DISABLE;

	@Property(key = "gameserver.abyssxform.afterlogout", defaultValue = "false")
	public static boolean ABYSS_XFORM_DURATION_AFTER_LOGOUT;
	
	@Property(key = "gameserver.dmgreduction.lvldiffpvp", defaultValue = "false")
	public static boolean DMG_REDUCTION_LVL_DIFF_PVP;
	
	@Property(key = "gameserver.cmotions.retail", defaultValue = "true")
	public static boolean       RETAIL_CMOTIONS;
	   
	@Property(key = "gameserver.cmotions.getlevel", defaultValue = "20")
	public static int           CMOTIONS_GETLEVEL;

	@Property(key = "gameserver.emotions.retail", defaultValue = "true")
	public static boolean RETAIL_EMOTIONS;

	@Property(key = "gameserver.dredgion.ap.win.bonus", defaultValue = "3000")
	public static int DREDGION_AP_WIN;

	@Property(key = "gameserver.dredgion.ap.lose.bonus", defaultValue = "1000")
	public static int DREDGION_AP_LOSE;

	@Property(key = "gameserver.chantradredgion.ap.win.bonus", defaultValue = "5000")
	public static int CHANTRA_DREDGION_AP_WIN;

	@Property(key = "gameserver.chantradredgion.ap.lose.bonus", defaultValue = "5000")
	public static int CHANTRA_DREDGION_AP_LOSE;

	@Property(key = "gameserver.npc.relation.aggro", defaultValue = "true")
	public static boolean NPC_RELATION_AGGRO;
	
	@Property(key = "gameserver.npc.dynamicstat", defaultValue = "false")
	public static boolean NPC_DYNAMIC_STAT;

	@Property(key = "gameserver.aionshop.database", defaultValue = "ak_server_ls")
	public static String AIONSHOP_DB;

	@Property(key = "gameserver.aionshop.gift.enable", defaultValue = "false")
	public static boolean AIONSHOP_GIFT_ENABLE;

	@Property(key = "gameserver.rate.tollexchange.enable", defaultValue = "true")
	public static boolean TOLL_EXCHANGE_ENABLED;

	@Property(key = "gameserver.rate.tollexchange.restriction", defaultValue = "none")
	public static String TOLL_EXCHANGE_RESTRICTION;

	@Property(key = "gameserver.rate.tollexchange.ap", defaultValue = "10")
	public static int TOLL_EXCHANGE_AP_RATE;

	@Property(key = "gameserver.rate.tollexchange.kinah", defaultValue = "1000")
	public static int TOLL_EXCHANGE_KINAH_RATE;

    @Property(key = "gameserver.dredgion.enable", defaultValue = "true")
    public static boolean ENABLE_DREDGION;
}