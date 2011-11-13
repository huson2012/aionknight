/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package loginserver;

import loginserver.configs.Config;
import loginserver.controller.BannedIpController;
import loginserver.network.IOServer;
import loginserver.network.ncrypt.KeyGen;
import loginserver.utils.DeadLockDetector;
import loginserver.utils.ThreadPoolManager;
import loginserver.utils.Util;
import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;
import commons.database.dao.DAOManager;
import commons.services.LoggingService;
import commons.utils.AEInfos;
import commons.utils.ExitCode;

public class LoginServer
{
    private static final Logger	log = Logger.getLogger(LoginServer.class);
    public static void main(String[] args)
    {
    	long start = System.currentTimeMillis();
    	
        LoggingService.init();
		Config.load();

		Util.printSection("DB Manager");
		DatabaseFactory.init("./config/loginserver.ini");
		DAOManager.init();

        new DeadLockDetector(60, DeadLockDetector.RESTART).start();
        ThreadPoolManager.getInstance();

        try
        {
        	Util.printSection("Key Manager");
            KeyGen.init();
        }
        catch (Exception e)
        {
            log.fatal("Failed initializing Key Generator. Reason: " + e.getMessage(), e);
            System.exit(ExitCode.CODE_ERROR);
        }

        Util.printSection("GS Manager");
        GameServerTable.load();
		
        Util.printSection("Ban Manager");
        BannedIpController.load();

        Util.printSection("I/O Manager");
        IOServer.getInstance().connect();
        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        Util.printSection("System Manager");
        AEInfos.printAllInfos();
        
        Util.printSection("Log Manager");
        log.info("Server started in " + (System.currentTimeMillis() - start) / 1000 + " sec.");
    }
}