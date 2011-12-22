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

package gameserver.network.aion;

import commons.network.AConnection;
import commons.network.Dispatcher;
import commons.utils.concurrent.RunnableStatsManager;
import gameserver.configs.main.CustomConfig;
import gameserver.configs.network.FloodConfig;
import gameserver.controllers.FloodController;
import gameserver.model.account.Account;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.Crypt;
import gameserver.network.aion.serverpackets.SM_KEY;
import gameserver.network.factories.AionPacketHandlerFactory;
import gameserver.network.loginserver.LoginServer;
import gameserver.services.PlayerService;
import gameserver.task.FIFORunnableQueue;
import gameserver.utils.ThreadPoolManager;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;

public class AionConnection extends AConnection
{
	/**
	 * Logger для этого класса.
	 */
	private static final Logger	log	= Logger.getLogger(AionConnection.class);

	/**
	 * Возможные состояния AionConnection
	 */
	public static enum State
	{
		/**
		 * клиент просто подключите
		 */
		CONNECTED,
		/**
		 * клиент аутентифицируется
		 */
		AUTHED,
		/**
		 * клиент вошел мира.
		 */
		IN_GAME
	}

	/**
	 * Сервер Пакет "отправить" Очереди
	 */
	private final FastList<AionServerPacket> sendMsgQueue = new FastList<AionServerPacket>();

	/**
	 * Состояние соединения
	 */
	private State state;

	/**
	 * AionClient-аутентификации путем перехода к GameServer id учетной записи.
	 */
	private Account	account;

	/**
	 * Crypt, которая будет шифровать/расшифровывать пакеты.
	 */
	private final Crypt	crypt = new Crypt ();

	/**
	 * активный Игрок, владеющий этой связи играет [вошел игры]
	 */
	private Player activePlayer;
	private String lastPlayerName = "";

	private AionPacketHandler aionPacketHandler;
	private long lastPingTimeMS;
	
	private int nbInvalidPackets = 0;
	private final static int MAX_INVALID_PACKETS = 3;

	/**
	 * Конструктор
	 * 
	 * @param sc
	 * @param d
	 * @throws IOException
	 */

	public AionConnection(SocketChannel sc, Dispatcher d) throws IOException
	{
		super(sc, d);

		AionPacketHandlerFactory aionPacketHandlerFactory = AionPacketHandlerFactory.getInstance();
		this.aionPacketHandler = aionPacketHandlerFactory.getPacketHandler();

		state = State.CONNECTED;

		String ip = getIP();
		log.debug("connection from: " + ip);
		
		/**
		 * Анти-Спам
		 */
		List<String> IPsException = Arrays.asList(FloodConfig.FLOOD_CONTROLLER_EXCEPTIONS.split(","));
		
		if (!FloodController.exist(ip) || !FloodController.checkFlood(ip) || IPsException.contains(ip))
		{
			if (!FloodController.exist(ip))
				FloodController.addIP(ip);
			else
				FloodController.addConnection(ip);
			
			log.info("connection from: " + ip + "[" + FloodController.getConnection(ip) + "]");

			/** Send SM_KEY packet */
			sendPacket(new SM_KEY());
		}
	}

	/**
	 * Enable crypt key - generate random key that will be used to encrypt second server packet [first one is
	 * unencrypted] and decrypt client packets. This method is called from SM_KEY server packet, that packet sends key
	 * to aion client.
	 * 
	 * @return "false key" that should by used by aion client to encrypt/decrypt packets.
	 */
	public final int enableCryptKey()
	{
		return crypt.enableKey();
	}

	/**
	 * Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
	 * 
	 * @param data
	 * @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
	 */
	@Override
	protected final boolean processData(ByteBuffer data)
	{
		try
		{
			if(!crypt.decrypt(data))
			{
				nbInvalidPackets++;
				log.warn("["+nbInvalidPackets+"/"+MAX_INVALID_PACKETS+"] decrypt fail, skipping client packet...");
				if (nbInvalidPackets>=MAX_INVALID_PACKETS)
				{
					log.error("reached MAX_INVALID_PACKETS, closing client connection (wrong client version ?)");
					return false;
				}
				return true;
			}
		}
		catch(Exception ex)
		{
			log.error("Exception caught during decrypt: " + ex.getMessage());
			return false;
		}
		
		AionClientPacket pck = aionPacketHandler.handle(data, this);

		if(state == State.IN_GAME && activePlayer == null)
		{
			log.warn("CHECKPOINT: Skipping packet processing of " + pck.getPacketName() + " for player " + lastPlayerName);
			return false;
		}
		
		/**
		 * Execute packet only if packet exist (!= null) and read was ok.
		 */
		if(pck != null && pck.read())
			getPacketQueue().execute(pck);

		return true;
	}

	private FIFORunnableQueue<Runnable> _packetQueue;
	
	public FIFORunnableQueue<Runnable> getPacketQueue()
	{
		if (_packetQueue == null)
			_packetQueue = new FIFORunnableQueue<Runnable>() {};
		
		return _packetQueue;
	}

