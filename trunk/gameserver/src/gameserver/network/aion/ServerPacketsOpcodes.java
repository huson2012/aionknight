/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
		if(GSConfig.SERVER_VERSION.startsWith("1.9"))
			initPacketsFor_1_9();
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

		addPacketOpcode(SM_STATS_INFO.class, 0x0001, idSet);// 2.6 0x01, 2.7
		addPacketOpcode(SM_CHAT_INIT.class, 0x0104, idSet);// 2.6 0x04, 2.7
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x0105, idSet);// 2.6 0x05, 2.7
		addPacketOpcode(SM_MACRO_RESULT.class, 0x0106, idSet);// 2.6 0x06, 2.7
		addPacketOpcode(SM_MACRO_LIST.class, 0x0107, idSet);// 2.6 0x07, 2.7
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x0109, idSet);// 2.6 0x09, 2.7
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x0A, idSet);// 2.1
		addPacketOpcode(SM_RIFT_STATUS.class, 0x010A, idSet);// 2.7
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x010B, idSet);// 2.6 0x0B, 2.7
		addPacketOpcode(SM_ABYSS_RANK.class, 0x010D, idSet);// 2.6 0x0D, 2.7
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x000E, idSet);// 2.6 0x0E, 2.7
		addPacketOpcode(SM_PETITION.class, 0x000F, idSet);// 2.6 0x0F, (Disabled in 2.7 Retail?)
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x0110, idSet);// 2.6 0x10, 2.7
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x0111, idSet);// 2.6 0x11, 2.7
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x0012, idSet);// 2.6 0x12, 2.7
		addPacketOpcode(SM_DELETE.class, 0x0014, idSet);// 2.6 0x14, 2.7
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x0015, idSet);// 2.6 0x11, 2.7 Testing
		addPacketOpcode(SM_MESSAGE.class, 0x0016, idSet);// 2.6 0x16, 2.7
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x0017, idSet);// 2.6 0x17, 2.7 Testing
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x0018, idSet);// 2.6 0x18, 2.7
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x0019, idSet);// 2.6 0x19, 2.7
		addPacketOpcode(SM_DELETE_ITEM.class, 0x001A, idSet);// 2.6 0x1A, 2.7
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x001B, idSet);// 2.6 0x1B, 2.7
		addPacketOpcode(SM_UI_SETTINGS.class, 0x001C, idSet);// 2.6 0x1C, 2.7
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x001D, idSet);// 2.6 0x1D, 2.7
		addPacketOpcode(SM_PLAYER_INFO.class, 0x001E, idSet);// 2.6 0x1E, 2.7
		addPacketOpcode(SM_STANCE_STATE.class, 0x001F, idSet);// 2.6 0x1F, 2.7
		addPacketOpcode(SM_GATHER_STATUS.class, 0x0020, idSet);// 2.6 0x20, 2.7
		addPacketOpcode(SM_CASTSPELL.class, 0x0021, idSet);// 2.6 0x21, 2.7
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x0022, idSet);// 2.6 0x22, 2.7
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x0023, idSet);// 2.6 0x23, 2.7
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x0024, idSet);// 2.6 0x24, 2.7
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x0025, idSet);// 2.6 0x25, 2.7
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x0026, idSet);// 2.6 0x26, 2.7
		addPacketOpcode(SM_DP_INFO.class, 0x0027, idSet);// 2.6 0x27, 2.7
		addPacketOpcode(SM_LEGION_TABS.class, 0x002A, idSet);// 2.6 0x2A, 2.7
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x012B, idSet);// 2.6 0x2B, 2.7 Testing
		addPacketOpcode(SM_NPC_INFO.class, 0x002C, idSet);// 2.6 0x2C, 2.7
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x002D, idSet);// 2.6 0x2D, 2.7
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x002F, idSet);// 2.6 0x2F, 2.7
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x0031, idSet);// 2.6 0x31, 2.7
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x0032, idSet);// 2.6 0x32, 2.7
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x0033, idSet);// 2.6 0x33, 2.7
		addPacketOpcode(SM_ATTACK.class, 0x0034, idSet);// 2.6 0x34, 2.7
		addPacketOpcode(SM_MOVE.class, 0x0037, idSet);// 2.6 0x37, 2.7
		addPacketOpcode(SM_TRANSFORM.class, 0x0038, idSet);// 2.6 0x38, 2.7
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x003A, idSet);// 2.6 0x3A, 2.7
		addPacketOpcode(SM_SELL_ITEM.class, 0x003C, idSet);// 2.6 0x41, 2.7
		addPacketOpcode(SM_WEATHER.class, 0x0040, idSet);// 2.6 0x40, 2.7
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x0041, idSet);// 2.6 0x41, 2.7
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x0042, idSet);// 2.6 0x42, 2.7
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x0043, idSet);// 2.6 0x43, 2.7
		addPacketOpcode(SM_GAME_TIME.class, 0x0044, idSet);// 2.6 0x44, 2.7
		addPacketOpcode(SM_EMOTION.class, 0x0045, idSet);// 2.6 0x45, 2.7
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x0046, idSet);// 2.6 0x46, 2.7
		addPacketOpcode(SM_TIME_CHECK.class, 0x0047, idSet);// 2.6 0x47, 2.7
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x0048, idSet);// 2.60x48, 2.7
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x0049, idSet);// 2.6 0x49, 2.7
		addPacketOpcode(SM_SKILL_LIST.class, 0x004A, idSet);// 2.6 0x4A, 2.7
		addPacketOpcode(SM_CASTSPELL_END.class, 0x004B, idSet);// 2.6 0x4B, 2.7
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x004C, idSet);// 2.6 0x4C, 2.7
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x004D, idSet);// 2.6 0x4D, 2.7
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x0050, idSet);// 2.6 0x50, 2.7
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x0051, idSet);// 2.6 0x51, 2.7
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x0153, idSet);// 2.6 0x53, 2.7
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x0154, idSet);// 2.6 0x54, 2.7
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x0155, idSet);// 2.6 0x55, 2.7
		addPacketOpcode(SM_NAME_CHANGE.class, 0x0156, idSet);// 2.6 0x56, 2.7 Testing
		addPacketOpcode(SM_GROUP_INFO.class, 0x0158, idSet);// 2.6 0x58, 2.7 Testing
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x0159, idSet);// 2.6 0x59, 2.7
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x015B, idSet);// 2.6 0x5B, 2.7
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x015E, idSet);// 2.6 0x5E, 2.7 Testing
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x0160, idSet);// 2.6 0x60, 2.7
		addPacketOpcode(SM_PLAYER_STATE.class, 0x0162, idSet);// 2.6 0x62, 2.7
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x0164, idSet);// 2.6 0x64, 2.7
		addPacketOpcode(SM_KEY.class, 0x0166, idSet);// 2.6 0x66, 2.7
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x0167, idSet);// 2.6 0x67, 2.7
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x0168, idSet);// 2.6 0x68, 2.7
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x0169, idSet);// 2.6 0x69, 2.7
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x016B, idSet);// 2.6 0x6B, 2.7
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x016C, idSet);// 2.6 0x6C, 2.7
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x016D, idSet);// 2.6 0x6D, 2.7
		addPacketOpcode(SM_EMOTION_LIST.class, 0x016F, idSet);// 2.6 0x63, 2.7
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x0171, idSet);// 2.6 0x71, 2.7
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x0177, idSet);// 2.6 0x77, 2.7
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x0178, idSet);// 2.6 0x78, 2.7
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x0179, idSet);// 2.1 0x76, 2.7 Testing
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x017A, idSet);// 2.6 0x7A, 2.7
		addPacketOpcode(SM_QUEST_LIST.class, 0x017B, idSet);// 2.6 0x7B, 2.7
		addPacketOpcode(SM_PING_RESPONSE.class, 0x017E, idSet);// 2.6 0x7E, 2.7
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x017F, idSet);// 2.6 0x7F, 2.7
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x0180, idSet);// 2.6 0x80, 2.7
		addPacketOpcode(SM_PET.class, 0x0185, idSet);// 2.6 0x85, 2.7
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x0186, idSet);// 2.6 0x86, 2.7
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x0187, idSet);// 2.6 0x87, 2.7
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x0189, idSet);// 2.6 0x89, 2.7
		addPacketOpcode(SM_LEGION_INFO.class, 0x018C, idSet);// 2.6 0x8C, 2.7
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x018E, idSet);// 2.6 0x8E, 2.7 Testing
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x018F, idSet);// 2.6 0x8F, 2.7
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x0190, idSet);// 2.6 0x90, 2.7
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x0191, idSet);// 2.6 0x91, 2.7
		addPacketOpcode(SM_CMOTION.class, 0x0192, idSet);// 2.6 0x92, 2.7
		//addPacketOpcode(SM_NPC_TRADE.class,0x0197, idSet); // 2.7
		addPacketOpcode(SM_TRADEINTRADELIST.class, 0x0197, idSet); // 2.7
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x0198, idSet); // 2.6 0x98, 2.7
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x0199, idSet);// 2.6 0x99, 2.7
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x019B, idSet);// 2.6 0x9B, 2.7
		addPacketOpcode(SM_LEGION_EDIT.class, 0x019C, idSet);// 2.6 0x9C, 2.7
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x019D, idSet);// 2.6 0x9D, 2.7
		addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x019F, idSet);// 2.6 0x9F, 2.7
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0x01A0, idSet);// 2.6 0xA0, 2.7
		addPacketOpcode(SM_MAIL_SERVICE.class, 0x01A1, idSet);// 2.6 0xA1, 2.7
		addPacketOpcode(SM_FRIEND_LIST.class, 0x01A2, idSet);// 2.6 0xA2, 2.7
		addPacketOpcode(SM_PRIVATE_STORE.class, 0x01A4, idSet); // 2.6 0xA4, 2.7
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA6, idSet);// 2.1
		addPacketOpcode(SM_GROUP_LOOT.class, 0xA7, idSet);// 2.1
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0x01A8, idSet);// 2.6 0xA8, 2.7
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0x01A9, idSet);// 2.6 0xA9, 2.7
		addPacketOpcode(SM_STAGE_STEP_STATUS.class, 0x01AA, idSet); // 2.7 - NEW!
		addPacketOpcode(SM_ACADEMY_BOOTCAMP_STAGE.class, 0xAB, idSet); // 2.5
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0x01AB, idSet);// 2.6 0xAB, 2.7
		addPacketOpcode(SM_PONG.class, 0x01AC, idSet);// 2.6 0xAC, 2.7
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0x01AD, idSet);// 2.6 0xAD, 2.7
		addPacketOpcode(SM_KISK_UPDATE.class, 0x01AE, idSet);// 2.6 0xAE, 2.7
		addPacketOpcode(SM_BROKER_ITEMS.class, 0x01B0, idSet);// 2.6 0xB0, 2.7
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0x01B1, idSet);// 2.6 0xB1, 2.7
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0x01B2, idSet);// 2.6 0xB2, 2.7
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0x01B4, idSet);// 2.6 0xB4, 2.7 Testing
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0x01B5, idSet);// 2.6 0xB5, 2.7
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0x01B6, idSet);// 2.6 0xB6, 2.7
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0x01B7, idSet);// 2.6 0xB7, 2.7
		addPacketOpcode(SM_DUEL.class, 0x01B9, idSet);// 2.6 0xB9, 2.7
		addPacketOpcode(SM_PET_MOVE.class, 0x01BB, idSet);// 2.6 0xBB, 2.7
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0x01BF, idSet);// 2.6 0xBF, 2.7
		addPacketOpcode(SM_RESURRECT.class, 0x01C0, idSet);// 2.6 0xC0, 2.7
		addPacketOpcode(SM_DIE.class, 0x01C1, idSet);// 2.6 0xC1, 2.7
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0x01C2, idSet);// 2.6 0xC2, 2.7
		addPacketOpcode(SM_WINDSTREAM.class, 0x01C3, idSet);// 2.6 0xC3, 2.7
		addPacketOpcode(SM_FIND_GROUP.class, 0x01C4, idSet);// 2.6 0xC4, 2.7
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0x01C6, idSet);// 2.6 0xC6, 2.7
		addPacketOpcode(SM_REPURCHASE.class, 0x01C7, idSet);// 2.6 0xC7, 2.7
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0x01C8, idSet);// 2.6 0xC8, 2.7
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0x01C9, idSet);// 2.6 0xC9, 2.7
		addPacketOpcode(SM_INGAMESHOP.class, 0x01CA, idSet);// 2.6 0xCA, 2.7
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0x01CB, idSet);// 2.6 0xCB, 2.7
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0x01CC, idSet);// 2.6 Testing 0xCC, 2.7
		addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0x01CD, idSet);// 2.6 0xCD, 2.7
		addPacketOpcode(SM_TITLE_LIST.class, 0x01CE, idSet);// 2.6 0xCE, 2.7
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0x01D1, idSet);// 2.6 0xD1, 2.7
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0x01D3, idSet);// 2.6 0xD3, 2.7
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xBF, idSet);// 2.6 0xD4, 2.7 Testing
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0x01D5, idSet);// 2.6 0xD5, 2.7
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0x01D7, idSet);// 2.6 0xD7, 2.7 Testing
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0x01D8, idSet);// 2.6 0xD8, 2.7 Testing
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0x01DA, idSet);// 2.6 0xDA, 2.7 Testing
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0x00DC, idSet);// 2.6
		addPacketOpcode(SM_BLOCK_LIST.class, 0x01DE, idSet);// 2.6 0xDE, 2.7
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0x00DF, idSet);// 2.6
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0x00E0, idSet);// 2.1
		addPacketOpcode(SM_TELEPORT_MAP.class, 0x01E2, idSet);// 2.6 0xE2, 2.7
		addPacketOpcode(SM_FORCED_MOVE.class, 0x01E3, idSet);// 2.6 0xE3, 2.7
		addPacketOpcode(SM_USE_OBJECT.class, 0x01E5, idSet);// 2.6 0xE5, 2.7
		addPacketOpcode(SM_CHARACTER_LIST.class, 0x01E6, idSet);// 2.6 0xE6, 2.7
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0x01E7, idSet);// 2.6 0xE7, 2.7
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0x01E8, idSet);// 2.6 0xE8, 2.7
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0x01E9, idSet);// 2.6 0xE9, 2.7
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0x01EA, idSet);// 2.6 0xEA, 2.7 Testing
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0x01EB, idSet);// 2.6 0xEB, 2.7
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0x01EC, idSet);// 2.6 0xEC, 2.7
		addPacketOpcode(SM_LOOT_STATUS.class, 0x01ED, idSet);// 2.6 0xED, 2.7
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0x01EE, idSet);// 2.6 0xEE, 2.7
		addPacketOpcode(SM_RECIPE_LIST.class, 0x01EF, idSet);// 2.6 0xEF, 2.7
		// 2.7 Unknown - F0 01 54 8F FE 6B 04 00 00 02 00 00 00 00   
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0x01F1, idSet);// 2.6 0xF1, 2.7
		addPacketOpcode(SM_FLY_TIME.class, 0x01F2, idSet);// 2.6 0xF2, 2.7
		addPacketOpcode(SM_FORTRESS_INFO.class, 0x01F3, idSet);// 2.6 0xF3, 2.7
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0x01F4, idSet);// 2.6 0xF5, 2.7
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0x01F5, idSet);// 2.6 0xF9, 2.7
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0x01F7, idSet);// 2.6 0xF7, 2.7
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0x01F8, idSet);// 2.6 0xF8, 2.7 Testing
		addPacketOpcode(SM_SHOW_BRAND.class, 0x01F9, idSet);// 2.6 0xF6, 2.7
		addPacketOpcode(SM_PRICES.class, 0x01FA, idSet);// 2.6 0xFA, 2.7
		// 2.7 Unknown - FC 01 54 BB FE 3C 00 00 00  (Directly After CM_LEVEL_READY)
		addPacketOpcode(SM_TRADELIST.class, 0x01FD, idSet);// 2.6 0xFD, 2.7
		addPacketOpcode(SM_VERSION_CHECK.class, 0x00FE, idSet);// 2.6 0xFE, 2.7
		addPacketOpcode(SM_RECONNECT_KEY.class, 0x01FF, idSet);// 2.6 0xFF, 2.7
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
	}

	private void initPacketsFor_2_6()
	{
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_STATUPDATE_HP.class, 0x00, idSet);
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x01, idSet);
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x02, idSet);
		addPacketOpcode(SM_MACRO_LIST.class, 0x04, idSet);
		addPacketOpcode(SM_CHAT_INIT.class, 0x05, idSet);
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x06, idSet);
		addPacketOpcode(SM_MACRO_RESULT.class, 0x07, idSet);
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x08, idSet);
		addPacketOpcode(SM_ABYSS_RANK.class, 0x0A, idSet);
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x0B, idSet);
		addPacketOpcode(SM_PETITION.class, 0x0C, idSet);
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x0E, idSet);
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0F, idSet);
		addPacketOpcode(SM_FORTRESS_INFO.class, 0x10, idSet);
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x11, idSet);
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x12, idSet);
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x13, idSet);
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x14, idSet);
		addPacketOpcode(SM_DELETE.class, 0x15, idSet);
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x16, idSet);
		addPacketOpcode(SM_MESSAGE.class, 0x17, idSet);
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x18, idSet);
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x19, idSet);
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x1A, idSet);
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1B, idSet);
		addPacketOpcode(SM_STANCE_STATE.class, 0x1C, idSet); // test
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1D, idSet);
		addPacketOpcode(SM_CASTSPELL.class, 0x1E, idSet);
		addPacketOpcode(SM_PLAYER_INFO.class, 0x1F, idSet);
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x43, idSet); // 2.6
		addPacketOpcode(SM_GATHER_STATUS.class, 0x21, idSet);
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x22, idSet);
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x23, idSet);
		addPacketOpcode(SM_DP_INFO.class, 0x26, idSet);
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x27, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x28, idSet);
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2A, idSet);
		addPacketOpcode(SM_LEGION_TABS.class, 0x2B, idSet);
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2C, idSet);
		addPacketOpcode(SM_NPC_INFO.class, 0x2D, idSet);
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x2E, idSet);
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x32, idSet); //2.7
		addPacketOpcode(SM_MOVE.class, 0x34, idSet);
		addPacketOpcode(SM_ATTACK.class, 0x35, idSet);
		//addPacketOpcode(SM_UNKNOW.class,0x36, idSet); Opcode on retail but dunno what is for
		addPacketOpcode(SM_TRANSFORM.class, 0x39, idSet);
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3A, idSet); //2.6
		addPacketOpcode(SM_SELL_ITEM.class, 0x3C, idSet); //2.6
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x41, idSet); //2.6
		addPacketOpcode(SM_WEATHER.class, 0x40, idSet);
		addPacketOpcode(SM_EMOTION.class, 0x45, idSet); //2.6
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x42, idSet); //2.6
		addPacketOpcode(SM_TIME_CHECK.class, 0x47, idSet); // 2.6
		addPacketOpcode(SM_GAME_TIME.class, 0x44, idSet); //2.6
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x49, idSet); //2.6
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x46, idSet); //2.6
		addPacketOpcode(SM_CASTSPELL_END.class, 0x4B, idSet); //2.6
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x48, idSet); //2.6
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x4A, idSet);
		addPacketOpcode(SM_SKILL_LIST.class, 0x4A, idSet); //2.6
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4D, idSet);
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x4E, idSet);
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x50, idSet);
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x51, idSet);
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x52, idSet);
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x55, idSet);
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x59, idSet); //2.6
		addPacketOpcode(SM_NAME_CHANGE.class, 0x57, idSet);
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x5B, idSet); //2.6
		addPacketOpcode(SM_GROUP_INFO.class, 0x58, idSet); //2.6
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5F, idSet);
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x61, idSet);
		addPacketOpcode(SM_PLAYER_STATE.class, 0x63, idSet);
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x64, idSet);
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x65, idSet);
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x67, idSet); //2.6
		addPacketOpcode(SM_KEY.class, 0x66, idSet); //2.6
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x68, idSet);
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x69, idSet);
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6A, idSet);
		addPacketOpcode(SM_EMOTION_LIST.class, 0x6C, idSet);
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6D, idSet);
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x6E, idSet);
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x70, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x74, idSet);
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x76, idSet);
		addPacketOpcode(SM_QUEST_LIST.class, 0x7B, idSet); //2.6
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x79, idSet);
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x7A, idSet); //2.6
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7C, idSet);
		addPacketOpcode(SM_PING_RESPONSE.class, 0x7F, idSet);
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x81, idSet);
		addPacketOpcode(SM_PET.class, 0x82, idSet);
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x84, idSet);
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x86, idSet);
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x87, idSet);
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8C, idSet);
		addPacketOpcode(SM_LEGION_INFO.class, 0x8D, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x91, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x8E, idSet);
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8F, idSet);
		addPacketOpcode(SM_CMOTION.class, 0x93, idSet);
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x96, idSet);
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x98, idSet);
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x99, idSet);
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9A, idSet);
		addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x9C, idSet);
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9D, idSet);
		addPacketOpcode(SM_MAIL_SERVICE.class, 0x9E, idSet);
		addPacketOpcode(SM_WINDSTREAM.class, 0xA0, idSet);
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA1, idSet);
		addPacketOpcode(SM_PRIVATE_STORE.class, 0xA5, idSet);
		addPacketOpcode(SM_FRIEND_LIST.class, 0xA3, idSet);
		addPacketOpcode(SM_GROUP_LOOT.class, 0xA4, idSet);
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA9, idSet); //2.6
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA7, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xA8, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA6, idSet); //2.6
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0xAA, idSet);
		addPacketOpcode(SM_RIFT_STATUS.class, 0xAC, idSet);// was AB
		addPacketOpcode(SM_PONG.class, 0xAC, idSet); // 2.6
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xAE, idSet);
		addPacketOpcode(SM_KISK_UPDATE.class, 0xAF, idSet);
		addPacketOpcode(SM_BROKER_ITEMS.class, 0xB1, idSet);
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB2, idSet);// was CD
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB3, idSet);
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB4, idSet);
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB5, idSet);
		addPacketOpcode(SM_DUEL.class, 0xB6, idSet);
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB7, idSet);
		addPacketOpcode(SM_PET_MOVE.class, 0xB8, idSet);
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBF, idSet); //2.6
		addPacketOpcode(SM_DIE.class, 0xBE, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class,0xBF, idSet);// Testing
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC0, idSet);
		addPacketOpcode(SM_RESURRECT.class, 0xC1, idSet);
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0xC3, idSet);
		addPacketOpcode(SM_REPURCHASE.class, 0xC4, idSet);
		addPacketOpcode(SM_FIND_GROUP.class, 0xC5, idSet);
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC6, idSet);
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC7, idSet);
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0xC8, idSet);
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC9, idSet);
		addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0xCA, idSet);
		addPacketOpcode(SM_INGAMESHOP.class, 0xCB, idSet);
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0xCD, idSet);
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xD1, idSet); //2.6
		addPacketOpcode(SM_TITLE_LIST.class, 0xCF, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD2, idSet);// Testing
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD4, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xD5, idSet);// Testing
		//addPacketOpcode(SM_ABYSS_ARTIFACT_INFO2.class,0xD9, idSet); - renamed to the below one.
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0xD9, idSet);
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDB, idSet);
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDD, idSet);// 1.9
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDE, idSet);
		addPacketOpcode(SM_BLOCK_LIST.class, 0xDF, idSet);
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE0, idSet);
		addPacketOpcode(SM_USE_OBJECT.class, 0xE5, idSet); //2.6
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xE2, idSet); //2.6
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE7, idSet); // 2.6
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE9, idSet); //2.6
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xE6, idSet); //2.6
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xEB, idSet); //2.6
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE8, idSet); //2.6
		addPacketOpcode(SM_LOOT_STATUS.class, 0xED, idSet); //2.6
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xEA, idSet); //2.7
		addPacketOpcode(SM_RECIPE_LIST.class, 0xEF, idSet); //2.6
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xEC, idSet); //2.6
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xF1, idSet); //2.6
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEE, idSet); //2.6
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xD3, idSet); //2.6
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF2, idSet);
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF5, idSet);// testing
		addPacketOpcode(SM_FLY_TIME.class, 0xF3, idSet);
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF4, idSet);
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF6, idSet);
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF9, idSet);
		addPacketOpcode(SM_TRADELIST.class, 0xFD, idSet); //2.6
		addPacketOpcode(SM_PRICES.class, 0xFA, idSet); //2.6
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFC, idSet);
		addPacketOpcode(SM_STATS_INFO.class, 0xFF, idSet);
		addPacketOpcode(SM_VERSION_CHECK.class, 0xFE, idSet); //2.6
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
	}

	private void initPacketsFor_2_5()
	{
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_STATUPDATE_HP.class, 0x00, idSet);
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x01, idSet);
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x02, idSet);
		addPacketOpcode(SM_MACRO_LIST.class, 0x04, idSet);
		addPacketOpcode(SM_CHAT_INIT.class, 0x05, idSet);
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x06, idSet);
		addPacketOpcode(SM_MACRO_RESULT.class, 0x07, idSet);
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x08, idSet);
		addPacketOpcode(SM_ABYSS_RANK.class, 0x0A, idSet);
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x0B, idSet);
		addPacketOpcode(SM_PETITION.class, 0x0C, idSet);
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x0E, idSet);
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0F, idSet);
		addPacketOpcode(SM_FORTRESS_INFO.class, 0x10, idSet);
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x11, idSet);
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x12, idSet);
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x13, idSet);
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x14, idSet);
		addPacketOpcode(SM_DELETE.class, 0x15, idSet);
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x16, idSet);
		addPacketOpcode(SM_MESSAGE.class, 0x17, idSet);
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x18, idSet);
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x19, idSet);
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x1A, idSet);
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1B, idSet);
		addPacketOpcode(SM_STANCE_STATE.class, 0x1C, idSet); // test
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1D, idSet);
		addPacketOpcode(SM_CASTSPELL.class, 0x1E, idSet);
		addPacketOpcode(SM_PLAYER_INFO.class, 0x1F, idSet);
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x20, idSet);
		addPacketOpcode(SM_GATHER_STATUS.class, 0x21, idSet);
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x22, idSet);
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x23, idSet);
		addPacketOpcode(SM_DP_INFO.class, 0x26, idSet);
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x27, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x28, idSet);
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2A, idSet);
		addPacketOpcode(SM_LEGION_TABS.class, 0x2B, idSet);
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2C, idSet);
		addPacketOpcode(SM_NPC_INFO.class, 0x2D, idSet);
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x2E, idSet);
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x33, idSet);
		addPacketOpcode(SM_MOVE.class, 0x34, idSet);
		addPacketOpcode(SM_ATTACK.class, 0x35, idSet);
		//addPacketOpcode(SM_UNKNOW.class,0x36, idSet); Opcode on retail but dunno what is for
		addPacketOpcode(SM_TRANSFORM.class, 0x39, idSet);
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3B, idSet);
		addPacketOpcode(SM_SELL_ITEM.class, 0x3D, idSet);
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x3E, idSet);
		addPacketOpcode(SM_WEATHER.class, 0x40, idSet);
		addPacketOpcode(SM_EMOTION.class, 0x42, idSet);
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x43, idSet);
		addPacketOpcode(SM_TIME_CHECK.class, 0x44, idSet);
		addPacketOpcode(SM_GAME_TIME.class, 0x45, idSet);
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x46, idSet);
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x47, idSet);
		addPacketOpcode(SM_CASTSPELL_END.class, 0x48, idSet);
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x49, idSet);
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x4A, idSet);
		addPacketOpcode(SM_SKILL_LIST.class, 0x4B, idSet);
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4D, idSet);
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x4E, idSet);
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x50, idSet);
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x51, idSet);
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x52, idSet);
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x55, idSet);
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x56, idSet);
		addPacketOpcode(SM_NAME_CHANGE.class, 0x57, idSet);
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x58, idSet);
		addPacketOpcode(SM_GROUP_INFO.class, 0x59, idSet);// Not Sure
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5F, idSet);
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x61, idSet);
		addPacketOpcode(SM_PLAYER_STATE.class, 0x63, idSet);
		addPacketOpcode(SM_STARTED_QUEST_LIST.class, 0x64, idSet);
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x65, idSet);
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x66, idSet);
		addPacketOpcode(SM_KEY.class, 0x67, idSet);
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x68, idSet);
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x69, idSet);
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6A, idSet);
		addPacketOpcode(SM_EMOTION_LIST.class, 0x6C, idSet);
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6D, idSet);
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x6E, idSet);
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x70, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x74, idSet);
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x76, idSet);
		addPacketOpcode(SM_QUEST_LIST.class, 0x78, idSet);
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x79, idSet);
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x7B, idSet);
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7C, idSet);
		addPacketOpcode(SM_PING_RESPONSE.class, 0x7F, idSet);
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x81, idSet);
		addPacketOpcode(SM_PET.class, 0x82, idSet);
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x84, idSet);
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x86, idSet);
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x87, idSet);
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8C, idSet);
		addPacketOpcode(SM_LEGION_INFO.class, 0x8D, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x91, idSet);
		addPacketOpcode(SM_CMOTION.class, 0x93, idSet);//2.5
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x8E, idSet);
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8F, idSet);
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x96, idSet);
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x98, idSet);
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x99, idSet);
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9A, idSet);
		addPacketOpcode(SM_INGAMESHOP_BALANCE.class, 0x9C, idSet);
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9D, idSet);
		addPacketOpcode(SM_MAIL_SERVICE.class, 0x9E, idSet);
		addPacketOpcode(SM_WINDSTREAM.class, 0xA0, idSet);
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA1, idSet);
		addPacketOpcode(SM_PRIVATE_STORE.class, 0xA5, idSet);
		addPacketOpcode(SM_FRIEND_LIST.class, 0xA3, idSet);
		addPacketOpcode(SM_GROUP_LOOT.class, 0xA4, idSet);
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA6, idSet);
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA7, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xA8, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA9, idSet);
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0xAA, idSet);
		addPacketOpcode(SM_RIFT_STATUS.class, 0xAC, idSet);// was AB
		addPacketOpcode(SM_STAGE_STEP_STATUS.class, 0xAB, idSet);// test 2.5
		addPacketOpcode(SM_PONG.class, 0xAD, idSet);
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xAE, idSet);
		addPacketOpcode(SM_KISK_UPDATE.class, 0xAF, idSet);
		addPacketOpcode(SM_BROKER_ITEMS.class, 0xB1, idSet);
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB2, idSet);// was CD
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB3, idSet);
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB4, idSet);
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB5, idSet);
		addPacketOpcode(SM_DUEL.class, 0xB6, idSet);
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB7, idSet);
		addPacketOpcode(SM_PET_MOVE.class, 0xB8, idSet);
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBC, idSet);
		addPacketOpcode(SM_DIE.class, 0xBE, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class,0xBF, idSet);// Testing
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC0, idSet);
		addPacketOpcode(SM_RESURRECT.class, 0xC1, idSet);
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0xC3, idSet);
		addPacketOpcode(SM_REPURCHASE.class, 0xC4, idSet);
		addPacketOpcode(SM_FIND_GROUP.class, 0xC5, idSet);
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC6, idSet);
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC7, idSet);
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0xC8, idSet);
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC9, idSet);
		addPacketOpcode(SM_INGAMESHOP_ITEMS.class, 0xCA, idSet);
		addPacketOpcode(SM_INGAMESHOP.class, 0xCB, idSet);
		addPacketOpcode(SM_INGAMESHOP_ITEM.class, 0xCD, idSet);
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xCE, idSet);
		addPacketOpcode(SM_TITLE_LIST.class, 0xCF, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD2, idSet);// Testing
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD4, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xD5, idSet);// Testing
		//addPacketOpcode(SM_ABYSS_ARTIFACT_INFO2.class,0xD9, idSet); - renamed to the below one.
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0xD9, idSet);
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xDB, idSet);
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDD, idSet);// 1.9
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDE, idSet);
		addPacketOpcode(SM_BLOCK_LIST.class, 0xDF, idSet);
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE0, idSet);
		addPacketOpcode(SM_USE_OBJECT.class, 0xE2, idSet);
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xE3, idSet);
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE4, idSet);
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE6, idSet);
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xE7, idSet);
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xE8, idSet);
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE9, idSet);
		addPacketOpcode(SM_LOOT_STATUS.class, 0xEA, idSet);
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xEB, idSet);
		addPacketOpcode(SM_RECIPE_LIST.class, 0xEC, idSet);
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xED, idSet);
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xEE, idSet);
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEF, idSet);
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xF0, idSet);
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF2, idSet);
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF5, idSet);// testing
		addPacketOpcode(SM_FLY_TIME.class, 0xF3, idSet);
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF4, idSet);
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF6, idSet);
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF9, idSet);
		addPacketOpcode(SM_TRADELIST.class, 0xFA, idSet);
		addPacketOpcode(SM_PRICES.class, 0xFB, idSet);
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFC, idSet);
		addPacketOpcode(SM_STATS_INFO.class, 0xFE, idSet);
		addPacketOpcode(SM_VERSION_CHECK.class, 0xFF, idSet);
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet);// fake packet
	}

	private void initPacketsFor_1_9()
	{
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_STATUPDATE_MP.class, 0x00, idSet);// 1.9
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x01, idSet);// 1.9
		addPacketOpcode(SM_CHAT_INIT.class, 0x02, idSet); // 1.9
		addPacketOpcode(SM_CHANNEL_INFO.class, 0x03, idSet);// 1.9
		addPacketOpcode(SM_MACRO_RESULT.class, 0x04, idSet); // 1.9
		addPacketOpcode(SM_MACRO_LIST.class, 0x05, idSet);// 1.9
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0x07, idSet);// 1.9
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0x08, idSet);// 1.9
		addPacketOpcode(SM_SET_BIND_POINT.class, 0x09, idSet);// 1.9
		addPacketOpcode(SM_ABYSS_RANK.class, 0x0B, idSet);// 1.9
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0x0C, idSet);
		addPacketOpcode(SM_PETITION.class, 0x0D, idSet);// 1.9
		addPacketOpcode(SM_RECIPE_DELETE.class, 0x0E, idSet); // 1.9
		addPacketOpcode(SM_LEARN_RECIPE.class, 0x0F, idSet);// 1.9
		addPacketOpcode(SM_FLY_TIME.class, 0x10, idSet);// 1.9
		addPacketOpcode(SM_FORTRESS_INFO.class, 0x11, idSet);// 1.9
		addPacketOpcode(SM_DELETE.class, 0x12, idSet);// 1.9
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x13, idSet);// 1.9
		addPacketOpcode(SM_MESSAGE.class, 0x14, idSet);// 1.9
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x15, idSet); // 1.9
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x16, idSet);// 1.9
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x17, idSet);// 1.9
		addPacketOpcode(SM_DELETE_ITEM.class, 0x18, idSet);
		addPacketOpcode(SM_INVENTORY_UPDATE.class, 0x19, idSet);// 1.9
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1A, idSet);// 1.9
		addPacketOpcode(SM_UPDATE_ITEM.class, 0x1B, idSet);// 1.9
		addPacketOpcode(SM_PLAYER_INFO.class, 0x1C, idSet);// 1.9
		addPacketOpcode(SM_STANCE_STATE.class, 0x1D, idSet);// 1.9
		addPacketOpcode(SM_GATHER_STATUS.class, 0x1E, idSet);// 1.9
		addPacketOpcode(SM_CASTSPELL.class, 0x1F, idSet);// 1.9
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x20, idSet);// 1.9
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x21, idSet);// 1.9
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x22, idSet);// 1.9
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x23, idSet);// 1.9
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x24, idSet);// 1.9
		addPacketOpcode(SM_DP_INFO.class, 0x25, idSet);// 1.9
		addPacketOpcode(SM_LEGION_TABS.class, 0x28, idSet);// 1.9
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x29, idSet);// 1.9
		addPacketOpcode(SM_NPC_INFO.class, 0x2A, idSet);// 1.9
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x2B, idSet);// 1.9
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x2D, idSet);// 1.9
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x2F, idSet);// 1.9
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x30, idSet);// 1.9
		addPacketOpcode(SM_ATTACK.class, 0x32, idSet);// 1.9
		addPacketOpcode(SM_MOVE.class, 0x35, idSet);// 1.9
		addPacketOpcode(SM_TRANSFORM.class, 0x36, idSet);// 1.9
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x38, idSet);// 1.9
		addPacketOpcode(SM_SELL_ITEM.class, 0x3A, idSet);// 1.9
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x3F, idSet);
		addPacketOpcode(SM_PLAYER_STATE.class, 0x40, idSet);// 1.9
		addPacketOpcode(SM_WEATHER.class, 0x41, idSet);// 1.9
		addPacketOpcode(SM_GAME_TIME.class, 0x42, idSet);// 1.9
		addPacketOpcode(SM_EMOTION.class, 0x43, idSet);// 1.9
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x44, idSet);// 1.9
		addPacketOpcode(SM_TIME_CHECK.class, 0x45, idSet);// 1.9
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x46, idSet);// 1.9
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x47, idSet);// 1.9
		addPacketOpcode(SM_SKILL_LIST.class, 0x48, idSet);// 1.9
		addPacketOpcode(SM_CASTSPELL_END.class, 0x49, idSet);// 1.9
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x4A, idSet);// 1.9
		addPacketOpcode(SM_STIGMA_SKILL_REMOVE.class, 0x4B, idSet);// 1.9
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x4E, idSet);// 1.9
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x4F, idSet);// 1.9
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x50, idSet);// 1.9
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x51, idSet);// 1.9
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x52, idSet);// 1.9
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x53, idSet);// 1.9
		addPacketOpcode(SM_NAME_CHANGE.class, 0x54, idSet);// 1.9
		addPacketOpcode(SM_GROUP_INFO.class, 0x56, idSet);// 1.9
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x57, idSet);// 1.9
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x59, idSet);// 1.9
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0x5C, idSet);// 1.9
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x5E, idSet);// 1.9
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x62, idSet);// 1.9
		addPacketOpcode(SM_KEY.class, 0x64, idSet); // 1.9
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x66, idSet);// 1.9
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x67, idSet);// testing
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x69, idSet);// 1.9
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x6A, idSet);// 1.9
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x6B, idSet);// 1.9
		addPacketOpcode(SM_EMOTION_LIST.class, 0x6D, idSet);// 1.9
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x6F, idSet);// 1.9
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x71, idSet);// 1.9
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x75, idSet);// 1.9
		addPacketOpcode(SM_DREDGION_INSTANCE.class, 0x76, idSet);// 2.0.0.5
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x77, idSet);// 2.0.0.5
		addPacketOpcode(SM_QUEST_ACCEPTED.class, 0x78, idSet);// 1.9
		addPacketOpcode(SM_QUEST_LIST.class, 0x79, idSet); // 1.9
		addPacketOpcode(SM_PING_RESPONSE.class, 0x7C, idSet);// 1.9
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7D, idSet); // 1.9
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x7E, idSet); // 1.9
		addPacketOpcode(SM_FRIEND_LIST.class, 0x80, idSet);// 1.9
		addPacketOpcode(SM_PET.class, 0x83, idSet);// 2.0
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x84, idSet); // 1.9
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x85, idSet);// 1.9
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x87, idSet); // 1.9
		addPacketOpcode(SM_LEGION_INFO.class, 0x8A, idSet);// 1.9
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x8C, idSet);// 1.9
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x8D, idSet);// 1.9
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x8E, idSet);// 1.9
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x8F, idSet);// 1.9
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x96, idSet);// testing
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x97, idSet);// testing
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x99, idSet);// testing
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9A, idSet);// 1.9
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9B, idSet);// 1.9
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0x9E, idSet);// testing
		addPacketOpcode(SM_MAIL_SERVICE.class, 0x9F, idSet);// 1.9
		addPacketOpcode(SM_WINDSTREAM_LOCATIONS.class, 0xA0, idSet);// 2.0.0.3
		addPacketOpcode(SM_WINDSTREAM.class, 0xA1, idSet);// 2.0.0.3
		addPacketOpcode(SM_PRIVATE_STORE.class, 0xA2, idSet);
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0xA4, idSet);// testing
		addPacketOpcode(SM_GROUP_LOOT.class, 0xA5, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0xA6, idSet);// 1.9
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0xA7, idSet);// 1.9
		addPacketOpcode(SM_RIFT_STATUS.class, 0xA8, idSet); // 1.9
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0xA9, idSet);// 1.9
		addPacketOpcode(SM_PONG.class, 0xAA, idSet);// 1.9
		addPacketOpcode(SM_INSTANCE_COOLDOWN.class, 0xAB, idSet);// 1.9
		addPacketOpcode(SM_KISK_UPDATE.class, 0xAC, idSet);// 1.9
		addPacketOpcode(SM_BROKER_ITEMS.class, 0xAE, idSet);// 1.9
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0xAF, idSet);// 1.9
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB2, idSet);// 1.9
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB3, idSet);// was CD
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB4, idSet);// 1.9
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB5, idSet);// 1.9
		addPacketOpcode(SM_DUEL.class, 0xB7, idSet);// 1.9
		addPacketOpcode(SM_PET_MOVE.class, 0xB9, idSet);// 2.0
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBD, idSet); // 1.9
		addPacketOpcode(SM_RESURRECT.class, 0xBE, idSet);// 1.9
		addPacketOpcode(SM_DIE.class, 0xBF, idSet);// 1.9
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xC0, idSet);// 1.9
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC1, idSet);// 1.9
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xC4, idSet);// 1.9
		addPacketOpcode(SM_REPURCHASE.class, 0xC5, idSet);// 1.9
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xC6, idSet);// 1.9
		addPacketOpcode(SM_WAREHOUSE_UPDATE.class, 0xC7, idSet);// 1.9
		addPacketOpcode(SM_UPDATE_WAREHOUSE_ITEM.class, 0xC9, idSet);// 1.9
		addPacketOpcode(SM_TITLE_LIST.class, 0xCC, idSet);// 1.9
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xD0, idSet);
		addPacketOpcode(SM_LEGION_EMBLEM_SEND.class, 0xD2, idSet);// 1.9
		addPacketOpcode(SM_LEGION_EMBLEM.class, 0xD3, idSet);// 1.9
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD5, idSet);// 1.9
		addPacketOpcode(SM_SIEGE_AETHERIC_FIELDS.class, 0xD6, idSet);// 1.9
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO3.class, 0xD8, idSet);// 1.9
		addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDA, idSet);// 1.9
		addPacketOpcode(SM_BLOCK_LIST.class, 0xDC, idSet);// 1.9
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDD, idSet);// 1.9
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xDF, idSet);// 1.9
		addPacketOpcode(SM_NPC_WEARING_OBJECT.class, 0xE2, idSet);// 2.0.0.5
		addPacketOpcode(SM_USE_OBJECT.class, 0xE3, idSet);// 1.9
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xE4, idSet);// 1.9
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xE5, idSet);// 1.9
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xE6, idSet);
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xE7, idSet);// 1.9
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xE8, idSet);
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xE9, idSet);// 1.9
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xEA, idSet);// 1.9
		addPacketOpcode(SM_LOOT_STATUS.class, 0xEB, idSet);// 1.9
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xEC, idSet);// 1.9
		addPacketOpcode(SM_RECIPE_LIST.class, 0xED, idSet);// testing
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xEF, idSet);// 1.9
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xF1, idSet);// 1.9
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF2, idSet);// 1.9
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF3, idSet);// 1.9
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF5, idSet);// 1.9
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xF6, idSet);// 1.9
		addPacketOpcode(SM_SHOW_BRAND.class, 0xF7, idSet);// 1.9
		addPacketOpcode(SM_PRICES.class, 0xF8, idSet);// 1.9
		addPacketOpcode(SM_TRADELIST.class, 0xFB, idSet);// 1.9
		addPacketOpcode(SM_VERSION_CHECK.class, 0xFC, idSet);// 1.9
		addPacketOpcode(SM_RECONNECT_KEY.class, 0xFD, idSet);// 1.9
		addPacketOpcode(SM_STATS_INFO.class, 0xFF, idSet);// 1.9
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet); // fake packet
	}
}
