package ru.aionknight.loginserver;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.commons.database.dao.DAOManager;
import org.openaion.commons.services.LoggingService;
import org.openaion.commons.utils.AEInfos;
import org.openaion.commons.utils.ExitCode;

import ru.aionknight.loginserver.configs.Config;
import ru.aionknight.loginserver.controller.BannedIpController;
import ru.aionknight.loginserver.network.IOServer;
import ru.aionknight.loginserver.network.ncrypt.KeyGen;
import ru.aionknight.loginserver.utils.DeadLockDetector;
import ru.aionknight.loginserver.utils.ThreadPoolManager;
import ru.aionknight.loginserver.utils.Util;


/**
 * @author -Nemesiss-
 */
public class LoginServer
{
    /**
     * Logger for this class.
     */
    private static final Logger	log = Logger.getLogger(LoginServer.class);

    /**
     * @param args
     */
    public static void main(String[] args)
    {
    	long start = System.currentTimeMillis();
    	
        LoggingService.init();

		Config.load();

		Util.printSection("DataBase");
		DatabaseFactory.init("./config/network/database.properties");
		DAOManager.init();

        /** Start deadlock detector that will restart server if deadlock happened */
        new DeadLockDetector(60, DeadLockDetector.RESTART).start();
        ThreadPoolManager.getInstance();


        /**
         * Initialize Key Generator
         */
        try
        {
        	Util.printSection("KeyGen");
            KeyGen.init();
        }
        catch (Exception e)
        {
            log.fatal("Failed initializing Key Generator. Reason: " + e.getMessage(), e);
            System.exit(ExitCode.CODE_ERROR);
        }

        Util.printSection("GSTable");
        GameServerTable.load();
        Util.printSection("BannedIP");
        BannedIpController.load();

        // DONE! flood protector
        // TODO! brute force protector

        Util.printSection("IOServer");
        IOServer.getInstance().connect();
        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        Util.printSection("System");
        AEInfos.printAllInfos();
        
        Util.printSection("LoginServerLog");
        log.info("Login Server started in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
    }
}
