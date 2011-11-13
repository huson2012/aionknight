package loginserver.network.gameserver;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import commons.network.AConnection;
import commons.network.ConnectionFactory;
import commons.network.Dispatcher;


/**
 * ConnectionFactory implementation that will be creating GsConnections
 * 
 * @author -Nemesiss-
 * 
 */
public class GsConnectionFactoryImpl implements ConnectionFactory
{
	/**
	 * Create a new {@link commons.network.AConnection AConnection} instance.<br>
	 * 
	 * @param socket
	 *           that new {@link commons.network.AConnection AConnection} instance will represent.<br>
	 * @param dispatcher
	 *           to wich new connection will be registered.<br>
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
