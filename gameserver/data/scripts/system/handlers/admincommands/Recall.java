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
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Recall extends AdminCommand
{
	public Recall()
	{
		super("recall");
	}

	@Override
	public void executeCommand(final Player admin, final String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_RECALL)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length == 0 || params.length > 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //recall <ELYOS | ASMODIANS | ALL>");
			return;
		}

		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player player)
			{
				if (params[0].equals("ALL"))
				{
					if (!player.equals(admin))
					{
						TeleportService.teleportTo(player, admin.getWorldId(),
						admin.getInstanceId(), admin.getX(), admin.getY(),
						admin.getZ(), admin.getHeading(), 5);
						PacketSendUtility.sendMessage(player, "Teleported by Admin " + admin.getName() + ".");
					}
				}

				if (params[0].equals("ELYOS"))
				{
					if (!player.equals(admin))
					{
						if (player.getCommonData().getRace() == Race.ELYOS)
						{
							TeleportService.teleportTo(player, admin.getWorldId(),
								admin.getInstanceId(), admin.getX(), admin.getY(),
								admin.getZ(), admin.getHeading(), 5);
							PacketSendUtility.sendMessage(player, "Teleported by Admin " + admin.getName() + ".");
						}
					}
				}

				if (params[0].equals("ASMODIANS"))
				{
					if (!player.equals(admin))
					{
						if (player.getCommonData().getRace() == Race.ASMODIANS)
						{
							TeleportService.teleportTo(player, admin.getWorldId(),
							admin.getInstanceId(), admin.getX(), admin.getY(),
							admin.getZ(), admin.getHeading(), 5);
							PacketSendUtility.sendMessage(player, "Teleported by Admin " + admin.getName() + ".");
						}
					}
				}
				return true;
			}
		}, true);
		
		PacketSendUtility.sendMessage(admin, "Player(s) teleported.");
	}
}