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

package gameserver.services;

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerPunishmentsDAO;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapType;
import java.util.concurrent.Future;

public class PunishmentService
{

	/**
	 * This method will handle moving or removing a player from prison
	 * @param player
	 * @param state
	 * @param delayInMinutes
	 */
	public static void setIsInPrison(Player player, boolean state, long delayInMinutes)
	{
		stopPrisonTask(player, false);
		if(state)
		{
			long prisonTimer = player.getPrisonTimer();
			if(delayInMinutes > 0)
			{
				prisonTimer = delayInMinutes * 60000L;
				schedulePrisonTask(player, prisonTimer);
				PacketSendUtility.sendMessage(player, "You are in prison for " + delayInMinutes + " minutes.\nIf you disconnect, the countdown will be stopped.");
			}
			
			player.setStartPrison(System.currentTimeMillis());
			TeleportService.teleportToPrison(player);
			DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, 1);
		}
		else
		{
			PacketSendUtility.sendMessage(player, "You have been removed from prison!");
			player.setPrisonTimer(0);

			TeleportService.moveToBindLocation(player, true);

			DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player);
		}
	}

	/**
	 * This method will stop the prison task
	 * @param playerObjId
	 */
	public static void stopPrisonTask(Player player, boolean save)
	{
		Future<?> prisonTask = player.getController().getTask(TaskId.PRISON);
		if(prisonTask != null)
		{
			if(save)
			{
				long delay = player.getPrisonTimer();
				if(delay < 0)
					delay = 0;
				player.setPrisonTimer(delay);
			}
			player.getController().cancelTask(TaskId.PRISON);
		}
	}

	/**
	 * This method will update the prison status
	 * @param player
	 */
	public static void updatePrisonStatus(Player player)
	{
		if(player.isInPrison())
		{
			long prisonTimer = player.getPrisonTimer();
			if(prisonTimer > 0)
			{
				schedulePrisonTask(player, prisonTimer);
				int timeInPrison = Math.round(prisonTimer / 60000);
				
				if (timeInPrison <= 0)
					timeInPrison = 1;
					
				PacketSendUtility.sendMessage(player, "You will be in prison for "
					+ timeInPrison + " minute" + (timeInPrison > 1 ? "s" : "") + '.');
					
				player.setStartPrison(System.currentTimeMillis());
			}
			if (player.getWorldId() != WorldMapType.PRISON.getId())
				TeleportService.teleportToPrison(player);
		}
	}

	/**
	 * This method will schedule a prison task
	 * @param player
	 * @param prisonTimer
	 */
	private static void schedulePrisonTask(final Player player, long prisonTimer)
	{
		player.setPrisonTimer(prisonTimer);
		player.getController().addTask(TaskId.PRISON, ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				setIsInPrison(player, false, 0);
				player.unbanFromWorld();
			}
		}, prisonTimer));
	}
}
