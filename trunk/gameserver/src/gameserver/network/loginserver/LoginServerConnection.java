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
 
package gameserver.network.loginserver;

import commons.network.AConnection;
import commons.network.Dispatcher;
import gameserver.network.factories.LsPacketHandlerFactory;
import gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

public class LoginServerConnection extends AConnection
{

	private static final Logger	log	= Logger.getLogger(LoginServerConnection.class);

	public static enum State
	{
		CONNECTED, AUTHED
	}

	private final Deque<LsServerPacket>	sendMsgQueue = new ArrayDeque<LsServerPacket>();

	private State state;
	private LsPacketHandler	lsPacketHandler;

	public LoginServerConnection(SocketChannel sc,Dispatcher d) throws IOException
	{
		super(sc, d);
		LsPacketHandlerFactory lsPacketHandlerFactory = LsPacketHandlerFactory.getInstance();
		this.lsPacketHandler = lsPacketHandlerFactory.getPacketHandler();
		
		state = State.CONNECTED;
		log.info("Connected to LS successfully");

		this.sendPacket(new SM_GS_AUTH());
	}

	@Override
	public boolean processData(ByteBuffer data)
	{
		LsClientPacket pck = lsPacketHandler.handle(data, this);
		log.info("Recived packet: " + pck);

		if(pck != null && pck.read())
			ThreadPoolManager.getInstance().executeLsPacket(pck);

		return true;
	}

	@Override
	protected final boolean writeData(ByteBuffer data)
	{
		synchronized(guard)
		{
			LsServerPacket packet = sendMsgQueue.pollFirst();
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
		LoginServer.getInstance().loginServerDown();
	}

	@Override
	protected final void onServerClose()
	{
		close(true);
	}

	public final void sendPacket(LsServerPacket bp)
	{
		synchronized(guard)
		{

			if(isWriteDisabled())
				return;

			log.info("Sending packet: " + bp);

			sendMsgQueue.addLast(bp);
			enableWriteInterest();
		}
	}

	public final void close(LsServerPacket closePacket, boolean forced)
	{
		synchronized(guard)
		{
			if(isWriteDisabled())
				return;

			log.info("Sending packet: " + closePacket + " and closing connection.");

			pendingClose = true;
			isForcedClosing = forced;
			sendMsgQueue.clear();
			sendMsgQueue.addLast(closePacket);
			enableWriteInterest();
		}
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	@Override
	public String toString()
	{
		return "LS " + getIP();
	}
}