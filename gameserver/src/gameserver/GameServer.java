/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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
		
		/**
		 * Название и путь к файлу конфигурации игрового сервера
		 */
		if(args.length == 0)
			CONFIGURATION_FILE = "./config/gameserver.ini";
		else
		{
			CONFIGURATION_FILE = args[0];
		}
		
		File cfgFile = new File(CONFIGURATION_FILE);
		
		/**
		 * Сообщение, выводимое в консоль в случае отсутствия файла конфигруации
		 */
		if(!cfgFile.exists())
		log.fatal("Unable to stat " + CONFIGURATION_FILE + " : No such file.");
		
		/**
		 * Сообщение, выводимое в консоль в случаее краха файла конфигруации
		 */			
		if(!cfgFile.canRead())
		log.fatal("Unable to stat " + CONFIGURATION_FILE + " : Unreadable file (check filesystem permissions)");
		
		cfgFile = null;
		initUtilityServicesAndConfig();
		
		GameServer gs = new GameServer();
		DAOManager.getDAO(PlayerDAO.class).setPlayersOffline(false);
		
		/**
		 * Секционная загрузка данных. Логирование результатов загрузки сервера.
		 */
		Util.printSection("IDFactory Manager");
		IDFactory.getInstance();
		DataManager.getInstance();
		
		Util.printSection("Spawns Manager");
		DayNightSpawnManager.getInstance().notifyChangeMode();
		SpawnEngine.getInstance();
		
		Util.printSection("Zone Manager");
		World.getInstance();
		ZoneService.getInstance();
		GameTimeService.getInstance();
		WeatherService.getInstance();
		
		Util.printSection("Quest Manager");
		QuestEngine.getInstance();
		QuestEngine.getInstance().load(false);
		
		Util.printSection("Roads Manager");
		RoadService.getInstance();
		log.info("Moves between locations: Ok");
		
		Util.printSection("Brokers Manager");
		BrokerService.getInstance();

		Util.printSection("Siege Manager");
		SiegeService.getInstance();	
		ShieldService.getInstance();
		GroupService.getInstance();
		AllianceService.getInstance();
		
		Util.printSection("Abyss Ranking Manager");
		AbyssRankingService.getInstance();
		
		Util.printSection("HTML Manager");
		HTMLCache.getInstance();
		
		Util.printSection("Geodata Manager");
		log.info("Reading *.geo files...");
		long startTime = System.currentTimeMillis();
		GeoEngine.getInstance();
		log.info("Loaded in " + (System.currentTimeMillis() - startTime)/1000 + " s");

		Util.printSection("Task Manager");
		PacketBroadcaster.getInstance();
		AnnouncementService.getInstance();
		DropService.getInstance();
		ExchangeService.getInstance();
		FlyRingService.getInstance();
		LanguageHandler.getInstance();
		ChatHandlers.getInstance();
		NpcShoutsService.getInstance();
		PetitionService.getInstance();
		DuelService.getInstance();
		MailService.getInstance();
		Influence.getInstance();
		PeriodicSaveService.getInstance();	
		
		try {
			GameServer.ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
			GameServer.ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
			computeRatios();
		}
		catch (Exception e) { }
		
		/**
		 * Итоговая информация о загрузке сервера. Вывод информации о состоянии памяти, текущей версии сервера,
		 * версии поддерживаемого клиента. Логирования состояния на момент старта сервера.
		 */
		Util.printSection("System Manager");
		AEVersions.printFullVersionInfo();
		System.gc(); AEInfos.printAllInfos();

		Util.printSection("Debug System Manager");
		log.info("Debug Service: Initialized");
		DebugService.getInstance();
		
		Util.printSection("Client Manager");
		log.info("The supported version of client: " + GSConfig.SERVER_VERSION);

		
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
		
		Util.printSection("Developers info");
		log.info("Main lang: Russian / Eng");
		log.info("Family: Aion-Knight 2.7 (Beta version)");
		log.info("Support: http://www.aion-knight.ru");
		
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