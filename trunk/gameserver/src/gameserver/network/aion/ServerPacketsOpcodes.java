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
package gameserver.network.aion;

import gameserver.network.aion.serverpackets.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerPacketsOpcodes {
	private static ServerPacketsOpcodes serverPackets = new ServerPacketsOpcodes();

	protected Map<Class<? extends AionServerPacket>, Integer> opcodes = new HashMap<Class<? extends AionServerPacket>, Integer>();

	private ServerPacketsOpcodes() {
		Set<Integer> idSet = new HashSet<Integer>();
		// addPacketOpcode(SM_STATUPDATE_DP.class, 0x01, idSet);
		addPacketOpcode(SM_STATS_INFO.class, 0x01, idSet);// 2.6
		addPacketOpcode(SM_CHAT_INIT.class, 0x04, idSet);// 2.6
		// addPacketOpcode(SM_MACRO_RESULT.class, 0x05, idSet);
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x05, idSet);// 2.6
		addPacketOpcode(SM_MACRO_LIST.class, 0x07, idSet);// 2.6
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x09, idSet);// 2.6
		// addPacketOpcode(SM_ABYSS_RANK.class, 0x0A, idSet);
		// addPacketOpcode(SM_RIFT_ANNOUNCE.class,0x0B, idSet);
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x0B, idSet);// 2.6
		// addPacketOpcode(SM_PETITION.class, 0x0C, idSet);
		// addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0F, idSet);
		// addPacketOpcode(SM_FORTRESS_INFO.class, 0x10, idSet);
		// addPacketOpcode(SM_RECIPE_DELETE.class,0x11, idSet);
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x11, idSet);// 2.6
		// addPacketOpcode(SM_PLAYER_MOVE.class, 0x12, idSet);
		// addPacketOpcode(SM_TELEPORT_LOC.class, 0x13, idSet);
		// addPacketOpcode(SM_LOGIN_QUEUE.class, 0x14, idSet);
		addPacketOpcode(SM_DELETE.class, 0x14, idSet);// 2.6
		addPacketOpcode(SM_MESSAGE.class, 0x16, idSet);// 2.6
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x18, idSet);// 2.6
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x19, idSet);// 2.6
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1A, idSet);// 2.6
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x1B, idSet);// 2.6
		// addPacketOpcode(SM_STANCE_STATE.class, 0x1C, idSet);
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1C, idSet);// 2.6
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x1D, idSet); // 2.6
		addPacketOpcode(SM_PLAYER_INFO.class, 0x1E, idSet);// 2.6
		addPacketOpcode(SM_GATHER_STATUS.class, 0x20, idSet);// 2.6
		addPacketOpcode(SM_CASTSPELL.class, 0x21, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x22, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x23, idSet);// 2.6
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x25, idSet);// 2.6
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x26, idSet);// 2.6
		addPacketOpcode(SM_DP_INFO.class, 0x27, idSet);// 2.6
		addPacketOpcode(SM_LEGION_TABS.class, 0x2A, idSet);// 2.6
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x2B, idSet);// 2.6
		addPacketOpcode(SM_NPC_INFO.class, 0x2C, idSet);// 2.6
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2D, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2F, idSet);// 2.6
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x31, idSet);// 2.6
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x32, idSet);// 2.7
		addPacketOpcode(SM_ATTACK.class, 0x34, idSet);// 2.6
		addPacketOpcode(SM_MOVE.class, 0x37, idSet);// 2.6
		// addPacketOpcode(SM_UNKNOW.class,0x36, idSet); Opcode on retail but
		// dunno what is for
		// addPacketOpcode(SM_TRANSFORM.class, 0x39, idSet);
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3A, idSet);// 2.6
		addPacketOpcode(SM_SELL_ITEM.class, 0x3C, idSet);// 2.6
		addPacketOpcode(SM_WEATHER.class, 0x39, idSet);// 2.6
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x41, idSet);// 2.6
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x42, idSet);// 2.6
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x43, idSet);// 2.6
		addPacketOpcode(SM_GAME_TIME.class, 0x44, idSet);// 2.6
		addPacketOpcode(SM_EMOTION.class, 0x45, idSet);// 2.6
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x46, idSet);// 2.6
		addPacketOpcode(SM_TIME_CHECK.class, 0x47, idSet);// 2.6
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x48, idSet);// 2.6
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x49, idSet);// 2.6
		// addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class,0x4A, idSet);
		addPacketOpcode(SM_SKILL_LIST.class, 0x4A, idSet);// 2.6
		addPacketOpcode(SM_CASTSPELL_END.class, 0x4B, idSet);// 2.6
		// addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4D, idSet);
		// addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x50, idSet);
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x51, idSet);// 2.6
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x52, idSet);// 2.7
		// addPacketOpcode(SM_FORTRESS_STATUS.class,0x55, idSet);
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x55, idSet);// 2.6
		// addPacketOpcode(SM_NAME_CHANGE.class, 0x57, idSet);
		addPacketOpcode(SM_GROUP_INFO.class, 0x58, idSet);// 2.6
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x59, idSet);// 2.6
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x5B, idSet);// 2.6
		// addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5F, idSet);
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x61, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_STATE.class, 0x62, idSet);// 2.6
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x64, idSet);// 2.6
		addPacketOpcode(SM_KEY.class, 0x66, idSet);// 2.7
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x67, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x68, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x69, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x6B, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6C, idSet);// 2.6
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6D, idSet);// 2.6
		addPacketOpcode(SM_EMOTION_LIST.class, 0x6F, idSet);// 2.6
		// addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x70, idSet);
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x71, idSet);// 2.6
		// addPacketOpcode(SM_INSTANCE_SCORE.class, 0x76, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x77, idSet);// 2.6
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x78, idSet);// 2.6
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
		// addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class,0x8E, idSet);
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8E, idSet);// 2.6
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8F, idSet);// 2.6
		// addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x91, idSet);
		addPacketOpcode(SM_MOTION.class, 0x92, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x98, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x99, idSet);// 2.6
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x9B, idSet);// 2.6
		// addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x9C, idSet);
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9C, idSet);// 2.6
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9D, idSet);// 2.6
		// addPacketOpcode(SM_WINDSTREAM.class, 0xA0, idSet);
		// addPacketOpcode(SM_SUMMON_USESKILL.class,0xA1, idSet);
		addPacketOpcode(SM_MAIL_SERVICE.class, 0xA1, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_LIST.class, 0xA2, idSet);// 2.6
		// addPacketOpcode(SM_GROUP_LOOT.class, 0xA4, idSet);
		// addPacketOpcode(SM_PRIVATE_STORE.class, 0xA5, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA6, idSet);// 2.6
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA8, idSet);// 2.6
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA9, idSet);// 2.6
		// addPacketOpcode(SM_ACADEMY_BOOTCAMP_STAGE.class,0xAB, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xAB, idSet);// 2.6
		// addPacketOpcode(SM_RIFT_STATUS.class,0xAC, idSet);
		addPacketOpcode(SM_PONG.class, 0xAC, idSet);// 2.6
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0xAD, idSet);// 2.6
		// addPacketOpcode(SM_KISK_UPDATE.class, 0xAF, idSet);
		addPacketOpcode(SM_BROKER_ITEMS.class, 0xB0, idSet);// 2.6
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xB1, idSet);// 2.6
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB2, idSet);// 2.6
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB4, idSet);// 2.6
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB5, idSet);// 2.6
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB6, idSet);// 2.6
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB7, idSet);// 2.6
		addPacketOpcode(SM_DUEL.class, 0xB9, idSet);// 2.6
		addPacketOpcode(SM_PET_MOVE.class, 0xBB, idSet);// 2.6
		// addPacketOpcode(SM_LEGION_EMBLEM_SEND.class,0xBF, idSet);// Testing
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBF, idSet);// 2.7
		// addPacketOpcode(SM_FORCED_MOVE.class, 0xC0, idSet);
		// addPacketOpcode(SM_RESURRECT.class, 0xC1, idSet);
		addPacketOpcode(SM_DIE.class, 0xC1, idSet);// 2.6
		// addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0xC3, idSet);
		addPacketOpcode(SM_FIND_GROUP.class, 0xC4, idSet);// 2.6
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC6, idSet);// 2.6
		addPacketOpcode(SM_REPURCHASE.class, 0xC7, idSet);// 2.6
		// addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class,0xC8, idSet);
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC8, idSet);// 2.6
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC9, idSet);// 2.6
		// addPacketOpcode(SM_INGAMESHOP_ITEM_LIST.class, 0xCA, idSet);
		// addPacketOpcode(SM_INGAMESHOP.class, 0xCB, idSet);
		// addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0xCD, idSet);
		addPacketOpcode(SM_TITLE_LIST.class, 0xCE, idSet);// 2.6
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xD1, idSet);// 2.6
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xD3, idSet);// 2.6
		// addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD4, idSet);
		// addPacketOpcode(SM_LEGION_EMBLEM_SEND.class,0xD5, idSet);// Testing
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD5, idSet);// 2.6
		// addPacketOpcode(SM_ABYSS_ARTIFACT_INFO2.class,0xD9, idSet); - renamed
		// to the below one.
		// addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0xD9, idSet);
		// addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDB, idSet);
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDC, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE1, idSet);// 2.6
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xE2, idSet);// 2.6
		addPacketOpcode(SM_NPC_WEARING_OBJECT.class, 0xE4, idSet);// 2.6
		addPacketOpcode(SM_USE_OBJECT.class, 0xE5, idSet);// 2.6
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xE6, idSet);// 2.6
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE7, idSet);// 2.6
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE8, idSet);// 2.6
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE9, idSet);// 2.6
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class,0xEA, idSet);// 2.7
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xEB, idSet);// 2.6
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xEC, idSet);// 2.6
		addPacketOpcode(SM_LOOT_STATUS.class, 0xED, idSet);// 2.6
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEE, idSet);// 2.6
		addPacketOpcode(SM_RECIPE_LIST.class, 0xEF, idSet);// 2.6
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xF1, idSet);// 2.6
		// addPacketOpcode(SM_ALLIANCE_INFO.class,0xF2, idSet);
		addPacketOpcode(SM_FLY_TIME.class, 0xF2, idSet);// 2.6
		// addPacketOpcode(SM_GROUP_MEMBER_LEAVE.class, 0xF4, idSet);
		// addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF5, idSet);//
		// testing
		// addPacketOpcode(SM_SHOW_BRAND.class, 0xF6, idSet);
		// addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF9, idSet);
		addPacketOpcode(SM_PRICES.class, 0xFA, idSet);// 2.6
		addPacketOpcode(SM_TRADELIST.class, 0xFD, idSet);// 2.6
		addPacketOpcode(SM_VERSION_CHECK.class, 0xFE, idSet);// 2.6
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFF, idSet);// 2.6
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
	}

	static int getOpcode(Class<? extends AionServerPacket> packetClass) {
		return serverPackets.getOpcodeForPacket(packetClass);
	}

	private int getOpcodeForPacket(Class<? extends AionServerPacket> packetClass) {
		Integer opcode = opcodes.get(packetClass);
		if (opcode == null)
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");

		return opcode;
	}

	private void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet) {
		if (opcode < 0)
			return;

		if (idSet.contains(opcode)) {
			throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X", opcode));
		}

		idSet.add(opcode);
		opcodes.put(packetClass, opcode);
	}
}