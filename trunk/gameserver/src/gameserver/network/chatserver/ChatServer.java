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