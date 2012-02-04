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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class WorldUnban extends AdminCommand
{
	public WorldUnban()
	{
		super("wunban");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		String syntax = "Syntax: //wunban <player>";

		if (!CustomConfig.CHANNEL_ALL_ENABLED)
		{
			PacketSendUtility.sendMessage(admin, "<There is no such admin command: " + getCommandName() + ">");
			return;
		}

		if (admin.getAccessLevel() < AdminConfig.COMMAND_WORLDBAN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		String param = null;
		if (params.length>=1)
		{
			param = params[0];
		}
		
		Player player = parsePlayerParameter(param, admin, syntax);
		if (player==null)
		{
			return;
		}

		if (!player.isBannedFromWorld())
		{
			PacketSendUtility.sendMessage(admin, "Player "+player.getName()+" is not banned from the chat channels");
		}
		else
		{
			player.unbanFromWorld();
			PacketSendUtility.sendSysMessage(player, "You are no longer banned from the chat channels");
			PacketSendUtility.sendMessage(admin, "Player "+player.getName()+" is not banned from the chat channels");
		}
	}
}