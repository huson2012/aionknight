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
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.ItemId;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Kinah extends AdminCommand
{
	public Kinah()
	{
		super("kinah");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_KINAH)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params == null || params.length < 1 || params.length > 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //kinah <player name> <quantity>");
			return;
		}

		long kinahCount;
		Player receiver;

		if (params.length == 1)
		{
			receiver = admin;
			try
			{
				kinahCount = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Kinah value must be an integer.");
				return;
			}
		}
		else
		{
			receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

			if (receiver == null)
			{
				PacketSendUtility.sendMessage(admin, "Could not find an online player with that name.");
				return;
			}

			try
			{
				kinahCount = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Kinah value must be an integer.");
				return;
			}
		}

		long count = ItemService.addItem(receiver, ItemId.KINAH.value(), kinahCount);

		if (count == 0)
		{
			PacketSendUtility.sendMessage(admin, "Kinah given successfully to player " + receiver.getName());
			PacketSendUtility.sendMessage(receiver, "Admin " + admin.getName() + " gives you some kinah.");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Kinah couldn't be given.");
		}
	}
}
