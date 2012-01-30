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

package loginserver.network.gameserver;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import commons.network.AConnection;
import commons.network.ConnectionFactory;
import commons.network.Dispatcher;

/**
 * ConnectionFactory implementation that will be creating GsConnections
 */
public class GsConnectionFactoryImpl implements ConnectionFactory
{
    /**
     * Create a new {@link commons.network.AConnection AConnection} instance.<br>
     *
     * @param socket     that new {@link commons.network.AConnection AConnection} instance will represent.<br>
     * @param dispatcher to wich new connection will be registered.<br>
     * @return a new instance of {@link commons.network.AConnection AConnection}<br>
     * @throws IOException
     * @see commons.network.AConnection
     * @see commons.network.Dispatcher
     */
    @Override
    public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException
    {
        return new GsConnection(socket, dispatcher);
    }
}
