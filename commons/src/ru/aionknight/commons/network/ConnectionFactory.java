package ru.aionknight.commons.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * This interface defines a factory for connection implementations.<br>
 * It is used by the class {@link ru.aionknight.commons.network.Acceptor Acceptor} to create actual connection
 * implementations.<br>
 * 
 * @author -Nemesiss-
 * @see ru.aionknight.commons.network.Acceptor
 * 
 */
public interface ConnectionFactory
{
	/**
	 * Create a new {@link ru.aionknight.commons.network.AConnection AConnection} instance.<br>
	 * 
	 * @param socket
	 *            that new {@link ru.aionknight.commons.network.AConnection AConnection} instance will represent.<br>
	 * @param dispatcher
	 *            to wich new connection will be registered.<br>
	 * @return a new instance of {@link ru.aionknight.commons.network.AConnection AConnection}<br>
	 * @throws IOException
	 * @see ru.aionknight.commons.network.AConnection
	 * @see ru.aionknight.commons.network.Dispatcher
	 */
	public AConnection create(SocketChannel socket, Dispatcher dispatcher) throws IOException;
}
