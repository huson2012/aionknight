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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Movie extends AdminCommand
{
	public Movie()
	{
		super("movie");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVIE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		int movieId = 0;
		int type = 0;
		VisibleObject target = admin.getTarget();

		if (target == null || !(target instanceof Player))
		{
			target = admin;
		}
		if (params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			return;
		}
		if (params.length == 1)
		{
			try
			{
				movieId = Integer.valueOf(params[0]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(0, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}
		
		else if (params.length == 2)
		{
			try
			{
				type = Integer.valueOf(params[0]);
				movieId = Integer.valueOf(params[1]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(type, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}
	}
}