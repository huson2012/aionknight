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
import gameserver.services.PunishmentService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import java.util.NoSuchElementException;

public class SendToPrison extends AdminCommand
{
	public SendToPrison()
	{
		super("sprison");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PRISON)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params.length == 0 || params.length != 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //sprison <player name> <time delay>");
			return;
		}

		try
		{
			Player playerToPrison = World.getInstance().findPlayer(Util.convertName(params[0]));
			int delay = Integer.parseInt(params[1]);

			if (playerToPrison != null)
			{
				PunishmentService.setIsInPrison(playerToPrison, true, delay);
				if (CustomConfig.CHANNEL_ALL_ENABLED)
					playerToPrison.banFromWorld(admin.getName(), "you have been sent to prison", 0);
				PacketSendUtility.sendMessage(admin, "Player " + playerToPrison.getName() + " has been sent to prison for "
					+ delay + ".");
			}
		}
		
		catch (NoSuchElementException nsee)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
		}
		
		catch (Exception e)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
		}
	}
}