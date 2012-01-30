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

package gameserver.network.aion.clientpackets;

import gameserver.configs.main.CustomConfig;
import gameserver.configs.main.GSConfig;
import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.world.World;
import org.apache.log4j.Logger;

/**
 * Packet that reads Whisper chat messages.<br>
 */
public class CM_CHAT_MESSAGE_WHISPER extends AionClientPacket
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_CHAT_MESSAGE_WHISPER.class);

	/**
	 * To whom this message is sent
	 */
	private String				name;

	/**
	 * Message text
	 */
	private String				message;

	/**
	 * Constructs new client packet instance.
	 * @param opcode
	 */
	public CM_CHAT_MESSAGE_WHISPER(int opcode)
	{
		super(opcode);

	}

	/**
	 * Read message
	 */
	@Override
	protected void readImpl()
	{
		name = readS();
		message = readS();
	}

	/**
	 * Print debug info
	 */
	@Override
	protected void runImpl()
	{
		String formatname = Util.convertName(name);

		Player sender = getConnection().getActivePlayer();
		Player receiver = World.getInstance().findPlayer(formatname);

		if(GSConfig.LOG_CHAT)
			log.info(String.format("[MESSAGE] [%s] Whisper To: %s, Message: %s", sender.getName(), formatname, message));

		if(receiver == null || receiver.getFriendList().getStatus()== FriendList.Status.OFFLINE || CustomConfig.FACTIONS_WHISPER_MODE == 0 && (sender.getCommonData().getRace() != receiver.getCommonData().getRace() && (!sender.isGM() && !receiver.isGM())))
		{
			sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(formatname));
		}
		else if(sender.getLevel() < CustomConfig.LEVEL_TO_WHISPER)
		{
			sendPacket(SM_SYSTEM_MESSAGE.LEVEL_NOT_ENOUGH_FOR_WHISPER(String.valueOf(CustomConfig.LEVEL_TO_WHISPER)));
		}
		else if (receiver.getBlockList().contains(sender.getObjectId()))
		{
			sendPacket(SM_SYSTEM_MESSAGE.YOU_ARE_BLOCKED_BY(receiver.getName()));
		}
		else if (!sender.isGM() && !receiver.isWhisperable())
		{
			PacketSendUtility.sendMessage(sender, receiver.getName() + " is on whisper refusal mode now, sorry.");
		}
		else
		{
			if(RestrictionsManager.canChat(sender))
				PacketSendUtility.sendPacket(receiver, new SM_MESSAGE(sender, message, ChatType.WHISPER));
		}
	}
}
