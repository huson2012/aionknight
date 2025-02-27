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
import gameserver.configs.main.SiegeConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.SiegeLocation;
import gameserver.model.siege.SiegeRace;
import gameserver.model.siege.SiegeType;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;

public class Fortress extends AdminCommand
{
	/**
	 * Constructor
	 */
	public Fortress()
	{
		super("fortress");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (SiegeConfig.SIEGE_ENABLED == false)
		{
			PacketSendUtility.sendMessage(admin, "Siege system is currently disabled.");
			return;
		}

		if (admin.getAccessLevel() < AdminConfig.COMMAND_SIEGE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params == null || params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, "syntax //fortress <capture | list>");
			return;
		}

		String cmd = params[0].toLowerCase();
		if (("capture").startsWith(cmd))
		{
			processCapture(admin, params);
		}
		else if (("list").startsWith(cmd))
		{
			processList(admin, params);
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "syntax //fortress <capture | list>");
			return;
		}
	}
	
	private void processList(Player admin, String[] params)
	{
		String msg = "[Siege Locations]\n";

		for(SiegeLocation loc : SiegeService.getInstance().getSiegeLocations().values())
		{
			if(loc.getSiegeType() == SiegeType.ARTIFACT || loc.getSiegeType() == SiegeType.FORTRESS)
			{
				if(params.length > 1)
				{
					String filter = params[1].toLowerCase();

					if(filter.startsWith("fortress") && loc.getSiegeType() == SiegeType.ARTIFACT)
						continue;

					if(filter.startsWith("artifact") && loc.getSiegeType() == SiegeType.FORTRESS)
						continue;
				}

				msg += "- " + loc.getSiegeType().name() +
					" (" + String.valueOf(loc.getLocationId()) +
					") - " + loc.getRace().name() + " (" +
					String.valueOf(loc.getLegionId()) +
					")\n";

				if (msg.length() > 500)
				{
					PacketSendUtility.sendMessage(admin, msg);
					msg = "";
				}
			}
		}
		PacketSendUtility.sendMessage(admin, msg);
	}

	private void processCapture(Player admin, String[] params)
	{
		if (params.length < 3 || params.length > 4)
		{
			PacketSendUtility.sendMessage(admin, "//fortress <location id> <race> [legion id]");
			return;
		}

		int locationId;
		try
		{
			locationId = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "<location id> must be an integer.");
			return;
		}

		SiegeLocation sLoc = SiegeService.getInstance().getSiegeLocation(locationId);

		if (sLoc == null)
		{
			PacketSendUtility.sendMessage(admin, "<location id> does not exist: " + locationId);
			return;
		}

		SiegeRace race = SiegeRace.BALAUR;

		final String raceName = params[2].toLowerCase();
		if (raceName.startsWith(("ely")))
		{
			race = SiegeRace.ELYOS;
		}
		else if (raceName.startsWith("asmo"))
		{
			race = SiegeRace.ASMODIANS;
		}
		else if (raceName.startsWith("balaur"))
		{
			race = SiegeRace.BALAUR;
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "<race> must be: elyos, asmos, or balaur.");
			return;
		}
		
		int legionId = 0;
		if (params.length == 4)
		{
			try
			{
				legionId = Integer.parseInt(params[3]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "[legion id] must be an integer.");
				return;
			}
		}

		PacketSendUtility.sendMessage(admin, "[Admin Capture]\n - Location ID: " + locationId +
			"\n - Race: " + race.toString() + "\n - Legion ID: " + legionId + "\n");

		SiegeService.getInstance().capture(locationId, race, legionId);
		SiegeService.getInstance().clearFortress(locationId);

		final SiegeRace siegeRace = race;
		final int fortressId = locationId;

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				SiegeService.getInstance().spawnLocation(fortressId, siegeRace, "PEACE");
			}
		}, 5000);
	}
}