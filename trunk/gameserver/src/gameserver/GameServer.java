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

package gameserver;

import commons.database.DatabaseFactory;
import commons.database.dao.DAOManager;
import commons.log4j.exceptions.Log4jInitializationError;
import commons.network.NioServer;
import commons.network.ServerCfg;
import commons.ngen.network.Server;
import commons.ngen.network.ServerConfig;
import commons.services.LoggingService;
import commons.utils.AEInfos;
import gameserver.cache.HTMLCache;
import gameserver.configs.Config;
import gameserver.configs.main.GSConfig;
import gameserver.configs.main.TaskManagerConfig;
import gameserver.configs.main.ThreadConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.dao.PlayerDAO;
import gameserver.dataholders.DataManager;
import gameserver.geo.GeoEngine;
import gameserver.model.Race;
import gameserver.model.siege.Influence;
import gameserver.network.aion.GameConnectionFactoryImpl;
import gameserver.network.chatserver.ChatServer;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.rdc.RDCConnectionFactory;
import gameserver.network.rdc.commands.RDCACommandTable;
import gameserver.quest.QuestEngine;
import gameserver.services.*;
import gameserver.spawn.DayNightSpawnManager;
import gameserver.spawn.SpawnEngine;
import gameserver.task.impl.PacketBroadcaster;
import gameserver.utils.*;
import gameserver.utils.chathandlers.ChatHandlers;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class GameServer
{
	private static final Logger log	= Logger.getLogger(GameServer.class);
	private static int ELYOS_COUNT = 0;
	private static int ASMOS_COUNT = 0;
	public static double ELYOS_RATIO = 0.0;
	public static double ASMOS_RATIO = 0.0;
	private static final ReentrantLock lock = new ReentrantLock();
	public static Server nioServer;
	public static Server rdcServer;
	public static String CONFIGURATION_FILE;
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		if(args.length == 0)
			CONFIGURATION_FILE = "./config/gameserver.ini";
		else
		{
			CONFIGURATION_FILE = args[0];
		}
		
		File cfgFile = new File(CONFIGURATION_FILE);
		if(!cfgFile.exists())
			log.fatal("Unable to stat " + CONFIGURATION_FILE + " : No such file.");
		if(!cfgFile.canRead())
			log.fatal("Unable to stat " + CONFIGURATION_FILE + " : Unreadable file (check filesystem permissions)");
		
		cfgFile = null;
		initUtilityServicesAndConfig();

		DataManager.getInstance();

		Util.printSection("IDFactory Manager");
		IDFactory.getInstance();
		
		World.getInstance();
		
		GameServer gs = new GameServer();
		DAOManager.getDAO(PlayerDAO.class).setPlayersOffline(false);
		
		Util.printSection("Geodata Manager");
		log.info("Loading Geodata");
		long startTime = System.currentTimeMillis();
		GeoEngine.getInstance();
		log.info("Loaded in " + (System.currentTimeMillis() - startTime)/1000 + " s");

		NpcShoutsService.getInstance();
		
		Util.printSection("Spawns Manager");
		SpawnEngine.getInstance();
		DayNightSpawnManager.getInstance().notifyChangeMode();
		
		Util.printSection("Roads Manager");
		log.info("Road Service: Initialized");
		RoadService.getInstance();

		Util.printSection("Quest Manager");
		QuestEngine.getInstance();
		QuestEngine.getInstance().load(false);

		Util.printSection("Task Manager");
		PacketBroadcaster.getInstance();
		GameTimeService.getInstance();
		AnnouncementService.getInstance();
		DebugService.getInstance();
		ZoneService.getInstance();
		WeatherService.getInstance();
		DuelService.getInstance();
		MailService.getInstance();
		GroupService.getInstance();
		AllianceService.getInstance();
		BrokerService.getInstance();
		PeriodicSaveService.getInstance();	
		Influence.getInstance();
		DropService.getInstance();
		ExchangeService.getInstance();
		PetitionService.getInstance();
		FlyRingService.getInstance();
		LanguageHandler.getInstance();
		ChatHandlers.getInstance();
		AbyssRankingService.getInstance();
		SiegeService.getInstance();	
		ShieldService.getInstance();		
		HTMLCache.getInstance();
		
		try {
			GameServer.ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
			GameServer.ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
			computeRatios();
		}
		catch (Exception e) { }
		
		Util.printSection("System Manager");
		AEVersions.printFullVersionInfo();
		System.gc();
		AEInfos.printAllInfos();

		Util.printSection("Client Manager");
		log.info("The supported version of the client: " + GSConfig.SERVER_VERSION);
		
		Util.printSection("Log Manager");
		log.info("Server started in " + (System.currentTimeMillis() - start) / 1000 + " sec.");

		gs.startServers();
		GameTimeManager.startClock();

		if(GSConfig.ENABLE_PURCHASE_LIMIT)
			PurchaseLimitService.getInstance().load();

		if(TaskManagerConfig.DEADLOCK_DETECTOR_ENABLED)
		{
			log.info("Deadlock Manager");
			new Thread(new DeadlockDetector(TaskManagerConfig.DEADLOCK_DETECTOR_INTERVAL)).start();
		}

		Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());
		
		if (GSConfig.FACTIONS_RATIO_LIMITED)
		{
			addStartupHook(new StartupHook(){
				@Override
				public void onStartup()
				{
					lock.lock();
					try {
						GameServer.ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
						GameServer.ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
						computeRatios();
					}
					catch (Exception e) { }
					finally
					{
						lock.unlock();
					}
					displayRatios(false);
				}
			});
		}

		onStartup();
		
		Util.printSection("RDC Manager");
		RDCACommandTable.initialize();
	}

	private void startServers()
	{	
		ServerCfg aion = new ServerCfg(NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, "connections", new GameConnectionFactoryImpl());
		

		NioServer nioServer = new NioServer(NetworkConfig.NIO_READ_THREADS + NetworkConfig.NIO_WRITE_THREADS, ThreadPoolManager.getInstance(), aion);
		LoginServer loginServer = LoginServer.getInstance();
		ChatServer chatServer = ChatServer.getInstance();
		
		ServerConfig rdcConfig = new ServerConfig("RDC", NetworkConfig.RDC_BIND_ADDRESS, NetworkConfig.RDC_BIND_PORT, new RDCConnectionFactory(), 1, 1, false, 0, 0, 3, 3, NetworkConfig.NIO_DEBUG);
		rdcServer = new Server(rdcConfig);
		
		loginServer.setNioServer(nioServer);
		chatServer.setNioServer(nioServer);
		
		nioServer.connect();
		loginServer.connect();
		
		if(!GSConfig.DISABLE_CHAT_SERVER)
			chatServer.connect();
		
		if(!GSConfig.DISABLE_RDC_SERVER)
			rdcServer.start();
	}

	private static void initUtilityServicesAndConfig() throws Log4jInitializationError
	{
		Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		LoggingService.init();
		Config.load();
		
		Util.printSection("The Aion-Knight Dev. Team");
		log.info("Main lang: Russian / Eng");
		log.info("Rev type: RC1 <Beta Version>");
		log.info("Dev. page: http://www.aion-knight.ru");
		
		Util.printSection("Connecting to DB");
		DatabaseFactory.init();
		DAOManager.init();
		
		Util.printSection("Thread Manager");
		ThreadConfig.load();
		ThreadPoolManager.getInstance();
	}

	private static Set<StartupHook> startUpHooks = new HashSet<StartupHook>();

	public synchronized static void addStartupHook(StartupHook hook)
	{
		if(startUpHooks != null)
			startUpHooks.add(hook);
		else
			hook.onStartup();
	}

	private synchronized static void onStartup()
	{
		final Set<StartupHook> startupHooks = startUpHooks;

		startUpHooks = null;

		for(StartupHook hook : startupHooks)
			hook.onStartup();
	}

	public interface StartupHook
	{
		public void onStartup();
	}

	public static void updateRatio(Race race, int i)
	{
		lock.lock();
		try {
			switch (race)
			{
				case ASMODIANS:
					GameServer.ASMOS_COUNT += i;
					break;
				case ELYOS:
					GameServer.ELYOS_COUNT += i;
					break;
				default:
					break;
			}
			
			computeRatios();
		}
		catch (Exception e) { }
		finally
		{
			lock.unlock();
		}
		
		displayRatios(true);
	}
	
	private static void computeRatios ()
	{
		if ((GameServer.ASMOS_COUNT <= GSConfig.FACTIONS_RATIO_MINIMUM) && (GameServer.ELYOS_COUNT <= GSConfig.FACTIONS_RATIO_MINIMUM))
		{
			GameServer.ASMOS_RATIO = GameServer.ELYOS_RATIO = 50.0;
		}
		else
		{
			GameServer.ASMOS_RATIO = GameServer.ASMOS_COUNT * 100.0 / (GameServer.ASMOS_COUNT + GameServer.ELYOS_COUNT);
			GameServer.ELYOS_RATIO = GameServer.ELYOS_COUNT * 100.0 / (GameServer.ASMOS_COUNT + GameServer.ELYOS_COUNT);
		}
	}
	
	private static void displayRatios (boolean updated)
	{
		log.info("==== RATIOS "+(updated?"UPDATED ":"")+": E "+String.format("%.1f", GameServer.ELYOS_RATIO)+" % / A "+String.format("%.1f", GameServer.ASMOS_RATIO)+" %");
	}
	
	public static double getRatiosFor (Race race)
	{
		switch (race)
		{
			case ASMODIANS:
				return GameServer.ASMOS_RATIO;
			case ELYOS:
				return GameServer.ELYOS_RATIO;
			default:
				return 0.0;
		}
	}
}