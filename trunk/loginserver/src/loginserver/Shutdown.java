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

import loginserver.network.IOServer;
import loginserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;

public class Shutdown extends Thread
{
    /**
     * Logger for this class
     */
    private static final Logger log = Logger.getLogger(Shutdown.class);

    /**
     * Instance of Shutdown.
     */
    private static final Shutdown instance = new Shutdown();

    /**
     * get the shutdown-hook instance the shutdown-hook instance is created by the first call of this function, but it
     * has to be registrered externaly.
     *
     * @return instance of Shutdown, to be used as shutdown hook
     */
    public static Shutdown getInstance()
    {
        return instance;
    }

    /**
     * this function is called, when a new thread starts if this thread is the thread of getInstance, then this is the
     * shutdown hook and we save all data and disconnect all clients. after this thread ends, the server will completely
     * exit if this is not the thread of getInstance, then this is a countdown thread. we start the countdown, and when
     * we finished it, and it was not aborted, we tell the shutdown-hook why we call exit, and then call exit when the
     * exit status of the server is 1, startServer.sh / startServer.bat will restart the server.
     */
    @Override
    public void run()
    {
        /**
         * Disconnecting all the clients
         */
        try
        {
            IOServer.getInstance().shutdown();
        }
        catch (Throwable t)
        {
            log.error("Can't shutdown I/O Server", t);
        }

        /**
         * Shuting down DB connections
         */
        try
        {
            DatabaseFactory.shutdown();
        }
        catch (Throwable t)
        {
            log.error("Can't shutdown DatabaseFactory", t);
        }

        /**
         * Shuting down threadpools
         */
        try
        {
            ThreadPoolManager.getInstance().shutdown();
        }
        catch (Throwable t)
        {
            log.error("Can't shutdown ThreadPoolManager", t);
        }
    }
}
