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


package gameserver.utils.chathandlers;

import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.Player;

/**
 * ChatHandler is called every time when player is trying to send a message using chat. ChatHandler can decide whether
 * message should be send later to players (i.e. admin command handler will block it) and can also change the content of
 * the message ( for example censor may put *** in place of vulgar words)
 */
public interface ChatHandler
{
	/**
	 * This method may check content of message and take proper actions based on it. The message can be changed and also
	 * blocked to forwarding to players.
	 * 
	 * @param chatType
	 * @param message
	 * @param sender
	 * @return response {@link ChatHandlerResponse}
	 */
	public ChatHandlerResponse handleChatMessage(ChatType chatType, String message, Player sender);
}
