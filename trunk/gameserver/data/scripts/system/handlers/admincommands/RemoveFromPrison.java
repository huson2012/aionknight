/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import java.util.NoSuchElementException;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.configs.main.CustomConfig;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.services.PunishmentService;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.Util;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;
import ru.aionknight.gameserver.world.World;


/**
 * @author lord_rex
 * Command: //rprison <player name>
 * This command removes a player from prison.
 * 
 */
public class RemoveFromPrison extends AdminCommand
{

	public RemoveFromPrison()
	{
		super("rprison");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PRISON)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params.length == 0 || params.length > 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //rprison <player name>");
			return;
		}

		try
		{
			Player playerFromPrison = World.getInstance().findPlayer(Util.convertName(params[0]));

			if (playerFromPrison != null)
			{
				PunishmentService.setIsInPrison(playerFromPrison, false, 0);
				if (CustomConfig.CHANNEL_ALL_ENABLED)
					playerFromPrison.unbanFromWorld();
				PacketSendUtility.sendMessage(admin, "Player " + playerFromPrison.getName() + " has been removed from prison.");
			}
		}
		catch (NoSuchElementException nsee)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //rprison <player name>");
		}
		catch (Exception e)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //rprison <player name>");
		}
	}
}
