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
package gameserver.network.factories;


import gameserver.configs.main.GSConfig;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionPacketHandler;
import gameserver.network.aion.AionConnection.State;
import gameserver.network.aion.clientpackets.*;

public class AionPacketHandlerFactory
{
	private AionPacketHandler	handler;

	public static final AionPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}

	private AionPacketHandlerFactory()
	{
		handler = new AionPacketHandler();
		
		if(GSConfig.SERVER_VERSION.startsWith("2.7"))
            init_2_7();
	}
	
	private void init_2_7()
	{
		addPacket(new CM_QUESTIONNAIRE(0x00), State.IN_GAME);// 2.6
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x04), State.CONNECTED);// 2.6
		addPacket(new CM_CHARACTER_LIST(0x05), State.AUTHED);// 2.6
		addPacket(new CM_TELEPORT_SELECT(0x07), State.IN_GAME);// 2.6
		addPacket(new CM_RESTORE_CHARACTER(0x08), State.AUTHED);// 2.6
		addPacket(new CM_START_LOOT(0x09), State.IN_GAME);// 2.6
		addPacket(new CM_CREATE_CHARACTER(0x0A), State.AUTHED);// 2.6
		addPacket(new CM_DELETE_CHARACTER(0x0B), State.AUTHED);// 2.6
		addPacket(new CM_SPLIT_ITEM(0x0C), State.IN_GAME);// 2.6
		addPacket(new CM_LOOT_ITEM(0x0E), State.IN_GAME);// 2.6
		addPacket(new CM_MOVE_ITEM(0x0F), State.IN_GAME);// 2.6

		// addPacket(new CM_DISCONNECT(0x11), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_MAIL_SUMMON_ZEPHYR(0x11), State.IN_GAME);// 2.6
		// addPacket(new CM_LEGION_UPLOAD_INFO(0x12), State.IN_GAME);// 2.1
		// Testing
		addPacket(new CM_PLAYER_SEARCH(0x12), State.IN_GAME);// 2.6
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x13), State.IN_GAME);// 2.1
																	// Testing
		addPacket(new CM_BLOCK_ADD(0x15), State.IN_GAME);// 2.6
		addPacket(new CM_FRIEND_STATUS(0x19), State.IN_GAME);// 2.6
		addPacket(new CM_BLOCK_DEL(0x1A), State.IN_GAME);// 2.6
		addPacket(new CM_SHOW_BLOCKLIST(0x1B), State.IN_GAME);// 2.6
		// addPacket(new CM_ITEM_REPLACE(0x1B), State.IN_GAME);// Testing
		addPacket(new CM_MAC_ADDRESS2(0x1D), State.IN_GAME);// 2.6
		addPacket(new CM_CHANGE_CHANNEL(0x1F), State.IN_GAME);// 2.6

		addPacket(new CM_CHECK_NICKNAME(0x20), State.AUTHED);// 2.6
		// addPacket(new CM_BLOCK_SET_REASON(0x21), State.IN_GAME);// 2.1
		addPacket(new CM_MACRO_CREATE(0x22), State.IN_GAME);// 2.6
		addPacket(new CM_MACRO_DELETE(0x23), State.IN_GAME);// 2.6
		// addPacket(new CM_SHOW_BRAND(0x27), State.IN_GAME);// 2.1
		addPacket(new CM_GROUP_LOOT(0x28), State.IN_GAME);// 2.6
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x29), State.AUTHED);// 2.6
		addPacket(new CM_RECONNECT_AUTH(0x2A), State.AUTHED);// 2.6
		// addPacket(new CM_DISTRIBUTION_SETTINGS(0x2B), State.IN_GAME);// 2.1
		addPacket(new CM_MAC_ADDRESS(0x2C), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x2F), State.IN_GAME);// 2.6

		addPacket(new CM_INSTANCE_CD_REQUEST(0x32), State.IN_GAME);// 2.1
		addPacket(new CM_REPORT_PLAYER(0x32), State.IN_GAME);// 2.6
		// addPacket(new CM_INGAMESHOP(0x33), State.IN_GAME);// 2.1
		// addPacket(new CM_NAME_CHANGE(0x37), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_MAP(0x37), State.IN_GAME);// 2.6
		addPacket(new CM_SUMMON_MOVE(0x38), State.IN_GAME);// 2.6
		addPacket(new CM_SUMMON_EMOTION(0x39), State.IN_GAME);// 2.6
		// addPacket(new CM_DREDGION_REQUEST(0x3A), State.IN_GAME); // 2.1
		// addPacket(new CM_FUSION_WEAPONS(0x3C), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_CASTSPELL(0x3C), State.IN_GAME);// 2.6
		// addPacket(new CM_BREAK_WEAPONS(0x3D), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_ATTACK(0x3E), State.IN_GAME);// 2.6

		addPacket(new CM_PLAY_MOVIE_END(0x40), State.IN_GAME);// 2.6
		addPacket(new CM_DELETE_QUEST(0x43), State.IN_GAME);// 2.6
		// addPacket(new CM_ACADEMY_BOOTCAMP_EXIT(0x46), State.IN_GAME);// 2.1
		addPacket(new CM_ITEM_REMODEL(0x48), State.IN_GAME);// 2.1
		// addPacket(new CM_GODSTONE_SOCKET(0x49), State.IN_GAME);// 2.1
		// addPacket(new CM_ALLIANCE_GROUP_CHANGE(0x4D), State.IN_GAME);// 1.9
		addPacket(new CM_INVITE_TO_GROUP(0x50), State.IN_GAME);// 2.6
		addPacket(new CM_PLAYER_STATUS_INFO(0x53), State.IN_GAME);// 2.6
		addPacket(new CM_PING_REQUEST(0x5A), State.IN_GAME);// 2.6
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x57), State.IN_GAME);// 2.6
		addPacket(new CM_SHOW_FRIENDLIST(0x5D), State.IN_GAME);// 2.6
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x5E), State.IN_GAME);// 2.6
		addPacket(new CM_GROUP_DISTRIBUTION(0x5F), State.IN_GAME);// 2.6

		addPacket(new CM_DUEL_REQUEST(0x61), State.IN_GAME);// 2.6
		addPacket(new CM_FRIEND_ADD(0x62), State.IN_GAME);// 2.6
		addPacket(new CM_FRIEND_DEL(0x63), State.IN_GAME);// 2.6
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x65), State.IN_GAME);// 2.6
		addPacket(new CM_DELETE_ITEM(0x67), State.IN_GAME);// 2.6
		addPacket(new CM_SUMMON_COMMAND(0x68), State.IN_GAME);// 2.6
		addPacket(new CM_PRIVATE_STORE(0x6A), State.IN_GAME);// 2.6
		addPacket(new CM_PRIVATE_STORE_NAME(0x6B), State.IN_GAME);// 2.6
		addPacket(new CM_BROKER_REGISTERED(0x6C), State.IN_GAME);// 2.6
		addPacket(new CM_BUY_BROKER_ITEM(0x6D), State.IN_GAME);// 2.6
		addPacket(new CM_BROKER_LIST(0x6E), State.IN_GAME);// 2.6
		addPacket(new CM_BROKER_SEARCH(0x6F), State.IN_GAME);// 2.6

		addPacket(new CM_BROKER_SETTLE_LIST(0x70), State.IN_GAME);// 2.6
		// addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x70), State.IN_GAME);// 2.1
		addPacket(new CM_REGISTER_BROKER_ITEM(0x72), State.IN_GAME);// 2.6
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x73), State.IN_GAME);// 2.6
		addPacket(new CM_READ_MAIL(0x75), State.IN_GAME);// 2.6
		addPacket(new CM_SEND_MAIL(0x77), State.IN_GAME);// 2.6
		addPacket(new CM_DELETE_MAIL(0x78), State.IN_GAME);// 2.6
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x7B), State.IN_GAME);// 2.6
		// addPacket(new CM_CLIENT_COMMAND_LOC(0x7C), State.IN_GAME);// 2.1
		addPacket(new CM_CRAFT(0x7C), State.IN_GAME);// 2.6
		addPacket(new CM_TITLE_SET(0x7E), State.IN_GAME);// 2.6

		addPacket(new CM_TIME_CHECK(0x81), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_LEGION_EMBLEM(0x83), State.IN_GAME);// 2.6
		addPacket(new CM_PET_MOVE(0x84), State.IN_GAME);// 2.6
		// addPacket(new CM_OPEN_STATICDOOR(0x85), State.IN_GAME);// 2.1
		addPacket(new CM_PET(0x85), State.IN_GAME);// 2.6
		addPacket(new CM_GATHER(0x86), State.IN_GAME);// 2.6
		// addPacket(new CM_PETITION(0x88), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x8E), State.IN_GAME);// 2.6
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x8F), State.IN_GAME);// 2.6

		// addPacket(new CM_SKILL_DEACTIVATE(0x90), State.IN_GAME); // 2.1
		addPacket(new CM_CASTSPELL(0x90), State.IN_GAME);// 2.6
		addPacket(new CM_TARGET_SELECT(0x92), State.IN_GAME);// 2.6
		addPacket(new CM_ATTACK(0x93), State.IN_GAME);// 2.6
		addPacket(new CM_USE_ITEM(0x94), State.IN_GAME);// 2.6
		addPacket(new CM_EQUIP_ITEM(0x95), State.IN_GAME);// 2.6
		addPacket(new CM_REMOVE_ALTERED_STATE(0x96), State.IN_GAME);// 2.6
		addPacket(new CM_PLAYER_LISTENER(0x9B), State.IN_GAME);// 2.6
		// addPacket(new CM_EXIT_LOCATION(0x9C), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION(0x9C), State.IN_GAME);// 2.6
		addPacket(new CM_EMOTION(0x9E), State.IN_GAME);// 2.6
		addPacket(new CM_PING(0x9F), State.AUTHED, State.IN_GAME);// 2.6

		addPacket(new CM_FLIGHT_TELEPORT(0xA0), State.IN_GAME);// 2.6
		addPacket(new CM_QUESTION_RESPONSE(0xA1), State.IN_GAME);// 2.6
		addPacket(new CM_LEGION_EMBLEM_SEND(0xA2), State.IN_GAME);// 2.6
		addPacket(new CM_MOVE(0xA3), State.IN_GAME);// 2.6
		addPacket(new CM_CLOSE_DIALOG(0xA4), State.IN_GAME);// 2.6
		addPacket(new CM_DIALOG_SELECT(0xA5), State.IN_GAME);// 2.6
		addPacket(new CM_BUY_ITEM(0xA6), State.IN_GAME);// 2.6
		addPacket(new CM_SHOW_DIALOG(0xA7), State.IN_GAME);// 2.6
		// addPacket(new CM_LEGION_MODIFY_EMBLEM(0xA9), State.IN_GAME);//
		// Testing
		addPacket(new CM_SET_NOTE(0xA9), State.IN_GAME);// 2.6
		addPacket(new CM_LEGION_TABS(0xAA), State.IN_GAME);// 2.6
		// addPacket(new CM_TWITTER_ADDON(0xAC), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_RECRUIT_GROUP(0xAF), State.IN_GAME);// 2.1

		addPacket(new CM_EXCHANGE_ADD_KINAH(0xB1), State.IN_GAME);// 2.6
		addPacket(new CM_EXCHANGE_ADD_ITEM(0xB3), State.IN_GAME);// 2.6
		addPacket(new CM_EXCHANGE_REQUEST(0xB2), State.IN_GAME);// 2.6
		// addPacket(new CM_WINDSTREAM(0xB4), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_CANCEL(0xB4), State.IN_GAME);// 2.6
		// addPacket(new CM_PLAYER_MOTION_SET(0xB5), State.IN_GAME);//2.5
		addPacket(new CM_EXCHANGE_LOCK(0xB6), State.IN_GAME);// 2.6
		addPacket(new CM_EXCHANGE_OK(0xB7), State.IN_GAME);// 2.6
		addPacket(new CM_MANASTONE(0xB9), State.IN_GAME);// 2.6
		addPacket(new CM_FIND_GROUP(0xBC), State.IN_GAME);// 2.6

		addPacket(new CM_CHARACTER_PASSKEY(0xC0), State.AUTHED);// 2.6
		addPacket(new CM_OPEN_MAIL_WINDOW(0xC7), State.IN_GAME);// 2.6

		addPacket(new CM_VERSION_CHECK(0xF3), State.CONNECTED);// 2.6
		addPacket(new CM_REVIVE(0xF4), State.IN_GAME);// 2.6
		addPacket(new CM_CHARACTER_EDIT(0xF5), State.AUTHED);// 2.1 testing
		addPacket(new CM_QUIT(0xF6), State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_MAY_QUIT(0xF7), State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_LEVEL_READY(0xF8), State.IN_GAME);// 2.6
		addPacket(new CM_UI_SETTINGS(0xF9), State.IN_GAME);// 2.6
		addPacket(new CM_ENTER_WORLD(0xFB), State.AUTHED);// 2.6
		addPacket(new CM_OBJECT_SEARCH(0xFE), State.IN_GAME);// 2.6
		addPacket(new CM_CUSTOM_SETTINGS(0xFF), State.IN_GAME);// 2.6
	}
	

	public AionPacketHandler getPacketHandler()
	{
		return handler;
	}

	private void addPacket(AionClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AionPacketHandlerFactory	instance	= new AionPacketHandlerFactory();
	}
}
