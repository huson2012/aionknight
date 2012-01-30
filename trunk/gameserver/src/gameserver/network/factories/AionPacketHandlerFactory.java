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

package gameserver.network.factories;

import gameserver.configs.main.GSConfig;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection.State;
import gameserver.network.aion.AionPacketHandler;
import gameserver.network.aion.clientpackets.*;

public class AionPacketHandlerFactory
{
	private AionPacketHandler	handler;
	public static final AionPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 */
		private AionPacketHandlerFactory()
		{
		handler = new AionPacketHandler();

		//here is a place to implement support for other aion versions ;]
		if(GSConfig.SERVER_VERSION.startsWith("2.7"))
			init_2_7();
		if(GSConfig.SERVER_VERSION.startsWith("2.6"))
			init_2_6();
		if(GSConfig.SERVER_VERSION.startsWith("2.5"))
			init_2_5();
		if(GSConfig.SERVER_VERSION.startsWith("1.9"))
			init_1_9();
		}

	private void init_2_7()
	{
		addPacket(new CM_QUESTIONNAIRE(0x0100), State.IN_GAME);//  2.6 0x00, 2.7
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x0104), State.CONNECTED);// 2.6 0x04, 2.7
		addPacket(new CM_CHARACTER_LIST(0x0105), State.AUTHED);// 2.6 0x05, 2.7
		addPacket(new CM_TELEPORT_SELECT(0x0107), State.IN_GAME);// 2.6 0x07, 2.7
        addPacket(new CM_RESTORE_CHARACTER(0x0108), State.AUTHED);// 2.6 0x08, 2.7
        addPacket(new CM_START_LOOT(0x0109), State.IN_GAME);// 2.6 0x09, 2.7
        addPacket(new CM_CREATE_CHARACTER(0x010A), State.AUTHED);// 2.6 0x0A, 2.7
		addPacket(new CM_DELETE_CHARACTER(0x010B), State.AUTHED);// 2.6 0x0B, 2.7
        addPacket(new CM_SPLIT_ITEM(0x010C), State.IN_GAME);// 2.6 0x0C, 2.7
        // 2.7 Unknown Packet - 0D 01 57 F2 FE
        addPacket(new CM_LOOT_ITEM(0x010E), State.IN_GAME);// 2.6 0x0E, 2.7        
        addPacket(new CM_MOVE_ITEM(0x010F), State.IN_GAME);// 2.6 0x0F, 2.7
        addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x0110), State.IN_GAME);// 2.6 0x10, 2.7 Testing
        addPacket(new CM_MAIL_SUMMON_ZEPHYR(0x0111), State.IN_GAME);// 2.6 0x11, 2.7
        addPacket(new CM_PLAYER_SEARCH(0x0112), State.IN_GAME);// 2.6 0x12, 2.7
        addPacket(new CM_LEGION_UPLOAD_INFO(0x0113), State.IN_GAME);// 2.6 0x13, 2.7 Testing
		addPacket(new CM_BLOCK_ADD(0x0115), State.IN_GAME);// 2.6 0x15, 2.7
		addPacket(new CM_FRIEND_STATUS(0x0119), State.IN_GAME);// 2.6 0x19, 2.7
		addPacket(new CM_BLOCK_DEL(0x011A), State.IN_GAME);// 2.6 0x1A, 2.7 Testing
		addPacket(new CM_SHOW_BLOCKLIST(0x011B), State.IN_GAME);// 2.6 0x1B, 2.7
		addPacket(new CM_MAC_ADDRESS2(0x011D), State.IN_GAME);// 2.6 0x1D, 2.7
        addPacket(new CM_CHANGE_CHANNEL(0x011F), State.IN_GAME);// 2.6 0x1F, 2.7
        addPacket(new CM_CHECK_NICKNAME(0x0120), State.AUTHED);// 2.6 0x20, 2.7
		addPacket(new CM_REPLACE_ITEM(0x011C), State.IN_GAME);//  2.7
        addPacket(new CM_MACRO_CREATE(0x0122), State.IN_GAME);// 2.6 0x22, 2.7
		addPacket(new CM_MACRO_DELETE(0x0123), State.IN_GAME);// 2.6 0x23, 2.7
        addPacket(new CM_SHOW_BRAND(0x0124), State.IN_GAME);// 2.6 0x24, 2.7 Testing
		addPacket(new CM_BLOCK_SET_REASON(0x0126), State.IN_GAME);// 2.6 0x26, 2.7 Testing
        addPacket(new CM_DISTRIBUTION_SETTINGS(0x0128), State.IN_GAME);// 2.6 0x28, 2.7
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x0129), State.AUTHED);// 2.6 0x29, 2.7
		addPacket(new CM_RECONNECT_AUTH(0x012A), State.AUTHED);// 2.6 0x25, 2.7 Testing
		addPacket(new CM_GROUP_LOOT(0x012B), State.IN_GAME);//  2.6 0x2B, 2.7
		addPacket(new CM_MAC_ADDRESS(0x012C), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.6 0x2C, 2.7
        addPacket(new CM_ABYSS_RANKING_PLAYERS(0x012F), State.IN_GAME);// 2.6 0x2F, 2.7
        addPacket(new CM_INGAMESHOP(0x0130), State.IN_GAME);// 2.6 0x30, 2.7
        addPacket(new CM_REPORT_PLAYER(0x0132), State.IN_GAME);// 2.6 0x32, 2.7
		addPacket(new CM_INSTANCE_CD_REQUEST(0x0133), State.IN_GAME);// 2.6 0x33, 2.7
        addPacket(new CM_NAME_CHANGE(0x0134), State.IN_GAME);// 2.6 0x34, 2.7
		addPacket(new CM_SHOW_MAP(0x0137), State.IN_GAME);// 2.6 0x37, 2.7
        addPacket(new CM_SUMMON_MOVE(0x0138), State.IN_GAME);// 2.6 0x38, 2.7
		addPacket(new CM_SUMMON_EMOTION(0x0139), State.IN_GAME);// 2.6 0x39, 2.7
		addPacket(new CM_DREDGION_REQUEST(0x013B), State.IN_GAME); // 2.6 0x3B, 2.7
        addPacket(new CM_SUMMON_CASTSPELL(0x013C), State.IN_GAME);// 2.6 0x3C, 2.7
		addPacket(new CM_FUSION_WEAPONS(0x013D), State.IN_GAME);// 2.6 0x3D, 2.7
        addPacket(new CM_SUMMON_ATTACK(0x013E), State.IN_GAME);// 2.6 0x3E, 2.7
		addPacket(new CM_PLAY_MOVIE_END(0x0140), State.IN_GAME);//  2.6 0x40, 2.7
        addPacket(new CM_DELETE_QUEST(0x0143), State.IN_GAME);// 2.6 0x43, 2.7
		addPacket(new CM_EXIT_EC(0x47), State.IN_GAME); // 2.5 -- NEW!
		addPacket(new CM_ITEM_REMODEL(0x0149), State.IN_GAME);// 2.6 0x49, 2.7
		addPacket(new CM_GODSTONE_SOCKET(0x014E), State.IN_GAME);//  2.6 0x4E, 2.7
        addPacket(new CM_INVITE_TO_GROUP(0x0150), State.IN_GAME);// 2.6 0x50, 2.7
		addPacket(new CM_ALLIANCE_GROUP_CHANGE(0x0152), State.IN_GAME);// 2.6 0x52, 2.7 Testing
		addPacket(new CM_PLAYER_STATUS_INFO(0x0153), State.IN_GAME);// 2.6 0x53, 2.7 Testing
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x0157), State.IN_GAME);// 2.6 0x57, 2.7
        addPacket(new CM_PING_REQUEST(0x015A), State.IN_GAME);// 2.6 0x5A, 2.7
		addPacket(new CM_SHOW_FRIENDLIST(0x015D), State.IN_GAME);// 2.6 0x5D, 2.7
        addPacket(new CM_CLIENT_COMMAND_ROLL(0x015E), State.IN_GAME);// 2.6 0x5E, 2.7
		addPacket(new CM_GROUP_DISTRIBUTION(0x015F), State.IN_GAME);//  2.6 0x5F, 2.7 Testing
		addPacket(new CM_DUEL_REQUEST(0x0161), State.IN_GAME);// 2.6 0x61, 2.7
        addPacket(new CM_FRIEND_ADD(0x0162), State.IN_GAME);// 2.6 0x62, 2.7
		addPacket(new CM_FRIEND_DEL(0x0163), State.IN_GAME);// 2.6 0x63, 2.7
        addPacket(new CM_ABYSS_RANKING_LEGIONS(0x0165), State.IN_GAME);// 2.6 0x65, 2.7
		addPacket(new CM_DELETE_ITEM(0x0167), State.IN_GAME);// 2.6 0x67, 2.7
        addPacket(new CM_SUMMON_COMMAND(0x0168), State.IN_GAME);// 2.6 0x68, 2.7
        addPacket(new CM_PRIVATE_STORE(0x016A), State.IN_GAME);// 2.6
        addPacket(new CM_PRIVATE_STORE_NAME(0x016B), State.IN_GAME);// 2.6
        addPacket(new CM_BROKER_REGISTERED(0x016C), State.IN_GAME);// 2.6 0x6C, 2.7
        addPacket(new CM_BUY_BROKER_ITEM(0x016D), State.IN_GAME);//  2.6 0x6D, 2.7
        addPacket(new CM_BROKER_LIST(0x016E), State.IN_GAME);// 2.6 0x6E, 2.7
        addPacket(new CM_BROKER_SEARCH(0x016F), State.IN_GAME);// 2.6 0x6F, 2.7
        addPacket(new CM_BROKER_SETTLE_LIST(0x0170), State.IN_GAME);//  2.6 0x70, 2.7
        addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x0171), State.IN_GAME);//  2.6 0x71, 2.7
        addPacket(new CM_REGISTER_BROKER_ITEM(0x0172), State.IN_GAME);//  2.6 0x72, 2.7
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x0173), State.IN_GAME);// 2.6 0x73, 2.7
        addPacket(new CM_OPEN_MAIL_WINDOW(0x0174), State.IN_GAME);// 2.6 0x74, 2.7
        addPacket(new CM_READ_MAIL(0x0175), State.IN_GAME);// 2.6 0x75, 2.7
        addPacket(new CM_SEND_MAIL(0x0177), State.IN_GAME);// 2.6 0x77, 2.7
        addPacket(new CM_DELETE_MAIL(0x0178), State.IN_GAME);// 2.6 0x78, 27
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x017B), State.IN_GAME);// 2.6 0x7B, 2.7
        addPacket(new CM_CRAFT(0x017C), State.IN_GAME);// 2.6 0x7C, 2.7
		addPacket(new CM_CLIENT_COMMAND_LOC(0x017D), State.IN_GAME);// 2.6 0x7D (2.7 Disabled in Retail?)
        addPacket(new CM_TITLE_SET(0x017E), State.IN_GAME);// 2.6 0x7E, 2.7
		addPacket(new CM_TIME_CHECK(0x0081), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.6 0x81, 2.7
		addPacket(new CM_LEGION_EMBLEM(0x0083), State.IN_GAME);// 2.6 0x83, 2.7
        addPacket(new CM_PET_MOVE(0x0084), State.IN_GAME);// 2.6 0x84, 2.7
        addPacket(new CM_PET(0x0085), State.IN_GAME);// 2.6 0x85, 2.7
        addPacket(new CM_GATHER(0x0086), State.IN_GAME);// 2.6 0x86, 2.7
        addPacket(new CM_PETITION(0x0089), State.IN_GAME);// 2.6 0x89, (2.7 Disabled in Retail)
        addPacket(new CM_OPEN_STATICDOOR(0x008A), State.IN_GAME);// 2.6 0x8A, 2.7 Testing
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x008E), State.IN_GAME);// 2.6 0x8E, 2.7
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x008F), State.IN_GAME);// 2.6 0x8F, 2.7
	    addPacket(new CM_CASTSPELL(0x0090), State.IN_GAME);//  2.6 0x90, 2.7
		addPacket(new CM_SKILL_DEACTIVATE(0x0091), State.IN_GAME); // 2.6 0x91, 2.7
        addPacket(new CM_TARGET_SELECT(0x0092), State.IN_GAME);// 2.6 0x92, 2.7
        addPacket(new CM_ATTACK(0x0093), State.IN_GAME);// 2.6 0x93, 2.7
        addPacket(new CM_USE_ITEM(0x0094), State.IN_GAME);// 2.6 0x94, 2.7
		addPacket(new CM_EQUIP_ITEM(0x0095), State.IN_GAME);// 2.6 0x95, 2.7
        addPacket(new CM_REMOVE_ALTERED_STATE(0x0096), State.IN_GAME);// 2.6 0x96, 2.7
        addPacket(new CM_PLAYER_LISTENER(0x009B), State.IN_GAME);// 2.6 0x9B, 2.7
		addPacket(new CM_LEGION(0x009C), State.IN_GAME);// 2.6 0x9C, 2.7
		addPacket(new CM_EXIT_LOCATION(0x9D), State.IN_GAME);// 2.1
        addPacket(new CM_EMOTION(0x009E), State.IN_GAME);// 2.6 0x9E, 2.7
		addPacket(new CM_PING(0x009F), State.AUTHED, State.IN_GAME);// 2.6 0x9F, 2.7
        addPacket(new CM_FLIGHT_TELEPORT(0x00A0), State.IN_GAME);// 2.6 0xA0, 2.7
		addPacket(new CM_QUESTION_RESPONSE(0x00A1), State.IN_GAME);// 2.6 0xA1, 2.7
	    addPacket(new CM_LEGION_EMBLEM_SEND(0x00A2), State.IN_GAME);// 2.6 0xA2, 2.7
		addPacket(new CM_MOVE(0x00A3), State.IN_GAME);// 2.6 0xA3, 2.7
        addPacket(new CM_CLOSE_DIALOG(0x00A4), State.IN_GAME);// 2.6 0xA4, 2.7
        addPacket(new CM_DIALOG_SELECT(0x00A5), State.IN_GAME);// 2.6 0xA5, 2.7
        addPacket(new CM_BUY_ITEM(0x00A6), State.IN_GAME);// 2.6 0xA6, 2.7
		addPacket(new CM_SHOW_DIALOG(0x00A7), State.IN_GAME);// 2.6 0xA7, 2.7
		addPacket(new CM_SET_NOTE(0x00A9), State.IN_GAME);// 2.6 0xC5, 2.7
        addPacket(new CM_LEGION_TABS(0x00AA), State.IN_GAME);// 2.6 0xAA, 2.7
        addPacket(new CM_CHAT_RECRUIT_GROUP(0x00AC), State.IN_GAME);// 2.6 0xAC, 2.7
        //addPacket(new CM_TWITTER_ADDON(0x00AD), State.IN_GAME);// 2.6 Unknown, 2.7
        addPacket(new CM_LEGION_MODIFY_EMBLEM(0x00AE), State.IN_GAME);// 2.6
        addPacket(new CM_EXCHANGE_ADD_KINAH(0x00B1), State.IN_GAME);// 2.6 0xB1, 2.7
        addPacket(new CM_EXCHANGE_REQUEST(0x00B2), State.IN_GAME);// 2.6 0xB2, 2.7
		addPacket(new CM_EXCHANGE_ADD_ITEM(0x00B3), State.IN_GAME);// 2.6 0xB3, 2.7
        addPacket(new CM_EXCHANGE_CANCEL(0x02B4), State.IN_GAME);// 2.6 0xB4, 2.7
        addPacket(new CM_WINDSTREAM(0x02B5), State.IN_GAME);// 2.6 0xB5, 2.7
        addPacket(new CM_EXCHANGE_LOCK(0x02B6), State.IN_GAME);// 2.6 0xB6, 2.7
		addPacket(new CM_EXCHANGE_OK(0x02B7), State.IN_GAME);// 2.6
		addPacket(new CM_MANASTONE(0x02B9), State.IN_GAME);// 2.6 0xB9, 2.7
        addPacket(new CM_CMOTION(0x02BA), State.IN_GAME);//2.6 0xBA, 2.7
		addPacket(new CM_FIND_GROUP(0x02BC), State.IN_GAME);// 2.6 0xBC, 2.7
		addPacket(new CM_CHARACTER_PASSKEY(0x00C0), State.AUTHED);// 2.6 0xC0, 2.7
        addPacket(new CM_BREAK_WEAPONS(0x01C2), State.IN_GAME);// 2.6 0xC2, 2.7
		addPacket(new CM_DISCONNECT(0x118), State.IN_GAME);// 2.7 Testing
        // Unknown 2.6 - 0xC7 Opcode Only, No Data Gets sent by client after SM_MAIL_SERVICE, every time. Server reply is unknown 95
		addPacket(new CM_VERSION_CHECK(0x00F3), State.CONNECTED);// 2.6 0xF3, 2.7
        addPacket(new CM_REVIVE(0x00F4), State.IN_GAME);// 2.6 0xF4, 2.7
        addPacket(new CM_QUIT(0x00F6), State.AUTHED, State.IN_GAME);//  2.6 0xF6, 2.7
		addPacket(new CM_MAY_QUIT(0x00F7), State.AUTHED, State.IN_GAME);// 2.6 0xF7, 2.7
        addPacket(new CM_LEVEL_READY(0x00F8), State.IN_GAME);// 2.6 0xF8, 2.7
		addPacket(new CM_UI_SETTINGS(0x00F9), State.IN_GAME);// 2.6 0xF9, 2.7
        addPacket(new CM_CHARACTER_EDIT(0x00FA), State.AUTHED);//  2.6 0xFA, 2.7
		addPacket(new CM_ENTER_WORLD(0x00FB), State.AUTHED);// 2.6 0xFB, 2.7
        addPacket(new CM_OBJECT_SEARCH(0x00FE), State.IN_GAME);// 2.6 0xFE, 2.7 Testing
        addPacket(new CM_CUSTOM_SETTINGS(0x00FF), State.IN_GAME);// 2.6 0xFF, 2.7 Testing
    }

	private void init_2_6()
	{
		addPacket(new CM_QUESTIONNAIRE(0x00), State.IN_GAME);//  2.6
		addPacket(new CM_CHARACTER_LIST(0x05), State.AUTHED);// 2.6
		addPacket(new CM_CREATE_CHARACTER(0x0A), State.AUTHED);// 2.6
		addPacket(new CM_TELEPORT_SELECT(0x07), State.IN_GAME);// 2.6
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x04), State.CONNECTED);// 2.6
		addPacket(new CM_START_LOOT(0x09), State.IN_GAME);// 2.6
		addPacket(new CM_LOOT_ITEM(0x0E), State.IN_GAME);// 2.6
		addPacket(new CM_DELETE_CHARACTER(0x0B), State.AUTHED);// 2.6
		addPacket(new CM_RESTORE_CHARACTER(0x08), State.AUTHED);// 2.6
		addPacket(new CM_PLAYER_SEARCH(0x12), State.IN_GAME);// 2.6
		addPacket(new CM_MOVE_ITEM(0x0F), State.IN_GAME);// 2.6
		addPacket(new CM_SPLIT_ITEM(0x0C), State.IN_GAME);// 2.6
		addPacket(new CM_MAIL_SUMMON_ZEPHYR(0x10), State.IN_GAME);// 2.1
		addPacket(new CM_DISCONNECT(0xF0), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_LEGION_UPLOAD_INFO(0x12), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x13), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_BLOCK_ADD(0x14), State.IN_GAME);// 2.1
		addPacket(new CM_BLOCK_DEL(0x15), State.IN_GAME);// 2.1
		addPacket(new CM_FRIEND_STATUS(0x18), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_BLOCKLIST(0x1A), State.IN_GAME);// 2.1
		addPacket(new CM_REPLACE_ITEM(0x1B), State.IN_GAME);//  Testing
		//addPacket(new CM_MAC_ADDRESS2(0x1C), State.IN_GAME);// 2.6
		addPacket(new CM_MACRO_CREATE(0x1D), State.IN_GAME);// 2.1
		addPacket(new CM_CHANGE_CHANNEL(0x1E), State.IN_GAME);// 2.1
		addPacket(new CM_BLOCK_SET_REASON(0x21), State.IN_GAME);// 2.1
		addPacket(new CM_MACRO_DELETE(0x22), State.IN_GAME);// 2.1
		addPacket(new CM_CHECK_NICKNAME(0x23), State.AUTHED);// 2.1
		addPacket(new CM_RECONNECT_AUTH(0x25), State.AUTHED);// 2.1
		addPacket(new CM_SHOW_BRAND(0x27), State.IN_GAME);// 2.1
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x29), State.AUTHED);// 2.6
		addPacket(new CM_GROUP_LOOT(0x2A), State.IN_GAME);//  2.1
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x2B), State.IN_GAME);// 2.1
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x2E), State.IN_GAME);// 2.1
		addPacket(new CM_MAC_ADDRESS(0x2C), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_REPORT_PLAYER(0x2D), State.IN_GAME);//  need to find wrong code
		addPacket(new CM_INSTANCE_CD_REQUEST(0x32), State.IN_GAME);// 2.1
		addPacket(new CM_INGAMESHOP(0x33), State.IN_GAME);// 2.5
		addPacket(new CM_SHOW_MAP(0x37), State.IN_GAME);// 2.6
		addPacket(new CM_NAME_CHANGE(0x36), State.IN_GAME);// 2.6
		addPacket(new CM_SUMMON_EMOTION(0x38), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_ATTACK(0x39), State.IN_GAME);// 2.1
		addPacket(new CM_DREDGION_REQUEST(0x3A), State.IN_GAME); // 2.1
		addPacket(new CM_SUMMON_MOVE(0x3B), State.IN_GAME);// 2.1
		addPacket(new CM_FUSION_WEAPONS(0x3C), State.IN_GAME);// 2.1
		addPacket(new CM_BREAK_WEAPONS(0x3D), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_CASTSPELL(0x3F), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_QUEST(0x42), State.IN_GAME);// 2.1
		addPacket(new CM_PLAY_MOVIE_END(0x43), State.IN_GAME);//  2.1
		addPacket(new CM_ITEM_REMODEL(0x48), State.IN_GAME);// 2.1
		addPacket(new CM_GODSTONE_SOCKET(0x49), State.IN_GAME);//  2.1
		addPacket(new CM_ALLIANCE_GROUP_CHANGE(0x4D), State.IN_GAME);//  1.9
		addPacket(new CM_PLAYER_STATUS_INFO(0x52), State.IN_GAME);//  Testing
		addPacket(new CM_INVITE_TO_GROUP(0x53), State.IN_GAME);// 2.1
		addPacket(new CM_PING_REQUEST(0x55), State.IN_GAME);// 2.1
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x56), State.IN_GAME);// 2.1
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x59), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_FRIENDLIST(0x5C), State.IN_GAME);// 2.1
		addPacket(new CM_FRIEND_ADD(0x5D), State.IN_GAME);//  2.1
		addPacket(new CM_GROUP_DISTRIBUTION(0x5E), State.IN_GAME);//  2.1
		addPacket(new CM_DUEL_REQUEST(0x60), State.IN_GAME);// 2.1
		addPacket(new CM_FRIEND_DEL(0x62), State.IN_GAME);//  2.1
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x64), State.IN_GAME);// 2.1
		addPacket(new CM_PRIVATE_STORE(0x65), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_ITEM(0x66), State.IN_GAME);// 2.1
		addPacket(new CM_BROKER_LIST(0x69), State.IN_GAME);// 2.1
		addPacket(new CM_PRIVATE_STORE_NAME(0x6A), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_COMMAND(0x6B), State.IN_GAME);// 2.1
		addPacket(new CM_BROKER_SEARCH(0x6E), State.IN_GAME);// 2.1
		addPacket(new CM_BROKER_SETTLE_LIST(0x73), State.IN_GAME);//  2.1
		addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x70), State.IN_GAME);//  2.1
		addPacket(new CM_BROKER_REGISTERED(0x6F), State.IN_GAME);// 2.1
		addPacket(new CM_BUY_BROKER_ITEM(0x6C), State.IN_GAME);//  2.1
		addPacket(new CM_REGISTER_BROKER_ITEM(0x6D), State.IN_GAME);//  2.1
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x72), State.IN_GAME);// 2.1
		addPacket(new CM_READ_MAIL(0x74), State.IN_GAME);// 2.1
		addPacket(new CM_SEND_MAIL(0x76), State.IN_GAME);// 2.1
		addPacket(new CM_OPEN_MAIL_WINDOW(0x77), State.IN_GAME);// 2.1
		addPacket(new CM_TITLE_SET(0x7E), State.IN_GAME);// 2.6
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x7A), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_MAIL(0x7B), State.IN_GAME);// 2.1
		addPacket(new CM_CLIENT_COMMAND_LOC(0x7C), State.IN_GAME);// 2.1
		addPacket(new CM_CRAFT(0x7F), State.IN_GAME);// 2.1
		addPacket(new CM_TIME_CHECK(0x81), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_GATHER(0x86), State.IN_GAME);// 2.6
		addPacket(new CM_LEGION_EMBLEM(0x82), State.IN_GAME);// 2.1
		addPacket(new CM_PET(0x85), State.IN_GAME);// 2.6
		addPacket(new CM_OPEN_STATICDOOR(0x87), State.IN_GAME);//  2.6
		addPacket(new CM_PET_MOVE(0x84), State.IN_GAME);// 2.6
		addPacket(new CM_PETITION(0x88), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x89), State.IN_GAME);// 2.1
		addPacket(new CM_TARGET_SELECT(0x8D), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x8E), State.IN_GAME);// 2.1
		addPacket(new CM_SKILL_DEACTIVATE(0x90), State.IN_GAME); // 2.1
		addPacket(new CM_REMOVE_ALTERED_STATE(0x91), State.IN_GAME);// 2.1
		addPacket(new CM_ATTACK(0x92), State.IN_GAME);// 2.1
		addPacket(new CM_CASTSPELL(0x93), State.IN_GAME);//  2.1
		addPacket(new CM_EQUIP_ITEM(0x95), State.IN_GAME);// 2.6
		addPacket(new CM_USE_ITEM(0x94), State.IN_GAME);// 2.6
		addPacket(new CM_EMOTION(0x9E), State.IN_GAME);// 2.6
		addPacket(new CM_PLAYER_LISTENER(0x9B), State.IN_GAME);// 2.6
		addPacket(new CM_EXIT_LOCATION(0x9C), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION_EMBLEM_SEND(0xA2), State.IN_GAME);//  2.6
		addPacket(new CM_PING(0x9F), State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_LEGION(0x9C), State.IN_GAME);// 2.6
		addPacket(new CM_QUESTION_RESPONSE(0xA1), State.IN_GAME);// 2.6
		addPacket(new CM_BUY_ITEM(0xA6), State.IN_GAME);// 2.6
		addPacket(new CM_MOVE(0xA3), State.IN_GAME);// 2.6
		addPacket(new CM_FLIGHT_TELEPORT(0xA0), State.IN_GAME);// 2.6
		addPacket(new CM_DIALOG_SELECT(0xA5), State.IN_GAME);// 2.6
		addPacket(new CM_LEGION_TABS(0xAA), State.IN_GAME);// 2.6
		addPacket(new CM_SHOW_DIALOG(0xA7), State.IN_GAME);// 2.6
		addPacket(new CM_CLOSE_DIALOG(0xA4), State.IN_GAME);// 2.6
		addPacket(new CM_SET_NOTE(0xA8), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION_MODIFY_EMBLEM(0xA9), State.IN_GAME);//  Testing
		//addPacket(new CM_TWITTER_ADDON(0xAC), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_REQUEST(0xAD), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_RECRUIT_GROUP(0xAF), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_ADD_KINAH(0xB0), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_LOCK(0xB1), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_ADD_ITEM(0xB2), State.IN_GAME);// 2.1
		addPacket(new CM_WINDSTREAM(0xB4), State.IN_GAME);// 2.1
		addPacket(new CM_CMOTION(0xB5), State.IN_GAME); // 2.5
		addPacket(new CM_EXCHANGE_OK(0xB6), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_CANCEL(0xB7), State.IN_GAME);// 2.1
		addPacket(new CM_MANASTONE(0xB8), State.IN_GAME);// 2.1
		addPacket(new CM_FIND_GROUP(0xBC), State.IN_GAME);// 2.6
		addPacket(new CM_CHARACTER_PASSKEY(0xC0), State.AUTHED);// 2.6
		addPacket(new CM_QUIT(0xF6), State.AUTHED, State.IN_GAME);//  2,6
		addPacket(new CM_VERSION_CHECK(0xF3), State.CONNECTED);// 2.6
		addPacket(new CM_CHARACTER_EDIT(0xF5), State.AUTHED);//  2.1 testing
		addPacket(new CM_MAY_QUIT(0xF7), State.AUTHED, State.IN_GAME);// 2.6
		addPacket(new CM_REVIVE(0xF4), State.IN_GAME);//  2.6
		addPacket(new CM_UI_SETTINGS(0xF9), State.IN_GAME);// 2.6
		addPacket(new CM_OBJECT_SEARCH(0xFE), State.IN_GAME);// 2.6
		addPacket(new CM_ENTER_WORLD(0xFB), State.AUTHED);// 2.6
		addPacket(new CM_LEVEL_READY(0xF8), State.IN_GAME);// 2.6
		addPacket(new CM_CUSTOM_SETTINGS(0xFF), State.IN_GAME);// 2.6
		}

		private void init_2_5()
		{
		addPacket(new CM_QUESTIONNAIRE(0x03), State.IN_GAME);//  2.1
		addPacket(new CM_CHARACTER_LIST(0x04), State.AUTHED);// 2.1
		addPacket(new CM_CREATE_CHARACTER(0x05), State.AUTHED);// 2.1
		addPacket(new CM_TELEPORT_SELECT(0x06), State.IN_GAME);// 2.1
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x07), State.CONNECTED);// 2.1
		addPacket(new CM_START_LOOT(0x08), State.IN_GAME);// 2.1
		addPacket(new CM_LOOT_ITEM(0x09), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_CHARACTER(0x0A), State.AUTHED);// 2.1
		addPacket(new CM_RESTORE_CHARACTER(0x0B), State.AUTHED);// 2.1
		addPacket(new CM_PLAYER_SEARCH(0x0D), State.IN_GAME);// 2.1
		addPacket(new CM_MOVE_ITEM(0x0E), State.IN_GAME);// 2.1
		addPacket(new CM_SPLIT_ITEM(0x0F), State.IN_GAME);// 2.1
		addPacket(new CM_MAIL_SUMMON_ZEPHYR(0x10), State.IN_GAME);// 2.1
		addPacket(new CM_DISCONNECT(0x11), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_LEGION_UPLOAD_INFO(0x12), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x13), State.IN_GAME);// 2.1 Testing
		addPacket(new CM_BLOCK_ADD(0x14), State.IN_GAME);// 2.1
		addPacket(new CM_BLOCK_DEL(0x15), State.IN_GAME);// 2.1
		addPacket(new CM_FRIEND_STATUS(0x18), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_BLOCKLIST(0x1A), State.IN_GAME);// 2.1
		addPacket(new CM_REPLACE_ITEM(0x1B), State.IN_GAME);//  Testing
		addPacket(new CM_MAC_ADDRESS2(0x1C), State.IN_GAME);// 2.1
		addPacket(new CM_MACRO_CREATE(0x1D), State.IN_GAME);// 2.1
		addPacket(new CM_CHANGE_CHANNEL(0x1E), State.IN_GAME);// 2.1
		addPacket(new CM_BLOCK_SET_REASON(0x21), State.IN_GAME);// 2.1
		addPacket(new CM_MACRO_DELETE(0x22), State.IN_GAME);// 2.1
		addPacket(new CM_CHECK_NICKNAME(0x23), State.AUTHED);// 2.1
		addPacket(new CM_RECONNECT_AUTH(0x25), State.AUTHED);// 2.1
		addPacket(new CM_SHOW_BRAND(0x27), State.IN_GAME);// 2.1
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x28), State.AUTHED);// 2.1
		addPacket(new CM_GROUP_LOOT(0x2A), State.IN_GAME);//  2.1
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x2B), State.IN_GAME);// 2.1
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x2E), State.IN_GAME);// 2.1
		addPacket(new CM_MAC_ADDRESS(0x2F), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.1 Testing
		addPacket(new CM_REPORT_PLAYER(0x31), State.IN_GAME);//  need to find wrong code
		addPacket(new CM_INSTANCE_CD_REQUEST(0x32), State.IN_GAME);// 2.1
		addPacket(new CM_INGAMESHOP(0x33), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_MAP(0x36), State.IN_GAME);// 2.1
		addPacket(new CM_NAME_CHANGE(0x37), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_EMOTION(0x38), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_ATTACK(0x39), State.IN_GAME);// 2.1
		addPacket(new CM_DREDGION_REQUEST(0x3A), State.IN_GAME); // 2.1
		addPacket(new CM_SUMMON_MOVE(0x3B), State.IN_GAME);// 2.1
		addPacket(new CM_FUSION_WEAPONS(0x3C), State.IN_GAME);// 2.1
		addPacket(new CM_BREAK_WEAPONS(0x3D), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_CASTSPELL(0x3F), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_QUEST(0x42), State.IN_GAME);// 2.1
		addPacket(new CM_PLAY_MOVIE_END(0x43), State.IN_GAME);//  2.1
		addPacket(new CM_EXIT_EC(0x46), State.IN_GAME);// 2.5
		addPacket(new CM_ITEM_REMODEL(0x48), State.IN_GAME);// 2.1
		addPacket(new CM_GODSTONE_SOCKET(0x49), State.IN_GAME);//  2.1
		addPacket(new CM_ALLIANCE_GROUP_CHANGE(0x4D), State.IN_GAME);//  1.9
		addPacket(new CM_PLAYER_STATUS_INFO(0x52), State.IN_GAME);//  Testing
		addPacket(new CM_INVITE_TO_GROUP(0x53), State.IN_GAME);// 2.1
		addPacket(new CM_PING_REQUEST(0x55), State.IN_GAME);// 2.1
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x56), State.IN_GAME);// 2.1
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x59), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_FRIENDLIST(0x5C), State.IN_GAME);// 2.1
		addPacket(new CM_FRIEND_ADD(0x5D), State.IN_GAME);//  2.1
		addPacket(new CM_GROUP_DISTRIBUTION(0x5E), State.IN_GAME);//  2.1
		addPacket(new CM_DUEL_REQUEST(0x60), State.IN_GAME);// 2.1
		addPacket(new CM_FRIEND_DEL(0x62), State.IN_GAME);//  2.1
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x64), State.IN_GAME);// 2.1
		addPacket(new CM_PRIVATE_STORE(0x65), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_ITEM(0x66), State.IN_GAME);// 2.1
		addPacket(new CM_BROKER_LIST(0x69), State.IN_GAME);// 2.1
		addPacket(new CM_PRIVATE_STORE_NAME(0x6A), State.IN_GAME);// 2.1
		addPacket(new CM_SUMMON_COMMAND(0x6B), State.IN_GAME);// 2.1
		addPacket(new CM_BROKER_SEARCH(0x6E), State.IN_GAME);// 2.1
		addPacket(new CM_BROKER_SETTLE_LIST(0x73), State.IN_GAME);//  2.1
		addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x70), State.IN_GAME);//  2.1
		addPacket(new CM_BROKER_REGISTERED(0x6F), State.IN_GAME);// 2.1
		addPacket(new CM_BUY_BROKER_ITEM(0x6C), State.IN_GAME);//  2.1
		addPacket(new CM_REGISTER_BROKER_ITEM(0x6D), State.IN_GAME);//  2.1
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x72), State.IN_GAME);// 2.1
		addPacket(new CM_READ_MAIL(0x74), State.IN_GAME);// 2.1
		addPacket(new CM_SEND_MAIL(0x76), State.IN_GAME);// 2.1
		addPacket(new CM_OPEN_MAIL_WINDOW(0x77), State.IN_GAME);// 2.1
		addPacket(new CM_TITLE_SET(0x79), State.IN_GAME);// 2.1
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x7A), State.IN_GAME);// 2.1
		addPacket(new CM_DELETE_MAIL(0x7B), State.IN_GAME);// 2.1
		addPacket(new CM_CLIENT_COMMAND_LOC(0x7C), State.IN_GAME);// 2.1
		addPacket(new CM_CRAFT(0x7F), State.IN_GAME);// 2.1
		addPacket(new CM_TIME_CHECK(0x80), State.CONNECTED, State.AUTHED, State.IN_GAME);// 2.1
		addPacket(new CM_GATHER(0x81), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION_EMBLEM(0x82), State.IN_GAME);// 2.1
		addPacket(new CM_PET(0x84), State.IN_GAME);// 2.1
		addPacket(new CM_OPEN_STATICDOOR(0x85), State.IN_GAME);//  2.1
		addPacket(new CM_PET_MOVE(0x87), State.IN_GAME);// 2.1
		addPacket(new CM_PETITION(0x88), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x89), State.IN_GAME);// 2.1
		addPacket(new CM_TARGET_SELECT(0x8D), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x8E), State.IN_GAME);// 2.1
		addPacket(new CM_SKILL_DEACTIVATE(0x90), State.IN_GAME); // 2.1
		addPacket(new CM_REMOVE_ALTERED_STATE(0x91), State.IN_GAME);// 2.1
		addPacket(new CM_ATTACK(0x92), State.IN_GAME);// 2.1
		addPacket(new CM_CASTSPELL(0x93), State.IN_GAME);//  2.1
		addPacket(new CM_EQUIP_ITEM(0x94), State.IN_GAME);// 2.1
		addPacket(new CM_USE_ITEM(0x97), State.IN_GAME);// 2.1
		addPacket(new CM_EMOTION(0x99), State.IN_GAME);// 2.1
		addPacket(new CM_PLAYER_LISTENER(0x9A), State.IN_GAME);// 2.1
		addPacket(new CM_EXIT_LOCATION(0x9C), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION_EMBLEM_SEND(0x9D), State.IN_GAME);//  2.1
		addPacket(new CM_PING(0x9E), State.AUTHED, State.IN_GAME);// 2.1
		addPacket(new CM_LEGION(0x9F), State.IN_GAME);//  Testing
		addPacket(new CM_QUESTION_RESPONSE(0xA0), State.IN_GAME);// 2.1
		addPacket(new CM_BUY_ITEM(0xA1), State.IN_GAME);// 2.1
		addPacket(new CM_MOVE(0xA2), State.IN_GAME);// 2.1
		addPacket(new CM_FLIGHT_TELEPORT(0xA3), State.IN_GAME);// 2.1
		addPacket(new CM_DIALOG_SELECT(0xA4), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION_TABS(0xA5), State.IN_GAME);// 2.1
		addPacket(new CM_SHOW_DIALOG(0xA6), State.IN_GAME);// 2.1
		addPacket(new CM_CLOSE_DIALOG(0xA7), State.IN_GAME);// 2.1
		addPacket(new CM_SET_NOTE(0xA8), State.IN_GAME);// 2.1
		addPacket(new CM_LEGION_MODIFY_EMBLEM(0xA9), State.IN_GAME);//  Testing
		//addPacket(new CM_TWITTER_ADDON(0xAC), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_REQUEST(0xAD), State.IN_GAME);// 2.1
		addPacket(new CM_CHAT_RECRUIT_GROUP(0xAF), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_ADD_KINAH(0xB0), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_LOCK(0xB1), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_ADD_ITEM(0xB2), State.IN_GAME);// 2.1
		addPacket(new CM_WINDSTREAM(0xB4), State.IN_GAME);// 2.1
		addPacket(new CM_CMOTION(0xB5), State.IN_GAME);//2.5
		addPacket(new CM_EXCHANGE_OK(0xB6), State.IN_GAME);// 2.1
		addPacket(new CM_EXCHANGE_CANCEL(0xB7), State.IN_GAME);// 2.1
		addPacket(new CM_MANASTONE(0xB8), State.IN_GAME);// 2.1
		addPacket(new CM_FIND_GROUP(0xBF), State.IN_GAME);// 2.1
		addPacket(new CM_CHARACTER_PASSKEY(0xC3), State.AUTHED);// 2.1
		addPacket(new CM_QUIT(0xF1), State.AUTHED, State.IN_GAME);//  2,1
		addPacket(new CM_VERSION_CHECK(0xF2), State.CONNECTED);// 2.1
		addPacket(new CM_CHARACTER_EDIT(0xF5), State.AUTHED);//  2.5
		addPacket(new CM_MAY_QUIT(0xF6), State.AUTHED, State.IN_GAME);// 2.1
		addPacket(new CM_REVIVE(0xF7), State.IN_GAME);//  2.1
		addPacket(new CM_UI_SETTINGS(0xF8), State.IN_GAME);// 2.1
		addPacket(new CM_OBJECT_SEARCH(0xF9), State.IN_GAME);// 2.1
		addPacket(new CM_ENTER_WORLD(0xFA), State.AUTHED);// 2.1
		addPacket(new CM_LEVEL_READY(0xFB), State.IN_GAME);// 2.1
		addPacket(new CM_CUSTOM_SETTINGS(0xFE), State.IN_GAME);// 2.1
		}

		private void init_1_9()
		{
		addPacket(new CM_CRAFT(0x00), State.IN_GAME);// 1.9
		addPacket(new CM_CLIENT_COMMAND_LOC(0x01), State.IN_GAME);// 1.9
		addPacket(new CM_RESTORE_CHARACTER(0x04), State.AUTHED);// 1.9
		addPacket(new CM_START_LOOT(0x05), State.IN_GAME);// 1.9
		addPacket(new CM_LOOT_ITEM(0x06), State.IN_GAME);// 1.9
		addPacket(new CM_MOVE_ITEM(0x07), State.IN_GAME);// 1.9
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x08), State.CONNECTED);// 1.9
		addPacket(new CM_CHARACTER_LIST(0x09), State.AUTHED);// 1.9
		addPacket(new CM_CREATE_CHARACTER(0x0A), State.AUTHED);// 1.9
		addPacket(new CM_DELETE_CHARACTER(0x0B), State.AUTHED);// 1.9
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x0C), State.IN_GAME);// testing
		addPacket(new CM_MAIL_SUMMON_ZEPHYR(0x0D), State.IN_GAME);// 1.9
		addPacket(new CM_QUEST_SHARE(0x0F), State.IN_GAME);// 1.9
		addPacket(new CM_SPLIT_ITEM(0x10), State.IN_GAME);// 1.9
		addPacket(new CM_PLAYER_SEARCH(0x12), State.IN_GAME);// 1.9
		addPacket(new CM_LEGION_UPLOAD_INFO(0x13), State.IN_GAME);// testing
		addPacket(new CM_FRIEND_STATUS(0x15), State.IN_GAME);// 1.9
		addPacket(new CM_CHANGE_CHANNEL(0x17), State.IN_GAME);// 1.9
		addPacket(new CM_BLOCK_ADD(0x19), State.IN_GAME);// 1.9
		addPacket(new CM_BLOCK_DEL(0x1A), State.IN_GAME);// 1.9
		addPacket(new CM_SHOW_BLOCKLIST(0x1B), State.IN_GAME);// 1.9
		addPacket(new CM_CHECK_NICKNAME(0x1C), State.AUTHED);// 1.9
		addPacket(new CM_REPLACE_ITEM(0x1D), State.IN_GAME);// testing
		addPacket(new CM_BLOCK_SET_REASON(0x1E), State.IN_GAME);// 1.9
		addPacket(new CM_MAC_ADDRESS2(0x21), State.IN_GAME);// 1.9
		addPacket(new CM_MACRO_CREATE(0x22), State.IN_GAME);// 1.9
		addPacket(new CM_MACRO_DELETE(0x23), State.IN_GAME);// 1.9
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x24), State.IN_GAME);// 1.9
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x25), State.AUTHED);// 1.9
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x27), State.IN_GAME);// 1.9
		addPacket(new CM_SHOW_BRAND(0x28), State.IN_GAME);// 1.9
		addPacket(new CM_RECONNECT_AUTH(0x2A), State.AUTHED);// 1.9
		addPacket(new CM_GROUP_LOOT(0x2B), State.IN_GAME);
		addPacket(new CM_SHOW_MAP(0x2F), State.IN_GAME);// 1.9
		addPacket(new CM_MAC_ADDRESS(0x30), State.CONNECTED, State.AUTHED, State.IN_GAME);// 1.9
		addPacket(new CM_REPORT_PLAYER(0x32), State.IN_GAME);// need to find wrong code
		addPacket(new CM_INSTANCE_CD_REQUEST(0x33), State.IN_GAME);// 1.9
		addPacket(new CM_SUMMON_MOVE(0x34), State.IN_GAME);// 1.9
		addPacket(new CM_SUMMON_EMOTION(0x35), State.IN_GAME);// 1.9
		addPacket(new CM_SUMMON_ATTACK(0x36), State.IN_GAME);// 1.9
		addPacket(new CM_NAME_CHANGE(0x38), State.IN_GAME); // 1.9
		addPacket(new CM_DREDGION_REQUEST(0x3B), State.IN_GAME); // 2.0.0.5
		addPacket(new CM_DELETE_QUEST(0x43), State.IN_GAME);// 1.9
		addPacket(new CM_ITEM_REMODEL(0x45), State.IN_GAME);// 1.9
		addPacket(new CM_GODSTONE_SOCKET(0x46), State.IN_GAME);// 1.9
		addPacket(new CM_INVITE_TO_GROUP(0x4C), State.IN_GAME);// 1.9
		addPacket(new CM_ALLIANCE_GROUP_CHANGE(0x4D), State.IN_GAME);// 1.9
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x4F), State.IN_GAME);// 1.9
		addPacket(new CM_PLAYER_STATUS_INFO(0x53), State.IN_GAME);// 1.9
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x56), State.IN_GAME);// 1.9
		addPacket(new CM_GROUP_DISTRIBUTION(0x57), State.IN_GAME);// 1.9
		addPacket(new CM_PING_REQUEST(0x5A), State.IN_GAME);// 1.9
		addPacket(new CM_DUEL_REQUEST(0x5D), State.IN_GAME);// 1.9
		addPacket(new CM_DELETE_ITEM(0x5F), State.IN_GAME);// 1.9
		addPacket(new CM_SHOW_FRIENDLIST(0x61), State.IN_GAME);// 1.9
		addPacket(new CM_FRIEND_ADD(0x62), State.IN_GAME);// 1.9
		addPacket(new CM_FRIEND_DEL(0x63), State.IN_GAME);// 1.9
		addPacket(new CM_SUMMON_COMMAND(0x64), State.IN_GAME);// 1.9
		addPacket(new CM_BROKER_LIST(0x66), State.IN_GAME);// 1.9
		addPacket(new CM_BROKER_SEARCH(0x67), State.IN_GAME);// 1.9
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x69), State.IN_GAME);// 1.9
		addPacket(new CM_PRIVATE_STORE(0x6A), State.IN_GAME);// 1.9
		addPacket(new CM_PRIVATE_STORE_NAME(0x6B), State.IN_GAME);// 1.9
		addPacket(new CM_BROKER_SETTLE_LIST(0x6C), State.IN_GAME);// 1.9
		addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x6D), State.IN_GAME);// 1.9
		addPacket(new CM_SEND_MAIL(0x6F), State.IN_GAME);// 1.9
		addPacket(new CM_BROKER_REGISTERED(0x70), State.IN_GAME);// 1.9
		addPacket(new CM_BUY_BROKER_ITEM(0x71), State.IN_GAME);// 1.9
		addPacket(new CM_REGISTER_BROKER_ITEM(0x72), State.IN_GAME);// 1.9
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x73), State.IN_GAME);// 1.9
		addPacket(new CM_DELETE_MAIL(0x74), State.IN_GAME);// 1.9
		addPacket(new CM_TITLE_SET(0x76), State.IN_GAME);// 1.9
		addPacket(new CM_OPEN_MAIL_WINDOW(0x78), State.IN_GAME); //1.9
		addPacket(new CM_READ_MAIL(0x79), State.IN_GAME);// 1.9
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x7B), State.IN_GAME);// 1.9
		addPacket(new CM_QUESTIONNAIRE(0x7c), State.IN_GAME); // 1.9
		addPacket(new CM_TELEPORT_SELECT(0x7F), State.IN_GAME);// 1.9
		addPacket(new CM_LEGION_EMBLEM(0x83), State.IN_GAME);// 1.9
		addPacket(new CM_PETITION(0x85), State.IN_GAME);// 1.9
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x86), State.IN_GAME);// 1.9
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x87), State.IN_GAME);// 1.9
		addPacket(new CM_PET_MOVE(0x88), State.IN_GAME);// 2.0
		addPacket(new CM_PET(0x89), State.IN_GAME);// 2.0
		addPacket(new CM_OPEN_STATICDOOR(0x8A), State.IN_GAME);// 1.9
		addPacket(new CM_CASTSPELL(0x8C), State.IN_GAME);// 1.9
		addPacket(new CM_SKILL_DEACTIVATE(0x8D), State.IN_GAME);// 1.9
		addPacket(new CM_REMOVE_ALTERED_STATE(0x8E), State.IN_GAME);// 1.9
		addPacket(new CM_TARGET_SELECT(0x92), State.IN_GAME);// 1.9
		addPacket(new CM_ATTACK(0x93), State.IN_GAME);// 1.9
		addPacket(new CM_EMOTION(0x96), State.IN_GAME);// 1.9
		addPacket(new CM_PING(0x97), State.AUTHED, State.IN_GAME);// 1.9
		addPacket(new CM_USE_ITEM(0x98), State.IN_GAME);// 1.9
		addPacket(new CM_EQUIP_ITEM(0x99), State.IN_GAME);// 1.9
		addPacket(new CM_FLIGHT_TELEPORT(0x9C), State.IN_GAME);// 1.9
		addPacket(new CM_QUESTION_RESPONSE(0x9D), State.IN_GAME);// 1.9
		addPacket(new CM_BUY_ITEM(0x9E), State.IN_GAME);// 1.9
		addPacket(new CM_SHOW_DIALOG(0x9F), State.IN_GAME);// 1.9
		addPacket(new CM_LEGION(0xA0), State.IN_GAME);// 1.9
		addPacket(new CM_EXIT_LOCATION(0xA1), State.IN_GAME);// 2.0
		addPacket(new CM_LEGION_EMBLEM_SEND(0xA2), State.IN_GAME);// 1.9
		addPacket(new CM_MOVE(0xA3), State.IN_GAME);// 1.9
		addPacket(new CM_SET_NOTE(0xA5), State.IN_GAME);// 1.9
		addPacket(new CM_LEGION_MODIFY_EMBLEM(0xA6), State.IN_GAME);// 1.9
		addPacket(new CM_CLOSE_DIALOG(0xA8), State.IN_GAME);// 1.9
		addPacket(new CM_DIALOG_SELECT(0xA9), State.IN_GAME);// 1.9
		addPacket(new CM_LEGION_TABS(0xAA), State.IN_GAME);// 1.9
		addPacket(new CM_EXCHANGE_ADD_KINAH(0xAD), State.IN_GAME);// 1.9
		addPacket(new CM_EXCHANGE_LOCK(0xAE), State.IN_GAME);// 1.9
		addPacket(new CM_EXCHANGE_OK(0xAF), State.IN_GAME);// 1.9
		addPacket(new CM_EXCHANGE_REQUEST(0xB2), State.IN_GAME);// 1.9
		addPacket(new CM_EXCHANGE_ADD_ITEM(0xB3), State.IN_GAME);// 1.9
		addPacket(new CM_MANASTONE(0xB5), State.IN_GAME);// 1.9
		addPacket(new CM_EXCHANGE_CANCEL(0xB8), State.IN_GAME);// 1.9
		addPacket(new CM_WINDSTREAM(0xB9), State.IN_GAME);// 2.0.0.3
		addPacket(new CM_PLAY_MOVIE_END(0xBC), State.IN_GAME);// 1.9
		addPacket(new CM_SUMMON_CASTSPELL(0xC0), State.IN_GAME);// 1.9
		addPacket(new CM_FUSION_WEAPONS(0xC1), State.IN_GAME);// 1.9
		addPacket(new CM_BREAK_WEAPONS(0xC2), State.IN_GAME);// 1.9
		addPacket(new CM_DISCONNECT(0xED), State.IN_GAME);// seems wrong
		addPacket(new CM_QUIT(0xEE), State.AUTHED, State.IN_GAME);// 1.9
		addPacket(new CM_MAY_QUIT(0xEF), State.AUTHED, State.IN_GAME);// 1.9
		addPacket(new CM_VERSION_CHECK(0xF3), State.CONNECTED); // 1.9
		addPacket(new CM_LEVEL_READY(0xF4), State.IN_GAME);// 1.9
		addPacket(new CM_UI_SETTINGS(0xF5), State.IN_GAME);// 1.9
		addPacket(new CM_OBJECT_SEARCH(0xF6), State.IN_GAME);// 1.9
		addPacket(new CM_CUSTOM_SETTINGS(0xF7), State.IN_GAME);// 1.9
		addPacket(new CM_REVIVE(0xF8), State.IN_GAME);// 1.9
		addPacket(new CM_CHARACTER_EDIT(0xF5), State.AUTHED);// 2.5 o.o...
		addPacket(new CM_ENTER_WORLD(0xFB), State.AUTHED); // 1.9
		addPacket(new CM_TIME_CHECK(0xFD), State.CONNECTED, State.AUTHED, State.IN_GAME);// 1.9
		addPacket(new CM_GATHER(0xFE), State.IN_GAME);// 1.9
		// opcode 70 broker sell page
		// opcode 6c broker sold items page

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