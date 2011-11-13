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

import gameserver.model.gameobjects.player.Player;
import gameserver.services.PvPZoneService;
import gameserver.services.PvpService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class PvP extends AdminCommand 
{
    public PvP()
    {
        super("pvp");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < 2) {
             PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
        if (params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax: //pvp <open|close|reset|ap>");
            return;
        }
        if ("open".equalsIgnoreCase(params[0])) {
			if (!PvPZoneService.Spawn(250148, 250148, 730207))
				PacketSendUtility.sendMessage(admin, "PvP Event ist already started.");
			else
				PacketSendUtility.sendMessage(admin, "PvP Event will be started.");
		} 
		else if ("close".equalsIgnoreCase(params[0])) {
			if (!PvPZoneService.Delete())
				PacketSendUtility.sendMessage(admin, "PvP Event has bin not started.");
		} 
		else if ("ap".equalsIgnoreCase(params[0])) {
			if (params.length == 1) {
				PacketSendUtility.sendMessage(admin, "syntax: //pvp ap <on|off>");
			}
			else if ("on".equalsIgnoreCase(params[1])) {
				if (PvpService.getPvpZoneReward())
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was already set.");
				else
				{
					PvpService.setPvpZoneReward(true);
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was already turn on");
				}
			}
			else if ("off".equalsIgnoreCase(params[1])) {
				if (!PvpService.getPvpZoneReward())
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was turned off.");
				else
				{
					PvpService.setPvpZoneReward(true);
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was set off.");
				}
			}
			else {
				PacketSendUtility.sendMessage(admin, "syntax: //pvp ap <on|off>");
			}
		} 
		else if ("reset".equalsIgnoreCase(params[0])) {
			PvPZoneService.AdminReset();
			PacketSendUtility.sendMessage(admin, "PvP Event has bin resetet.");
		} 
		else {
			PacketSendUtility.sendMessage(admin, "Syntax: //pvp <open|close|reset|ap>");
		}
    }
}