	/**
	 * This method will be called by Dispatcher, and will be repeated till return false.
	 * 
	 * @param data
	 * @return True if data was written to buffer, False indicating that there are not any more data to write.
	 */
	@Override
	protected final boolean writeData(ByteBuffer data)
	{
		synchronized(guard)
		{
			final long begin = System.nanoTime();
			if (sendMsgQueue.isEmpty() || this == null)
				return false;
			AionServerPacket packet = sendMsgQueue.removeFirst();
			try
			{
				packet.write(this, data);
				return true;
			}
			finally
			{
				RunnableStatsManager.handleStats(packet.getClass(), "runImpl()", System.nanoTime() - begin);
			}

		}
	}

	/**
	 * This method is called by Dispatcher when connection is ready to be closed.
	 * 
	 * @return time in ms after witch onDisconnect() method will be called. Always return 0.
	 */
	@Override
	protected final long getDisconnectionDelay()
	{
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onDisconnect()
	{
		/**
		 * Client starts authentication procedure
		 */
		if(getAccount() != null)
			LoginServer.getInstance().aionClientDisconnected(getAccount().getId());
		if(getActivePlayer() != null)
		{
			final Player player = getActivePlayer();
			
			if(player.getController().isInShutdownProgress())
				PlayerService.playerLoggedOut(player);
			
			// prevent ctrl+alt+del / close window exploit
			else
			{
				//prevent exploit when players increase their speed to max and DC themselves
				player.getFlyController().endFly();
				player.getController().stopMoving();
				
				ThreadPoolManager.getInstance().schedule(new Runnable(){
						@Override
						public void run()
						{
							PlayerService.playerLoggedOut(player);
						}
					}, CustomConfig.DISCONNECT_DELAY * 1000);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void onServerClose()
	{
		// TODO mb some packet should be send to client before closing?
		close(/** packet, */true);
	}

	/**
	 * Encrypt packet.
	 * 
	 * @param buf
	 */
	public final void encrypt(ByteBuffer buf)
	{
		crypt.encrypt(buf);
	}

	/**
	 * Sends AionServerPacket to this client.
	 * 
	 * @param bp
	 *           AionServerPacket to be sent.
	 */
	public final void sendPacket(AionServerPacket bp)
	{
		synchronized(guard)
		{
			/**
			 * Connection is already closed or waiting for last (close packet) to be sent
			 */
			if(isWriteDisabled())
				return;

			sendMsgQueue.addLast(bp);
			enableWriteInterest();
		}
	}

	/**
	 * Its guaranted that closePacket will be sent before closing connection, but all past and future packets wont.
	 * Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be called to clear all other
	 * things. forced means that server shouldn't wait with removing this connection.
	 * 
	 * @param closePacket
	 *           Packet that will be send before closing.
	 * @param forced
	 *           have no effect in this implementation.
	 */
	public final void close(AionServerPacket closePacket, boolean forced)
	{
		synchronized(guard)
		{
			if(isWriteDisabled())
				return;

			log.debug("sending packet: " + closePacket + " and closing connection after that.");

			pendingClose = true;
			isForcedClosing = forced;
			sendMsgQueue.clear();
			sendMsgQueue.addLast(closePacket);
			enableWriteInterest();
		}
		final AionConnection ptr = this;
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			
			@Override
			public void run()
			{
				try
				{
					ptr.finalize();
				}
				catch(Throwable t)
				{
					log.error("Cannot destroy connection !", t);
				}
			}
		}, forced ? 0 : 500);
	}

	/**
	 * Current state of this connection
	 * 
	 * @return state
	 */
	public final State getState()
	{
		return state;
	}

	/**
	 * Sets the state of this connection
	 * 
	 * @param state
	 *           state of this connection
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * Returns account object associated with this connection
	 * 
	 * @return account object associated with this connection
	 */
	public Account getAccount()
	{
		return account;
	}

	/**
	 * Sets account object associated with this connection
	 * 
	 * @param account
	 *           account object associated with this connection
	 */
	public void setAccount(Account account)
	{
		this.account = account;
	}

	/**
	 * Sets Active player to new value. Update connection state to correct value.
	 * 
	 * @param player
	 * @return True if active player was set to new value.
	 */
	public boolean setActivePlayer(Player player)
	{
		if(activePlayer != null && player != null)
			return false;
		activePlayer = player;

		if(activePlayer == null)
			state = State.AUTHED;
		else
			state = State.IN_GAME;
		
		if(activePlayer != null)
			lastPlayerName = player.getName();
			
		return true;
	}

	/**
	 * Return active player or null.
	 * 
	 * @return active player or null.
	 */
	public Player getActivePlayer()
	{
		return activePlayer;
	}

	/**
	 * @return the lastPingTimeMS
	 */
	public long getLastPingTimeMS()
	{
		return lastPingTimeMS;
	}

	/**
	 * @param lastPingTimeMS the lastPingTimeMS to set
	 */
	public void setLastPingTimeMS(long lastPingTimeMS)
	{
		this.lastPingTimeMS = lastPingTimeMS;
	}
}