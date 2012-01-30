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

package loginserver.network;

import loginserver.configs.Config;
import loginserver.network.aion.AionConnectionFactoryImpl;
import loginserver.network.gameserver.GsConnectionFactoryImpl;
import loginserver.utils.ThreadPoolManager;
import commons.network.NioServer;
import commons.network.ServerCfg;

public class IOServer
{
    /**
     * NioServer instance that will handle io.
     */
    private final static NioServer instance;

    static
    {
        ServerCfg aion = new ServerCfg(Config.LOGIN_BIND_ADDRESS, Config.LOGIN_PORT, "Aion Connections", new AionConnectionFactoryImpl());

        ServerCfg gs = new ServerCfg(Config.GAME_BIND_ADDRESS, Config.GAME_PORT, "GS Connections", new GsConnectionFactoryImpl());

        instance = new NioServer(Config.NIO_READ_THREADS, ThreadPoolManager.getInstance(), gs, aion);
    }

    /**
     * @return NioServer instance.
     */
    public static NioServer getInstance()
    {
        return instance;
    }
}
