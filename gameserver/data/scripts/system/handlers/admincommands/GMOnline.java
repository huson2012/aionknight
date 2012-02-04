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
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.FriendList.Status;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class GMOnline extends AdminCommand
{
	public GMOnline()
	{
		super("gmonline");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_GMONLINE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		int GMCount = 0;
		String sGMNames = "";
		for(Player player : World.getInstance().getPlayers())
		{
			if(player.isGM() && !player.isProtectionActive() && player.getFriendList().getStatus() != FriendList.Status.OFFLINE)
			{
				GMCount++;
				sGMNames += player.getName()+" : "+ returnStringStatus(player.getFriendList().getStatus()) +";\n";
			}
		}
		if(GMCount == 0)
		{
			PacketSendUtility.sendMessage(admin, "There is no GM online !");
		}
		else if(GMCount == 1)
		{
			PacketSendUtility.sendMessage(admin, "There is "+String.valueOf( GMCount )+" GM online !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "There are "+String.valueOf( GMCount )+" GMs online !");
		}
		if(GMCount != 0)
			PacketSendUtility.sendMessage(admin, "List : \n"+sGMNames);
	}
	private String returnStringStatus(Status p_status)
	{
		String return_string = "";
		if (p_status == FriendList.Status.ONLINE)
			return_string = "online";
		if (p_status == FriendList.Status.AWAY)
			return_string = "away";
		return return_string;
	}
}