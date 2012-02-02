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

package gameserver.network.aion;

import gameserver.configs.main.GSConfig;
import gameserver.network.aion.serverpackets.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerPacketsOpcodes
{
	private static ServerPacketsOpcodes serverPackets = new ServerPacketsOpcodes();
	protected Map<Class<? extends AionServerPacket>, Integer> opcodes = new HashMap<Class<? extends AionServerPacket>, Integer>();
	private ServerPacketsOpcodes()
	{
		if(GSConfig.SERVER_VERSION.startsWith("2.7"))
			initPacketsFor_2_7();
		
		if(GSConfig.SERVER_VERSION.startsWith("2.6"))
			initPacketsFor_2_6();
		
		if(GSConfig.SERVER_VERSION.startsWith("2.5"))
			initPacketsFor_2_5();
	}

	static int getOpcode(Class<? extends AionServerPacket> packetClass)
	{
		return serverPackets.getOpcodeForPacket(packetClass);
	}

	private int getOpcodeForPacket(Class<? extends AionServerPacket> packetClass)
	{
		Integer opcode = opcodes.get(packetClass);
		if(opcode == null)
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");

		return opcode;
	}

	private void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet)
	{
		if(opcode < 0)
			return;

		if(idSet.contains(opcode))
			throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X", opcode));

		idSet.add(opcode);
		opcodes.put(packetClass, opcode);
	}

	private void initPacketsFor_2_7()
	{
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_STATS_INFO.class, 0x0001, idSet);// 2.7
		addPacketOpcode(SM_CHAT_INIT.class, 0x0104, idSet);// 2.7
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x0105, idSet);// 2.7
		addPacketOpcode(SM_MACRO_RESULT.class, 0x0106, idSet);// 2.7
		addPacketOpcode(SM_MACRO_LIST.class, 0x0107, idSet);// 2.7
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x0109, idSet);// 2.7
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x010A, idSet); // 2.7 Testing
		addPacketOpcode(SM_RIFT_STATUS.class, 0x01AF, idSet); // 2.7 Testing
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x010B, idSet);// 2.7
		addPacketOpcode(SM_ABYSS_RANK.class, 0x010D, idSet);// 2.7
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x000E, idSet);// 2.7
		addPacketOpcode(SM_PETITION.class, 0x000F, idSet);// 2.7
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x0110, idSet);// 2.7
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x0111, idSet);// 2.7
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x0012, idSet);// 2.7
		addPacketOpcode(SM_DELETE.class, 0x0014, idSet);// 2.7
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x0015, idSet);// 2.7
		addPacketOpcode(SM_MESSAGE.class, 0x0016, idSet);// 2.7
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x0017, idSet);// 2.7
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x0018, idSet);// 2.7
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x0019, idSet);// 2.7
		addPacketOpcode(SM_DELETE_ITEM.class, 0x001A, idSet);// 2.7
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x001B, idSet);// 2.7
		addPacketOpcode(SM_UI_SETTINGS.class, 0x001C, idSet);// 2.7
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x001D, idSet);// 2.7
		addPacketOpcode(SM_PLAYER_INFO.class, 0x001E, idSet);// 2.7
		addPacketOpcode(SM_STANCE_STATE.class, 0x001F, idSet);// 2.7
		addPacketOpcode(SM_GATHER_STATUS.class, 0x0020, idSet);// 2.7
		addPacketOpcode(SM_CASTSPELL.class, 0x0021, idSet);// 2.7
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x0022, idSet);// 2.7
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x0023, idSet);// 2.7
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x0024, idSet);// 2.7
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x0025, idSet);// 2.7
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x0026, idSet);// 2.7
		addPacketOpcode(SM_DP_INFO.class, 0x0027, idSet);// 2.7
		addPacketOpcode(SM_LEGION_TABS.class, 0x002A, idSet);// 2.7
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x012B, idSet);// 2.7
		addPacketOpcode(SM_NPC_INFO.class, 0x002C, idSet);// 2.7
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x002D, idSet);// 2.7
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x002F, idSet);// 2.7
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x0031, idSet);// 2.7
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x0032, idSet);// 2.7
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x0033, idSet);// 2.7
		addPacketOpcode(SM_ATTACK.class, 0x0034, idSet);// 2.7
		addPacketOpcode(SM_MOVE.class, 0x0037, idSet);// 2.7
		addPacketOpcode(SM_TRANSFORM.class, 0x0038, idSet);// 2.7
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x003A, idSet);// 2.7
		addPacketOpcode(SM_SELL_ITEM.class, 0x003C, idSet);// 2.7
		addPacketOpcode(SM_WEATHER.class, 0x0040, idSet);// 2.7
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x0041, idSet);// 2.7
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x0042, idSet);// 2.7
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x0043, idSet);// 2.7
		addPacketOpcode(SM_GAME_TIME.class, 0x0044, idSet);// 2.7
		addPacketOpcode(SM_EMOTION.class, 0x0045, idSet);// 2.7
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x0046, idSet);// 2.7
		addPacketOpcode(SM_TIME_CHECK.class, 0x0047, idSet);// 2.7
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x0048, idSet);// 2.7
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x0049, idSet);// 2.7
		addPacketOpcode(SM_SKILL_LIST.class, 0x004A, idSet);// 2.7
		addPacketOpcode(SM_CASTSPELL_END.class, 0x004B, idSet);// 2.7
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x004C, idSet);// 2.7
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x004D, idSet);// 2.7
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x0050, idSet);// 2.7
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x0051, idSet);// 2.7
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x0153, idSet);// 2.7
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x0154, idSet);// 2.7
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x0155, idSet);// 2.7
		addPacketOpcode(SM_NAME_CHANGE.class, 0x0156, idSet);// 2.7
		addPacketOpcode(SM_GROUP_INFO.class, 0x0158, idSet);// 2.7
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x0159, idSet);// 2.7
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x015B, idSet);// 2.7
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x015E, idSet);// 2.7
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x0160, idSet);// 2.7
		addPacketOpcode(SM_PLAYER_STATE.class, 0x0162, idSet);// 2.7
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x0164, idSet);// 2.7
		addPacketOpcode(SM_KEY.class, 0x0166, idSet);// 2.7
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x0167, idSet);// 2.7
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x0168, idSet);// 2.7
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x0169, idSet);// 2.7
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x016B, idSet);// 2.7
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x016C, idSet);// 2.7
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x016D, idSet);// 2.7
		addPacketOpcode(SM_EMOTION_LIST.class, 0x016F, idSet);// 2.7
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x0171, idSet);// 2.7
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x0177, idSet);// 2.7
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x0178, idSet);// 2.7
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x0179, idSet);// 2.7
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x017A, idSet);// 2.7
		addPacketOpcode(SM_QUEST_LIST.class, 0x017B, idSet);// 2.7
		addPacketOpcode(SM_PING_RESPONSE.class, 0x017E, idSet);// 2.7
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x017F, idSet);// 2.7
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x0180, idSet);// 2.7
		addPacketOpcode(SM_PET.class, 0x0185, idSet);// 2.7
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x0186, idSet);// 2.7
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x0187, idSet);// 2.7
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x0189, idSet);// 2.7
		addPacketOpcode(SM_LEGION_INFO.class, 0x018C, idSet);// 2.7
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x018E, idSet);// 2.7
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x018F, idSet);// 2.7
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x0190, idSet);// 2.7
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x0191, idSet);// 2.7
		addPacketOpcode(SM_CMOTION.class, 0x0192, idSet);// 2.6 0x92, 2.7
		addPacketOpcode(SM_TRADEINTRADELIST.class, 0x0197, idSet); // 2.7
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x0198, idSet); // 2.7
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x0199, idSet);// 2.7
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x019B, idSet);// 2.7
		addPacketOpcode(SM_LEGION_EDIT.class, 0x019C, idSet);// 2.7
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x019D, idSet);// 2.7
		addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x019F, idSet);// 2.7
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0x01A0, idSet);// 2.7
		addPacketOpcode(SM_MAIL_SERVICE.class, 0x01A1, idSet);// 2.7
		addPacketOpcode(SM_FRIEND_LIST.class, 0x01A2, idSet);// 22.7
		addPacketOpcode(SM_PRIVATE_STORE.class, 0x01A4, idSet); // 2.7
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA6, idSet);// 2.1
		addPacketOpcode(SM_GROUP_LOOT.class, 0x01A7, idSet);// 2.7
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0x01A8, idSet);// 2.7
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0x01A9, idSet);// 2.7
		addPacketOpcode(SM_STAGE_STEP_STATUS.class, 0x01AA, idSet); // 2.7
		addPacketOpcode(SM_ACADEMY_BOOTCAMP_STAGE.class, 0xAB, idSet); // 2.5
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0x01AB, idSet);// 2.7
		addPacketOpcode(SM_PONG.class, 0x01AC, idSet);// 2.7
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0x01AD, idSet);// 2.7
		addPacketOpcode(SM_KISK_UPDATE.class, 0x01AE, idSet);// 2.7
		addPacketOpcode(SM_BROKER_ITEMS.class, 0x01B0, idSet);// 2.7
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0x01B1, idSet);// 2.7
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0x01B2, idSet);// 2.7
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0x01B4, idSet);// 2.7
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0x01B5, idSet);// 2.7
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0x01B6, idSet);// 2.7
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0x01B7, idSet);// 2.7
		addPacketOpcode(SM_DUEL.class, 0x01B9, idSet);// 2.7
		addPacketOpcode(SM_PET_MOVE.class, 0x01BB, idSet);// 2.7
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0x01BF, idSet);// 2.7
		addPacketOpcode(SM_RESURRECT.class, 0x01C0, idSet);// 2.7
		addPacketOpcode(SM_DIE.class, 0x01C1, idSet);// 2.7
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0x01C2, idSet);// 2.7
		addPacketOpcode(SM_WINDSTREAM.class, 0x01C3, idSet);// 2.7
		addPacketOpcode(SM_FIND_GROUP.class, 0x01C4, idSet);// 2.7
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0x01C6, idSet);// 2.7
		addPacketOpcode(SM_REPURCHASE.class, 0x01C7, idSet);// 2.7
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0x01C8, idSet);// 2.7
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0x01C9, idSet);// 2.7
		addPacketOpcode(SM_INGAMESHOP.class, 0x01CA, idSet);// 2.7
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0x01CB, idSet);// 2.7
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0x01CC, idSet);// 2.7
		addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0x01CD, idSet);// 2.7
		addPacketOpcode(SM_TITLE_LIST.class, 0x01CE, idSet);// 2.7
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0x01D1, idSet);// 2.7
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0x01D3, idSet);// 2.7
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xBF, idSet);// 2.7
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0x01D5, idSet);// 2.7
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0x01D7, idSet);// 2.7
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0x01D8, idSet);// 2.7
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0x01DA, idSet);// 2.7
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0x00DC, idSet);// 2.6
		addPacketOpcode(SM_BLOCK_LIST.class, 0x01DE, idSet);// 2.7
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0x00DF, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0x00E0, idSet);// 2.1
		addPacketOpcode(SM_TELEPORT_MAP.class, 0x01E2, idSet);// 2.7
		addPacketOpcode(SM_FORCED_MOVE.class, 0x01E3, idSet);// 2.7
		addPacketOpcode(SM_USE_OBJECT.class, 0x01E5, idSet);// 2.7
		addPacketOpcode(SM_CHARACTER_LIST.class, 0x01E6, idSet);// 2.7
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0x01E7, idSet);// 2.7
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0x01E8, idSet);// 2.7
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0x01E9, idSet);// 2.7
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0x01EA, idSet);// 2.7
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0x01EB, idSet);// 2.7
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0x01EC, idSet);// 2.7
		addPacketOpcode(SM_LOOT_STATUS.class, 0x01ED, idSet);// 2.7
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0x01EE, idSet);// 2.7
		addPacketOpcode(SM_RECIPE_LIST.class, 0x01EF, idSet);// 2.7
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0x01F1, idSet);// 2.7
		addPacketOpcode(SM_FLY_TIME.class, 0x01F2, idSet);// 2.7
		addPacketOpcode(SM_FORTRESS_INFO.class, 0x01F3, idSet);// 2.7
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0x01F4, idSet);// 2.7
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0x01F5, idSet);// 2.7
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0x01F7, idSet);// 2.7
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0x01F8, idSet);// 2.7
		addPacketOpcode(SM_SHOW_BRAND.class, 0x01F9, idSet);// 2.7
		addPacketOpcode(SM_PRICES.class, 0x01FA, idSet);// 2.7
		addPacketOpcode(SM_TRADELIST.class, 0x01FD, idSet);// 2.7
		addPacketOpcode(SM_VERSION_CHECK.class, 0x00FE, idSet);// 2.7
		addPacketOpcode(SM_RECONNECT_KEY.class, 0x01FF, idSet);// 2.7
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
	}

	private void initPacketsFor_2_6()
	{
		Set<Integer> idSet = new HashSet<Integer>();

addPacketOpcode(SM_STATS_INFO.class, 0x01, idSet);// 2.6
		addPacketOpcode(SM_CHAT_INIT.class, 0x04, idSet);// 2.6
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x05, idSet);// 2.6
		addPacketOpcode(SM_MACRO_RESULT.class, 0x06, idSet);// 2.6
		addPacketOpcode(SM_MACRO_LIST.class, 0x07, idSet);// 2.6
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x09, idSet);// 2.6
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x52, idSet); // 2.7
		addPacketOpcode(SM_RIFT_STATUS.class, 0x0A, idSet); // 2.7
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x0B, idSet);// 2.6
		addPacketOpcode(SM_ABYSS_RANK.class, 0x0D, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0E, idSet);// 2.6
		addPacketOpcode(SM_PETITION.class, 0x0F, idSet);// 2.6 
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x10, idSet);// 2.6
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x11, idSet);// 2.6
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x12, idSet);// 2.6
		addPacketOpcode(SM_DELETE.class, 0x14, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x15, idSet);// 2.6
		addPacketOpcode(SM_MESSAGE.class, 0x16, idSet);// 2.6
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x17, idSet);// 2.6
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x18, idSet);// 2.6
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x19, idSet);// 2.6
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1A, idSet);// 2.6
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x1B, idSet);// 2.6
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1C, idSet);// 2.6
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x1D, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_INFO.class, 0x1E, idSet);// 2.6
		addPacketOpcode(SM_STANCE_STATE.class, 0x1F, idSet);// 2.6
		addPacketOpcode(SM_GATHER_STATUS.class, 0x20, idSet);// 2.6
		addPacketOpcode(SM_CASTSPELL.class, 0x21, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x22, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x23, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x24, idSet);// 2.6
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x25, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x26, idSet);// 2.6
		addPacketOpcode(SM_DP_INFO.class, 0x27, idSet);// 2.6
		addPacketOpcode(SM_LEGION_TABS.class, 0x2A, idSet);// 2.6
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x2B, idSet);// 2.6
		addPacketOpcode(SM_NPC_INFO.class, 0x2C, idSet);// 2.6
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2D, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2F, idSet);// 2.6
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x31, idSet);// 2.6
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x32, idSet);// 2.6
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x33, idSet);// 2.6
		addPacketOpcode(SM_ATTACK.class, 0x34, idSet);// 2.6
		addPacketOpcode(SM_MOVE.class, 0x37, idSet);// 2.6
		addPacketOpcode(SM_TRANSFORM.class, 0x38, idSet);// 2.6
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3A, idSet);// 2.6
		addPacketOpcode(SM_SELL_ITEM.class, 0x3C, idSet);// 2.6
		addPacketOpcode(SM_WEATHER.class, 0x40, idSet);// 2.6
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x41, idSet);// 2.6
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x42, idSet);// 2.6
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x43, idSet);// 2.6
		addPacketOpcode(SM_GAME_TIME.class, 0x44, idSet);// 2.6
		addPacketOpcode(SM_EMOTION.class, 0x45, idSet);// 2.6
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x46, idSet);// 2.6
		addPacketOpcode(SM_TIME_CHECK.class, 0x47, idSet);// 2.6
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x48, idSet);// 2.6
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x49, idSet);// 2.6
		addPacketOpcode(SM_SKILL_LIST.class, 0x4A, idSet);// 2.6
		addPacketOpcode(SM_CASTSPELL_END.class, 0x4B, idSet);// 2.6
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4C, idSet);// 2.6
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x4D, idSet);// 2.6
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x50, idSet);// 2.6
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x51, idSet);// 2.6
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x53, idSet);// 2.6
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x54, idSet);// 2.6
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x55, idSet);// 2.6
		addPacketOpcode(SM_NAME_CHANGE.class, 0x56, idSet);// 2.6
		addPacketOpcode(SM_GROUP_INFO.class, 0x58, idSet);// 2.6
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x59, idSet);// 2.6
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x5B, idSet);// 2.6
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5E, idSet);// 2.6
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x60, idSet);// 2.6 
		addPacketOpcode(SM_PLAYER_STATE.class, 0x62, idSet);// 2.6
		addPacketOpcode(SM_EMOTION_LIST.class, 0x63, idSet);// 2.6
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x64, idSet);// 2.6
		addPacketOpcode(SM_KEY.class, 0x66, idSet);// 2.6
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x67, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x68, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x69, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x6B, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6C, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6D, idSet);// 2.6
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x71, idSet);// 2.6
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x77, idSet);// 2.6
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x78, idSet);// 2.6
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x79, idSet);// 2.6
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x7A, idSet);// 2.6
		addPacketOpcode(SM_QUEST_LIST.class, 0x7B, idSet);// 2.6
		addPacketOpcode(SM_PING_RESPONSE.class, 0x7E, idSet);// 2.6
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7F, idSet);// 2.6
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x80, idSet);// 2.6
		addPacketOpcode(SM_PET.class, 0x85, idSet);// 2.6
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x86, idSet);// 2.6
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x87, idSet);// 2.6
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x89, idSet);// 2.6
		addPacketOpcode(SM_LEGION_INFO.class, 0x8C, idSet);// 2.6
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8E, idSet);// 2.6
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8F, idSet);// 2.6
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x90, idSet);// 2.6
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x91, idSet);// 2.6
		addPacketOpcode(SM_CMOTION.class, 0x92, idSet);// 2.6
		addPacketOpcode(SM_TRADEINTRADELIST.class, 0x97, idSet); // 2.6
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x98, idSet); // 2.6
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x99, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x9B, idSet);// 2.6
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9C, idSet);// 2.6
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9D, idSet);// 2.6
		addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x9F, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA0, idSet);// 2.6
		addPacketOpcode(SM_MAIL_SERVICE.class, 0xA1, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_LIST.class, 0xA2, idSet);// 2.6
		addPacketOpcode(SM_PRIVATE_STORE.class, 0xA4, idSet); // 2.6
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA6, idSet);// 2.1
		addPacketOpcode(SM_GROUP_LOOT.class, 0xA7, idSet);// 2.1
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA8, idSet);// 2.6
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA9, idSet);// 2.6
		addPacketOpcode(SM_STAGE_STEP_STATUS.class, 0xAA, idSet); // 2.6
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xAB, idSet);// 2.6
		addPacketOpcode(SM_PONG.class, 0xAC, idSet);// 2.6
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0xAD, idSet);// 2.6
		addPacketOpcode(SM_KISK_UPDATE.class, 0xAE, idSet);// 2.6
		addPacketOpcode(SM_BROKER_ITEMS.class, 0xB0, idSet);// 2.6
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xB1, idSet);// 2.6
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB2, idSet);// 2.6
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB4, idSet);// 2.6
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB5, idSet);// 2.6
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB6, idSet);// 2.6
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB7, idSet);// 2.6
		addPacketOpcode(SM_DUEL.class, 0xB9, idSet);// 2.6
		addPacketOpcode(SM_PET_MOVE.class, 0xBB, idSet);// 2.6
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBF, idSet);// 2.6
		addPacketOpcode(SM_RESURRECT.class, 0xC0, idSet);// 2.6
		addPacketOpcode(SM_DIE.class, 0xC1, idSet);// 2.6
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0xC2, idSet);// 2.6
		addPacketOpcode(SM_WINDSTREAM.class, 0xC3, idSet);// 2.6
		addPacketOpcode(SM_FIND_GROUP.class, 0xC4, idSet);// 2.6
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC6, idSet);// 2.6
		addPacketOpcode(SM_REPURCHASE.class, 0xC7, idSet);// 2.6
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC8, idSet);// 2.6
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC9, idSet);// 2.6
		addPacketOpcode(SM_INGAMESHOP.class, 0xCA, idSet);// 2.6
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0xCB, idSet);// 2.6
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0xCC, idSet);// 2.6
		addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0xCD, idSet);// 2.6
		addPacketOpcode(SM_TITLE_LIST.class, 0xCE, idSet);// 2.6
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xD1, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xD3, idSet);// 2.6
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xD4, idSet);// 2.6
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD5, idSet);// 2.6
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD7, idSet);// 2.6
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0xD8, idSet);// 2.6
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDA, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDC, idSet);// 2.6
		addPacketOpcode(SM_BLOCK_LIST.class, 0xDE, idSet);// 2.6
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDF, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE0, idSet);// 2.1
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xE2, idSet);// 2.6
		addPacketOpcode(SM_FORCED_MOVE.class, 0xE3, idSet);// 2.6
		addPacketOpcode(SM_USE_OBJECT.class, 0xE5, idSet);// 2.6
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xE6, idSet);// 2.6
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE7, idSet);// 2.6
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE8, idSet);// 2.6
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE9, idSet);// 2.6
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xEA, idSet);// 2.6
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xEB, idSet);// 2.6
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xEC, idSet);// 2.6
		addPacketOpcode(SM_LOOT_STATUS.class, 0xED, idSet);// 2.6
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEE, idSet);// 2.6
		addPacketOpcode(SM_RECIPE_LIST.class, 0xEF, idSet);// 2.6
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xF1, idSet);// 2.6
		addPacketOpcode(SM_FLY_TIME.class, 0xF2, idSet);// 2.6
		addPacketOpcode(SM_FORTRESS_INFO.class, 0xF3, idSet);// 2.6
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF5, idSet);// 2.6
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF9, idSet);// 2.6
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF7, idSet);// 2.6
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF8, idSet);// 2.6
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF6, idSet);// 2.6
		addPacketOpcode(SM_PRICES.class, 0xFA, idSet);// 2.6
		addPacketOpcode(SM_TRADELIST.class, 0xFD, idSet);// 2.6
		addPacketOpcode(SM_VERSION_CHECK.class, 0xFE, idSet);// 2.6
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFF, idSet);// 2.6
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
	}

	private void initPacketsFor_2_5()
	{
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_STATUPDATE_HP.class, 0x00, idSet); //2.5
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x01, idSet); //2.5
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x02, idSet); //2.5
		addPacketOpcode(SM_MACRO_LIST.class, 0x04, idSet); //2.5
		addPacketOpcode(SM_CHAT_INIT.class, 0x05, idSet); //2.5
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x06, idSet); //2.5
		addPacketOpcode(SM_MACRO_RESULT.class, 0x07, idSet); //2.5
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x08, idSet); //2.5
		addPacketOpcode(SM_ABYSS_RANK.class, 0x0A, idSet); //2.5
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x0B, idSet); //2.5
		addPacketOpcode(SM_PETITION.class, 0x0C, idSet); //2.5
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x0E, idSet); //2.5
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0F, idSet); //2.5
		addPacketOpcode(SM_FORTRESS_INFO.class, 0x10, idSet); //2.5
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x11, idSet); //2.5
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x12, idSet); //2.5
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x13, idSet); //2.5
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x14, idSet); //2.5
		addPacketOpcode(SM_DELETE.class, 0x15, idSet); //2.5
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x16, idSet); //2.5
		addPacketOpcode(SM_MESSAGE.class, 0x17, idSet); //2.5
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x18, idSet); //2.5
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x19, idSet); //2.5
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x1A, idSet); //2.5
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1B, idSet); //2.5
		addPacketOpcode(SM_STANCE_STATE.class, 0x1C, idSet); //2.5 
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1D, idSet); //2.5
		addPacketOpcode(SM_CASTSPELL.class, 0x1E, idSet); //2.5
		addPacketOpcode(SM_PLAYER_INFO.class, 0x1F, idSet); //2.5
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x20, idSet); //2.5
		addPacketOpcode(SM_GATHER_STATUS.class, 0x21, idSet); //2.5
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x22, idSet); //2.5
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x23, idSet); //2.5
		addPacketOpcode(SM_DP_INFO.class, 0x26, idSet); //2.5
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x27, idSet); //2.5
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x28, idSet); //2.5
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2A, idSet); //2.5
		addPacketOpcode(SM_LEGION_TABS.class, 0x2B, idSet); //2.5
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2C, idSet); //2.5
		addPacketOpcode(SM_NPC_INFO.class, 0x2D, idSet); //2.5
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x2E, idSet); //2.5
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x33, idSet); //2.5
		addPacketOpcode(SM_MOVE.class, 0x34, idSet); //2.5
		addPacketOpcode(SM_ATTACK.class, 0x35, idSet); //2.5
		addPacketOpcode(SM_TRANSFORM.class, 0x39, idSet); //2.5
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3B, idSet); //2.5
		addPacketOpcode(SM_SELL_ITEM.class, 0x3D, idSet); //2.5
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x3E, idSet); //2.5
		addPacketOpcode(SM_WEATHER.class, 0x40, idSet); //2.5
		addPacketOpcode(SM_EMOTION.class, 0x42, idSet); //2.5
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x43, idSet); //2.5
		addPacketOpcode(SM_TIME_CHECK.class, 0x44, idSet); //2.5
		addPacketOpcode(SM_GAME_TIME.class, 0x45, idSet); //2.5
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x46, idSet); //2.5
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x47, idSet); //2.5
		addPacketOpcode(SM_CASTSPELL_END.class, 0x48, idSet); //2.5
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x49, idSet); //2.5
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x4A, idSet); //2.5
		addPacketOpcode(SM_SKILL_LIST.class, 0x4B, idSet); //2.5
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4D, idSet); //2.5
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x4E, idSet); //2.5
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x50, idSet); //2.5
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x51, idSet); //2.5
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x52, idSet); //2.5
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x55, idSet); //2.5
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x56, idSet); //2.5
		addPacketOpcode(SM_NAME_CHANGE.class, 0x57, idSet); //2.5
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x58, idSet); //2.5
		addPacketOpcode(SM_GROUP_INFO.class, 0x59, idSet); //2.5
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5F, idSet); //2.5
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x61, idSet); //2.5
		addPacketOpcode(SM_PLAYER_STATE.class, 0x63, idSet); //2.5
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x64, idSet); //2.5
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x65, idSet); //2.5
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x66, idSet); //2.5
		addPacketOpcode(SM_KEY.class, 0x67, idSet); //2.5
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x68, idSet); //2.5
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x69, idSet); //2.5
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6A, idSet); //2.5
		addPacketOpcode(SM_EMOTION_LIST.class, 0x6C, idSet); //2.5
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6D, idSet); //2.5
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x6E, idSet); //2.5
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x70, idSet); //2.5
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x74, idSet); //2.5
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x76, idSet); //2.5
		addPacketOpcode(SM_QUEST_LIST.class, 0x78, idSet); //2.5
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x79, idSet); //2.5
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x7B, idSet); //2.5
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7C, idSet); //2.5
		addPacketOpcode(SM_PING_RESPONSE.class, 0x7F, idSet); //2.5
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x81, idSet); //2.5
		addPacketOpcode(SM_PET.class, 0x82, idSet); //2.5
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x84, idSet); //2.5
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x86, idSet); //2.5
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x87, idSet); //2.5
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8C, idSet); //2.5
		addPacketOpcode(SM_LEGION_INFO.class, 0x8D, idSet); //2.5
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x91, idSet); //2.5
		addPacketOpcode(SM_CMOTION.class, 0x93, idSet); //2.5
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x8E, idSet); //2.5
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8F, idSet); //2.5
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x96, idSet); //2.5
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x98, idSet); //2.5
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x99, idSet); //2.5
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9A, idSet); //2.5
		addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x9C, idSet); //2.5
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9D, idSet); //2.5
		addPacketOpcode(SM_MAIL_SERVICE.class, 0x9E, idSet); //2.5
		addPacketOpcode(SM_WINDSTREAM.class, 0xA0, idSet); //2.5
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA1, idSet); //2.5
		addPacketOpcode(SM_PRIVATE_STORE.class, 0xA5, idSet); //2.5
		addPacketOpcode(SM_FRIEND_LIST.class, 0xA3, idSet); //2.5
		addPacketOpcode(SM_GROUP_LOOT.class, 0xA4, idSet); //2.5
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA6, idSet); //2.5
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA7, idSet); //2.5
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xA8, idSet); //2.5
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA9, idSet); //2.5
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0xAA, idSet); //2.5
		addPacketOpcode(SM_RIFT_STATUS.class, 0xAC, idSet); //2.5
		addPacketOpcode(SM_STAGE_STEP_STATUS.class, 0xAB, idSet); //2.5
		addPacketOpcode(SM_PONG.class, 0xAD, idSet); //2.5
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xAE, idSet); //2.5
		addPacketOpcode(SM_KISK_UPDATE.class, 0xAF, idSet); //2.5
		addPacketOpcode(SM_BROKER_ITEMS.class, 0xB1, idSet); //2.5
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB2, idSet); //2.5
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB3, idSet); //2.5
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB4, idSet); //2.5
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB5, idSet); //2.5
		addPacketOpcode(SM_DUEL.class, 0xB6, idSet); //2.5
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB7, idSet); //2.5
		addPacketOpcode(SM_PET_MOVE.class, 0xB8, idSet); //2.5
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBC, idSet); //2.5
		addPacketOpcode(SM_DIE.class, 0xBE, idSet); //2.5
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class,0xBF, idSet); //2.5
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC0, idSet); //2.5
		addPacketOpcode(SM_RESURRECT.class, 0xC1, idSet); //2.5
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0xC3, idSet); //2.5
		addPacketOpcode(SM_REPURCHASE.class, 0xC4, idSet); //2.5
		addPacketOpcode(SM_FIND_GROUP.class, 0xC5, idSet); //2.5
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC6, idSet); //2.5
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC7, idSet); //2.5
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0xC8, idSet); //2.5
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC9, idSet); //2.5
		addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0xCA, idSet); //2.5
		addPacketOpcode(SM_INGAMESHOP.class, 0xCB, idSet); //2.5
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0xCD, idSet); //2.5
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xCE, idSet); //2.5
		addPacketOpcode(SM_TITLE_LIST.class, 0xCF, idSet); //2.5
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD2, idSet); //2.5
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD4, idSet); //2.5
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xD5, idSet); //2.5
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0xD9, idSet); //2.5
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDB, idSet); //2.5
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDD, idSet); //2.5
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDE, idSet); //2.5
		addPacketOpcode(SM_BLOCK_LIST.class, 0xDF, idSet); //2.5
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE0, idSet); //2.5
		addPacketOpcode(SM_USE_OBJECT.class, 0xE2, idSet); //2.5
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xE3, idSet); //2.5
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE4, idSet); //2.5
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE6, idSet); //2.5
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xE7, idSet); //2.5
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xE8, idSet); //2.5
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE9, idSet); //2.5
		addPacketOpcode(SM_LOOT_STATUS.class, 0xEA, idSet); //2.5
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xEB, idSet); //2.5
		addPacketOpcode(SM_RECIPE_LIST.class, 0xEC, idSet); //2.5
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xED, idSet); //2.5
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xEE, idSet); //2.5
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEF, idSet); //2.5
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xF0, idSet); //2.5
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF2, idSet); //2.5
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF5, idSet); //2.5
		addPacketOpcode(SM_FLY_TIME.class, 0xF3, idSet); //2.5
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF4, idSet); //2.5
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF6, idSet); //2.5
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF9, idSet); //2.5
		addPacketOpcode(SM_TRADELIST.class, 0xFA, idSet); //2.5
		addPacketOpcode(SM_PRICES.class, 0xFB, idSet); //2.5
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFC, idSet); //2.5
		addPacketOpcode(SM_STATS_INFO.class, 0xFE, idSet); //2.5
		addPacketOpcode(SM_VERSION_CHECK.class, 0xFF, idSet); //2.5
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet); //2.5
	}
}