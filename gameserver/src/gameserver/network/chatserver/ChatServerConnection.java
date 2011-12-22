/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package gameserver.network.chatserver;

import commons.network.AConnection;
import commons.network.Dispatcher;
import gameserver.network.chatserver.serverpackets.SM_CS_AUTH;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

public class ChatServerConnection extends AConnection
{
	private static final Logger	log	= Logger.getLogger(ChatServerConnection.class);

	/**
	 * Possible states of CsConnection
	 */
	public static enum State
	{
		/**
		 * chat server just connected
		 */
		CONNECTED,
		/**
		 * chat server is authenticated
		 */
		AUTHED
	}

	/**
	 * Server Packet "to send" Queue
	 */
	private final Deque<CsServerPacket>	sendMsgQueue	= new ArrayDeque<CsServerPacket>();

	/**
	 * Current state of this connection
	 */
	private State						state;
	private ChatServer					chatServer;
	private CsPacketHandler				csPacketHandler;

	/**
	 * @param sc
	 * @param d
	 * @throws IOException
	 */

	public ChatServerConnection(SocketChannel sc, Dispatcher d, CsPacketHandler csPacketHandler) throws IOException
	{
		super(sc, d);
		this.chatServer = ChatServer.getInstance();
		this.csPacketHandler = csPacketHandler;

		state = State.CONNECTED;
		log.info("Connected to ChatServer!");

		this.sendPacket(new SM_CS_AUTH());
	}

	@Override
	public boolean processData(ByteBuffer data)
	{
		CsClientPacket pck = csPacketHandler.handle(data, this);
		log.info("recived packet: " + pck);

		/**
		 * Execute packet only if packet exist (!= null) and read was ok.
		 */
		if(pck != null && pck.read())
			ThreadPoolManager.getInstance().executeLsPacket(pck);

		return true;
	}

	@Override
	protected final boolean writeData(ByteBuffer data)
	{
		synchronized(guard)
		{
			CsServerPacket packet = sendMsgQueue.pollFirst();
			if(packet == null)
				return false;

			packet.write(this, data);
			return true;
		}
	}

	@Override
	protected final long getDisconnectionDelay()
	{
		return 0;
	}

	@Override
	protected final void onDisconnect()
	{
		chatServer.chatServerDown();
	}

	@Override
	protected final void onServerClose()
	{
		// TODO send close packet to chat server
		close(/** packet, */true);
	}

	/**
	 * @param bp
	 */
	public final void sendPacket(CsServerPacket bp)
	{
		synchronized(guard)
		{
			/**
			 * Connection is already closed or waiting for last (close packet) to be sent
			 */
			if(isWriteDisabled())
				return;

			log.info("sending packet: " + bp);

			sendMsgQueue.addLast(bp);
			enableWriteInterest();
		}
	}

	/**
	 * 
	 * @param closePacket
	 * @param forced
	 */
	public final void close(CsServerPacket closePacket, boolean forced)
	{
		synchronized(guard)
		{
			if(isWriteDisabled())
				return;

			log.info("sending packet: " + closePacket + " and closing connection after that.");

			pendingClose = true;
			isForcedClosing = forced;
			sendMsgQueue.clear();
			sendMsgQueue.addLast(closePacket);
			enableWriteInterest();
		}
	}

	/**
	 * @return
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	@Override
	public String toString()
	{
		return "ChatServer " + getIP();
	}
}