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
import gameserver.services.WeatherService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.WorldMapType;

public class Weather extends AdminCommand
{
	private final static String	RESET = "reset";
	private final static String	COMMAND	= "weather";

	public Weather()
	{
		super(COMMAND);
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		// Check restriction level
		if (admin.getAccessLevel() < AdminConfig.COMMAND_WEATHER)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params.length == 0 || params.length > 2)
		{
			// Syntax :
			// - //weather poeta 0 -> to set clear sky in this region
			// - //weather reset -> to change randomly all weathers in the world

			PacketSendUtility.sendMessage(admin, "syntax //weather <location name> <value>\n//weather reset");
			return;
		}

		String regionName = null;
		int weatherType = -1;

		regionName = new String(params[0]);

		if (params.length == 2)
		{
			try
			{
				weatherType = Integer.parseInt(params[1]);
			}
			catch(NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "weather type parameter need to be an integer [0-8].");
				return;
			}
		}

		if (regionName.equals(RESET))
		{
			WeatherService.getInstance().resetWeather();
			return;
		}

		// Retrieving regionId by name
		WorldMapType region = null;
		for (WorldMapType worldMapType : WorldMapType.values())
		{
			if (worldMapType.name().toLowerCase().equals(regionName.toLowerCase()))
			{
				region = worldMapType;
			}
		}

		if (region != null)
		{
			if (weatherType > -1 && weatherType < 9)
			{
				WeatherService.getInstance().changeRegionWeather(region.getId(), new Integer(weatherType));
			}
			else
			{
				PacketSendUtility.sendMessage(admin, "Weather type must be between 0 and 8");
				return;
			}
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Region " + regionName + " not found");
			return;
		}
	}
}