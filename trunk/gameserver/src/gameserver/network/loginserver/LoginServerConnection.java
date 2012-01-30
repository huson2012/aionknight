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