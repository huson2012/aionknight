/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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

	private final Deque<LsServerPacket>	sendMsgQueue	= new ArrayDeque<LsServerPacket>();

	private State						state;
	private LsPacketHandler				lsPacketHandler;

	public LoginServerConnection(SocketChannel sc,Dispatcher d) throws IOException
	{
		super(sc, d);
		LsPacketHandlerFactory lsPacketHandlerFactory = LsPacketHandlerFactory.getInstance();
		this.lsPacketHandler = lsPacketHandlerFactory.getPacketHandler();
		
		state = State.CONNECTED;
		log.info("Connected to LoginServer successfully");

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

			log.info("Sending packet: " + closePacket + " and closing connection after that.");

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
		return "LoginServer " + getIP();
	}
}