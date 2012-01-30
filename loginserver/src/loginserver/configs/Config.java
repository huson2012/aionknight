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

package loginserver.configs;

import java.util.Properties;
import loginserver.utils.Util;
import org.apache.log4j.Logger;
import commons.configuration.ConfigurableProcessor;
import commons.configuration.Property;
import commons.utils.PropertiesUtils;

public class Config
{
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(Config.class);

    /**
     * Login Server port
     */
    @Property(key = "loginserver.network.client.port", defaultValue = "2106")
    public static int LOGIN_PORT;

    /**
     * Login Server bind ip
     */
    @Property(key = "loginserver.network.client.host", defaultValue = "*")
    public static String LOGIN_BIND_ADDRESS;

    /**
     * Login Server port
     */
    @Property(key = "loginserver.network.gameserver.port", defaultValue = "9014")
    public static int GAME_PORT;

    /**
     * Login Server bind ip
     */
    @Property(key = "loginserver.network.gameserver.host", defaultValue = "*")
    public static String GAME_BIND_ADDRESS;

    /**
     * Number of trys of login before ban
     */
    @Property(key = "loginserver.network.client.logintrybeforeban", defaultValue = "5")
    public static int LOGIN_TRY_BEFORE_BAN;

    /**
     * Ban time in minutes
     */
    @Property(key = "loginserver.network.client.bantimeforbruteforcing", defaultValue = "15")
    public static int WRONG_LOGIN_BAN_TIME;

    /**
     * Number of Threads that will handle io read (>= 0)
     */
    @Property(key = "loginserver.network.nio.threads.read", defaultValue = "0")
    public static int NIO_READ_THREADS;

    /**
     * Number of Threads that will handle io write (>= 0)
     */
    @Property(key = "loginserver.network.nio.threads.write", defaultValue = "0")
    public static int NIO_WRITE_THREADS;

    /**
     * Should server automaticly create accounts for users or not?
     */
    @Property(key = "loginserver.accounts.autocreate", defaultValue = "true")
    public static boolean ACCOUNT_AUTO_CREATION;

    /**
     * Flood controller
     */
    @Property(key = "loginserver.floodcontrol.maxconnection", defaultValue = "10")
    public static int FLOOD_CONTROLLER_MAX_CONNECTION;

    @Property(key = "loginserver.floodcontrol.interval", defaultValue = "5")
    public static int FLOOD_CONTROLLER_INTERVAL;

    @Property(key = "loginserver.floodcontrol.exceptions", defaultValue = "127.0.0.1")
    public static String FLOOD_CONTROLLER_EXCEPTIONS;

    /**
     * Load configs from files.
     */
    public static void load()
    {
        try
        {
            Util.printSection("Network");
            String network = "./config";
            Properties[] props = PropertiesUtils.loadAllFromDirectory(network);

            ConfigurableProcessor.process(Config.class, props);
            log.info("Read: " + network + "/network.ini");
            log.info("Read: " + network + "/database.ini");
            log.info("Read: " + network + "/floodcontroller.ini");
        }
        catch (Exception e)
        {
            log.fatal("Can't read loginserver configuration", e);
            throw new Error("Can't read loginserver configuration", e);
        }
    }
}