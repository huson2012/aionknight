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

package gameserver;

import commons.utils.ExitCode;
import commons.utils.concurrent.RunnableStatsManager;
import commons.utils.concurrent.RunnableStatsManager.SortBy;
import gameserver.configs.main.ShutdownConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.loginserver.LoginServer;
import gameserver.services.PeriodicSaveService;
import gameserver.services.PlayerService;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class ShutdownHook extends Thread
{
	private static final Logger	log	= Logger.getLogger(ShutdownHook.class);

	public static ShutdownHook getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	@Override
	public void run()
	{
		if(ShutdownConfig.HOOK_MODE == 1)
		{
			shutdownHook(ShutdownConfig.HOOK_DELAY, ShutdownConfig.ANNOUNCE_INTERVAL, ShutdownMode.SHUTDOWN);
		}
		else if(ShutdownConfig.HOOK_MODE == 2)
		{
			shutdownHook(ShutdownConfig.HOOK_DELAY, ShutdownConfig.ANNOUNCE_INTERVAL, ShutdownMode.RESTART);
		}
		
		GameServer.nioServer.close();
	}

	public static enum ShutdownMode
	{
		NONE("terminating"),
		SHUTDOWN("shutting down"),
		RESTART("restarting");

		private final String	text;

		private ShutdownMode(String text)
		{
			this.text = text;
		}

		public String getText()
		{
			return text;
		}
	}

	private void sendShutdownMessage(final int seconds)
	{
		try
		{
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{					
					if(player != null && player.getClientConnection() != null && player.isSpawned())
					{
						player.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.SERVER_SHUTDOWN(seconds));
					}

					return true;
				}
			}, true);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	private void sendShutdownStatus(final boolean status)
	{
		try
		{
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if(player != null && player.getClientConnection() != null)
						player.getController().setInShutdownProgress(status);
					return true;
				}
			}, true);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
	}

	private void shutdownHook(int duration, int interval, ShutdownMode mode)
	{
		LoginServer.getInstance().gameServerDisconnected();
		
		for(int i = duration; i >= interval; i -= interval)
		{
			try
			{
				if(World.getInstance().getPlayersCount() > 0)
				{
					log.info("Runtime is " + mode.getText() + " in " + i + " seconds.");
					sendShutdownMessage(i);
					sendShutdownStatus(ShutdownConfig.SAFE_REBOOT);
				}
				else
				{
					log.info("Runtime is " + mode.getText() + " now ...");
					break; // fast exit.
				}

				if(i > interval)
				{
					sleep(interval * 1000);
				}
				else
				{
					sleep(i * 1000);
				}
			}
			catch(InterruptedException e)
			{
				return;
			}
		}
		
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			
			@Override
			public boolean run(Player activePlayer)
			{
				try
				{
					PlayerService.playerLoggedOut(activePlayer);					
				}
				catch(Exception e)
				{
					log.error("Error while saving player " + e.getMessage());
				}
				return true;
			}
		}, true);
		
		log.info("All players are disconnected...");
		
		RunnableStatsManager.dumpClassStats(SortBy.AVG);
		PeriodicSaveService.getInstance().onShutdown();
		GameTimeManager.saveTime();
		ThreadPoolManager.getInstance().shutdown();

		if(mode == ShutdownMode.RESTART)
			Runtime.getRuntime().halt(ExitCode.CODE_RESTART);
		else
			Runtime.getRuntime().halt(ExitCode.CODE_NORMAL);

		log.info("Runtime is " + mode.getText() + " now...");
	}

	public void doShutdown(int delay, int announceInterval, ShutdownMode mode)
	{
		shutdownHook(delay, announceInterval, mode);
	}

	private static final class SingletonHolder
	{
		private static final ShutdownHook INSTANCE = new ShutdownHook();
	}
}