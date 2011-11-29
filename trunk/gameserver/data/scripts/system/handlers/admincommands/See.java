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