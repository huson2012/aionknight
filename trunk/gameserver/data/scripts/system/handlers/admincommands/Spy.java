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

import gameserver.model.gameobjects.player.Player;
import gameserver.model.legion.Legion;
import gameserver.services.LegionService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Spy extends AdminCommand
{
	public Spy()
	{
		super("spy");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < 2)
		{
			PacketSendUtility.sendMessage(admin, "Not authorized");
			return;
		}

		if(params.length != 2)
		{
			syntax(admin);
			return;
		}

		if(params[1].startsWith("L"))
		{
			try
			{
				int legionId = Integer.parseInt(params[1].replace("L", ""));
				Legion legion = LegionService.getInstance().getLegion(legionId);
				if(legion == null)
					throw new Exception("no such legion.");
				
				if(params[0].equals("start"))
				{
					if(!admin.spyedLegions.contains(legionId))
						admin.spyedLegions.add(legionId);
				}
				else if(params[0].equals("stop"))
				{
					if(admin.spyedLegions.contains(legionId))
						admin.spyedLegions.remove(new Integer(legionId));
				}
				else
					syntax(admin);
			}
			catch(Exception e)
			{
				PacketSendUtility.sendMessage(admin, "no such legion.");
				return;
			}
		}
		else if(params[1].startsWith("G"))
		{
			Player target = World.getInstance().findPlayer(params[1].substring(1));
			if(target == null)
			{
				PacketSendUtility.sendMessage(admin, "target player not found.");
				return;
			}
			if(target.getPlayerGroup() == null)
			{
				PacketSendUtility.sendMessage(admin, "target player not in group.");
				return;
			}
			
			if(params[0].equals("start"))
			{
				if(!admin.spyedGroups.contains(target.getPlayerGroup().getGroupId()))
					admin.spyedGroups.add(target.getPlayerGroup().getGroupId());
			}
			else if(params[0].equals("stop"))
			{
				if(admin.spyedGroups.contains(target.getPlayerGroup().getGroupId()))
					admin.spyedGroups.remove(new Integer(target.getPlayerGroup().getGroupId()));
			}
			else
				syntax(admin);
			
		}

	}

	private void syntax(Player admin)
	{
		PacketSendUtility.sendMessage(admin, "Syntax: //spy <start|stop> <legionName>");
	}
}