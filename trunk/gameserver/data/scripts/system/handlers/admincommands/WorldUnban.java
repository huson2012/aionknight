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