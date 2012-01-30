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

package gameserver.network.chatserver;

import commons.network.Dispatcher;
import commons.network.NioServer;
import gameserver.configs.network.NetworkConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_AUTH;
import gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_LOGOUT;
import gameserver.network.factories.CsPacketHandlerFactory;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import java.nio.channels.SocketChannel;

public class ChatServer
{
	private static final Logger log = Logger.getLogger(ChatServer.class);
	private ChatServerConnection chatServer;
	private NioServer nioServer;
	private boolean	serverShutdown = false;
	
	public static final ChatServer getInstance()
	{
		return SingletonHolder.instance;
	}

	private ChatServer()
	{
	}
	
	public void setNioServer(NioServer nioServer)
	{
		this.nioServer = nioServer;
	}

	/**
	 * @return
	 */
	public ChatServerConnection connect()
	{
		SocketChannel sc;
		for(;;)
		{
			chatServer = null;
			log.info("Connecting to CS: " + NetworkConfig.CHAT_ADDRESS);
			try
			{
				sc = SocketChannel.open(NetworkConfig.CHAT_ADDRESS);
				sc.configureBlocking(false);
				Dispatcher d = nioServer.getReadWriteDispatcher();
				CsPacketHandlerFactory csPacketHandlerFactory = new CsPacketHandlerFactory();
				chatServer = new ChatServerConnection(sc, d, csPacketHandlerFactory.getPacketHandler());
				return chatServer;
			}
			catch(Exception e)
			{
				log.info("Cant connect to ChatServer: " + e.getMessage());
			}
			try
			{
				/**
				 * 10s sleep
				 */
				Thread.sleep(10 * 1000);
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 * This method is called when we lost connection to ChatServer.
	 */
	public void chatServerDown()
	{
		log.warn("Connection with ChatServer lost...");

		chatServer = null;

		if(!serverShutdown)
		{
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					connect();
				}
			}, 5000);
		}
	}

	public void sendPlayerLoginRequst(Player player)
	{
		if(chatServer != null)
			chatServer.sendPacket(new SM_CS_PLAYER_AUTH(player.getObjectId(), player.getAcountName()));
	}

	public void sendPlayerLogout(Player player)
	{
		if(chatServer != null)
			chatServer.sendPacket(new SM_CS_PLAYER_LOGOUT(player.getObjectId()));
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ChatServer instance = new ChatServer();
	}
}