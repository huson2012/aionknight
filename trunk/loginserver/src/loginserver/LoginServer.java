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
    private static final Logger log = Logger.getLogger(LoginServer.class);

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
            log.fatal("Failed key generator. Reason: " + e.getMessage(), e);
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
        log.info("Server load at " + (System.currentTimeMillis() - start) / 1000 + " sec.");
    }
}
