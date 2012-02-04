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
import gameserver.model.gameobjects.state.CreatureSeeState;
import gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class See extends AdminCommand
{
	public See()
	{
		super("see");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_SEE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}
		if (params.length == 1)
		{
			int see;
			try 
			{
				see = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Syntax: //see <0|1|2|20>");
				return;
			}

			Player target = null;
		
			if (admin.getTarget() != null && admin.getTarget() instanceof Player)
				target = (Player)admin.getTarget();
			else
				target = admin;
		
			CreatureSeeState seeState;
				
			switch(see)
			{
				case 1:
					seeState = CreatureSeeState.SEARCH1;
					break;
				case 2:
					seeState = CreatureSeeState.SEARCH2;
					break;
				case 20:
					seeState = CreatureSeeState.SEARCH20;
					break;
				default:
					seeState = CreatureSeeState.NORMAL;
					break;
			}
				
			//reset see state
			for(CreatureSeeState saw : CreatureSeeState.values())
			{
				if (target.isInSeeState(saw))
					target.unsetSeeState(saw);
			}
				
			target.setSeeState(seeState);

			PacketSendUtility.broadcastPacket(target, new SM_PLAYER_STATE(target), true);
			PacketSendUtility.sendMessage(target, "Your SeeState level was changed to "+see);
			
		}
		else
			PacketSendUtility.sendMessage(admin, "Syntax: //see <0|1|2|20>");
	}
}