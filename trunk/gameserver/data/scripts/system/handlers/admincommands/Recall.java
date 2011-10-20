/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.model.Race;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.gameobjects.stats.modifiers.Executor;
import ru.aionknight.gameserver.services.TeleportService;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;
import ru.aionknight.gameserver.world.World;


/**
 * @author ginho1
 */
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