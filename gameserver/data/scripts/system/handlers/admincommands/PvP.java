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