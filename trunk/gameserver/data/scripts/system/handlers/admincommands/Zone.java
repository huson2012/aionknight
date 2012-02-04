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