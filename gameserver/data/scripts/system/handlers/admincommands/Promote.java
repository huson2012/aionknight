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
import gameserver.network.loginserver.LoginServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Promote extends AdminCommand
{

	public Promote()
	{
		super("promote");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PROMOTE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length != 3 )
		{
			PacketSendUtility.sendMessage(admin, "syntax //promote <player name> <accesslevel | membership> <rolemask>");
			return;
		}

		int mask = 0;
		try
		{
			mask = Integer.parseInt(params[2]);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "only numbers allowed for rolemask!");
			return;
		}

		int type = 0;
		if (params[1].toLowerCase().equals("accesslevel"))
		{
			type = 1;
			if (mask > 3 || mask < 0)
			{
				PacketSendUtility.sendMessage(admin, "accesslevel can be 0, 1, 2 or 3");
				return;
			}
		}
		else if (params[1].toLowerCase().equals("membership"))
		{
			type = 2;
			if (mask > 1 || mask < 0)
			{
				PacketSendUtility.sendMessage(admin, "membership can be 0 or 1");
				return;
			}
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "syntax //promote <player name> <accesslevel | membership> <rolemask>");
			return;
		}

		Player player = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (player == null)
		{
			PacketSendUtility.sendMessage(admin, "the specified player is not online.");
			return;
		}
		LoginServer.getInstance().sendLsControlPacket(player.getAcountName(), player.getName(), admin.getName(), mask, type);
	}
}