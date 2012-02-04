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
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Announce extends AdminCommand
{
	public Announce()
	{
		super("announce");
	}

	@Override
	public int getSplitSize()
	{
		return 2;
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{

		if (admin.getAccessLevel() < AdminConfig.COMMAND_ANNOUNCE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params == null || params.length != 2)
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //announce <anonymous|name> <message>");
			return;
		}

		String message = "";

		if (("anonymous").startsWith(params[0].toLowerCase()))
		{
			message += "Announce: ";
		}
		
		else if (("name").startsWith(params[0].toLowerCase()))
		{
			if(CustomConfig.GMTAG_DISPLAY)
			{
				if(admin.getAccessLevel() == 1 )
				{
					message += CustomConfig.GM_LEVEL1.trim();
				}
				
				else if (admin.getAccessLevel() == 2 )
				{
					message += CustomConfig.GM_LEVEL2.trim();
				}
				
				else if (admin.getAccessLevel() == 3 )
				{
					message += CustomConfig.GM_LEVEL3.trim();
				}
			}

			message += admin.getName() + ": ";
		}
		
		else
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //announce <anonymous|name> <message>");
			return;
		}
		
		message += params[1];
		final String _message = message;

		World.getInstance().doOnAllPlayers(new Executor<Player> () 
		{
			@Override
			public boolean run(Player player)
			{
				PacketSendUtility.sendSysMessage(player, _message);
				return true;
			}
		});
	}
}