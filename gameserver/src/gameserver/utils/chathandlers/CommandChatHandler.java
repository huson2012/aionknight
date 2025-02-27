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

import gameserver.configs.main.CustomConfig;
import gameserver.model.ChatType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This chat handler is responsible for handling admin commands, starting with //, and user commands starting with .
 */
public class CommandChatHandler implements ChatHandler
{
	private static final Logger	log	= Logger.getLogger(CommandChatHandler.class);
    private static final Pattern COMPILE = Pattern.compile(" ");

    private Map<String, AdminCommand>	adminCommands = new FastMap<String, AdminCommand>();
	private Map<String, UserCommand>    userCommands  = new FastMap<String, UserCommand>();

	public CommandChatHandler () { }

	void registerAdminCommand(AdminCommand command)
	{
		if(command == null)
			throw new NullPointerException("Command instance cannot be null");

		String commandName = command.getCommandName();

		AdminCommand old = adminCommands.put(commandName, command);
		
		if(old != null)
		{
			log.warn("Overriding handler for command " + commandName + " from " + old.getClass().getName() + " to "
				+ command.getClass().getName());
		}
	}

	void registerUserCommand(UserCommand command)
	{
		if(command == null)
			throw new NullPointerException("Command instance cannot be null");

		String commandName = command.getCommandName();

		UserCommand old = userCommands.put(commandName, command);
		
		if(old != null)
		{
			log.warn("Overriding handler for command " + commandName + " from " + old.getClass().getName() + " to "
				+ command.getClass().getName());
		}
	}

	@Override
	public ChatHandlerResponse handleChatMessage(ChatType chatType, String message, Player sender)
	{
		if(!message.startsWith("//") && !message.startsWith("."))
		{
			if(CustomConfig.CHANNEL_ALL_ENABLED && sender.CHAT_FIX_WORLD_CHANNEL != Player.CHAT_NOT_FIXED)
			{
				CustomChannel.sendMessageOnWorld(sender, message, sender.CHAT_FIX_WORLD_CHANNEL);
				return ChatHandlerResponse.BLOCKED_MESSAGE;
			}
			else
			{
				return new ChatHandlerResponse(false, message);
			}
		}
		else if(message.startsWith("."))
		{
			String[] commandAndParams = COMPILE.split(message, 2);
			String command = commandAndParams[0].substring(1);
			
			UserCommand usrc = userCommands.get(command);
			if (usrc == null)
			{
				PacketSendUtility.sendMessage(sender, LanguageHandler.translate(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST));
				return ChatHandlerResponse.BLOCKED_MESSAGE;
			}
			
			String params = "";
			if (commandAndParams.length > 1)
			{
				params = commandAndParams[1];
			}
			
			usrc.executeCommand(sender, params);
			return ChatHandlerResponse.BLOCKED_MESSAGE;
		}
		else
		{
			String[] commandAndParams = COMPILE.split(message, 2);

			String command = commandAndParams[0].substring(2);
			AdminCommand admc = adminCommands.get(command);

			if (sender.getAccessLevel() == 0)
				log.info("[ADMIN COMMAND] > [Name: " + sender.getName() + "]: The player has tried to use the command without have the rights :");

			if (sender.getTarget() != null && sender.getTarget() instanceof Creature)
			{
				Creature target = (Creature) sender.getTarget();

				log.info("[ADMIN COMMAND] > [Name: " + sender.getName() + "][Target : " + target.getName() + "]: " + message);
			}
			else
				log.info("[ADMIN COMMAND] > [Name: " + sender.getName() + "]: " + message);

			if(admc == null)
			{
				PacketSendUtility.sendMessage(sender, "<There is no such admin command: " + command + ">");
				return ChatHandlerResponse.BLOCKED_MESSAGE;
			}

			String[] params = new String[] {};
			if(commandAndParams.length > 1)
				params = commandAndParams[1].split(" ", admc.getSplitSize());

			admc.executeCommand(sender, params);
			return ChatHandlerResponse.BLOCKED_MESSAGE;
		}
	}

	void clearHandlers()
	{
		this.adminCommands.clear();
		this.userCommands.clear();
	}

	public int getAdminCommandsCount()
	{
		return this.adminCommands.size();
	}
	
	public int getUserCommandsCount()
	{
		return this.userCommands.size();
	}	
}
