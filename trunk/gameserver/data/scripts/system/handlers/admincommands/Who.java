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

import java.util.Collection;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

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

		PacketSendUtility.sendMessage(admin, "List players :");

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