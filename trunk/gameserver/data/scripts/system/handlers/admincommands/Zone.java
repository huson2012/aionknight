/**
 * This file is part of Aion-Knight Dev. Team <Aion-Knight.org>.
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
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.zone.FlightZoneInstance;
import gameserver.world.zone.ZoneInstance;

public class Zone extends AdminCommand
{
	public Zone()
	{
		super("zone");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ZONE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length == 0)
		{
			ZoneInstance zoneInstance = admin.getZoneInstance();
			if (zoneInstance == null)
			{
				PacketSendUtility.sendMessage(admin, "You are out of any zone");
			}
			else
			{
				String zoneName = zoneInstance.getTemplate().getName().name();
				PacketSendUtility.sendMessage(admin, "You are in zone: " + zoneName);
			}
			if (ZoneService.getInstance().mapHasFightZones(admin.getWorldId()))
			{
				FlightZoneInstance currentFlightZoneName = ZoneService.getInstance().findFlightZoneInCurrentMap(admin.getPosition());
				if (currentFlightZoneName != null)
				{
					PacketSendUtility.sendMessage(admin, "You are in flightzone: "+currentFlightZoneName.getTemplate().getName());
				}
				else
					PacketSendUtility.sendMessage(admin, "You are out of any flightzone");
			}
			else
				PacketSendUtility.sendMessage(admin, "No flight zones in the map");
		}
		else if ("refresh".equalsIgnoreCase(params[0]))
		{
			ZoneService.getInstance().findZoneInCurrentMap(admin);
		}
	}
}