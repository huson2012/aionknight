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
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import java.util.Collection;

public class Who extends AdminCommand
{
	public Who()
	{
		super("who");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_WHO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		Collection<Player> players = World.getInstance().getPlayers();
		PacketSendUtility.sendMessage(admin, "List players: ");

		for(Player player : players)
		{
			if (params != null && params.length > 0)
			{
				String cmd = params[0].toLowerCase();

				if (("ely").startsWith(cmd))
				{
					if(player.getCommonData().getRace() == Race.ASMODIANS)
						continue;
				}

				if (("asmo").startsWith(cmd))
				{
					if(player.getCommonData().getRace() == Race.ELYOS)
						continue;
				}

				if (("member").startsWith(cmd) || ("premium").startsWith(cmd))
				{
					if(player.getPlayerAccount().getMembership() == 0)
						continue;
				}
			}			

			PacketSendUtility.sendMessage(admin, "Char: " + player.getName() + " - Race: " +  player.getCommonData().getRace().name() + " - Acc: " +  player.getAcountName());
		}		
	}
}