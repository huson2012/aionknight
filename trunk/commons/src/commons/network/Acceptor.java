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

package commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * This class represents an <code>Acceptor</code> that will accept sockets<br>
 * connections dispatched by Accept <code>Dispatcher</code>. <code>Acceptor</code> is attachment<br>
 * of <code>ServerSocketChannel</code> <code>SelectionKey</code> registered on Accept <code>Dispatcher</code>
 * <code>Selector</code>.<br>
 * <code>Acceptor</code> will create new <code>AConnection</code> object using
 * <code>ConnectionFactory.create(SocketChannel socket)</code><br>
 * representing accepted socket, register it into one of ReadWrite <code>Dispatcher</code><br>
 * <code>Selector</code> as ready for io read operations.<br>

 * @see commons.network.Dispatcher
 * @see java.nio.channels.ServerSocketChannel
 * @see java.nio.channels.SelectionKey
 * @see java.nio.channels.SocketChannel
 * @see java.nio.channels.Selector
 * @see commons.network.AConnection
 * @see commons.network.ConnectionFactory
 * @see commons.network.NioServer
 */
public class Acceptor
{
	/**
	 * <code>ConnectionFactory</code> that will create new <code>AConnection</code>
	 * 
	 * @see commons.network.ConnectionFactory
	 * @see commons.network.AConnection
	 */
	private final ConnectionFactory	factory;

	/**
	 * <code>NioServer</code> that created this Acceptor.
	 * 
	 * @see commons.network.NioServer
	 */
	private final NioServer	nioServer;

	/**
	 * Constructor that accept <code>ConnectionFactory</code> and <code>NioServer</code> as parameter<br>
	 * 
	 * @param factory
	 *           <code>ConnectionFactory</code> that will be used to<br>
	 * @param nioServer
	 *           <code>NioServer</code> that created this Acceptor object<br>
	 *           creating new <code>AConnection</code> instances.
	 * @see commons.network.ConnectionFactory
	 * @see commons.network.NioServer
	 * @see commons.network.AConnection
	 */
	Acceptor(ConnectionFactory factory, NioServer nioServer)
	{
		this.factory = factory;
		this.nioServer = nioServer;
	}

	/**
	 * Method called by Accept <code>Dispatcher</code> <code>Selector</code> when socket<br>
	 * connects to <code>ServerSocketChannel</code> listening for connections.<br>
	 * New instance of <code>AConnection</code> will be created by <code>ConnectionFactory</code>,<br>
	 * socket representing accepted connection will be register into<br>
	 * one of ReadWrite <code>Dispatchers</code> <code>Selector as ready for io read operations.<br>
	 * 
	 * @param key
	 *           <code>SelectionKey</code> representing <code>ServerSocketChannel</code> that is accepting<br>
	 *           new socket connection.
	 * @throws IOException
	 * @see commons.network.Dispatcher
	 * @see java.nio.channels.ServerSocketChannel
	 * @see java.nio.channels.SelectionKey
	 * @see java.nio.channels.SocketChannel
	 * @see java.nio.channels.Selector
	 * @see commons.network.AConnection
	 * @see commons.network.ConnectionFactory
	 */
	public final void accept(SelectionKey key) throws IOException
	{
		/** For an accept to be pending the channel must be a server socket channel. */
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		/** Accept the connection and make it non-blocking */
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		Dispatcher dispatcher = nioServer.getReadWriteDispatcher();
		factory.create(socketChannel, dispatcher);
	}
}