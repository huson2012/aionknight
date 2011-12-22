/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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