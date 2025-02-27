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

import commons.network.Dispatcher;
import commons.network.NioServer;
import gameserver.configs.network.NetworkConfig;
import gameserver.model.account.Account;
import gameserver.model.account.AccountTime;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_L2AUTH_LOGIN_CHECK;
import gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import gameserver.network.aion.serverpackets.SM_RECONNECT_KEY;
import gameserver.network.loginserver.LoginServerConnection.State;
import gameserver.network.loginserver.serverpackets.*;
import gameserver.services.AccountService;
import gameserver.services.PlayerService;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utill class for connecting GameServer to LoginServer.
 */
public class LoginServer
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(LoginServer.class);

	/**
	 * Map<accountId,Connection> for waiting request. This request is send to LoginServer and GameServer is 
	 * waiting for response.
	 */
	private Map<Integer, AionConnection> loginRequests = new HashMap<Integer, AionConnection>();

	/**
	 * Map<accountId,Connection> for all logged in accounts.
	 */
	private Map<Integer, AionConnection>	loggedInAccounts	= new HashMap<Integer, AionConnection>();

	/**
	 * Connection to LoginServer.
	 */
	private LoginServerConnection			loginServer;

	private NioServer						nioServer;
	private boolean							serverShutdown = false;
	
	public static final LoginServer getInstance()
	{
		return SingletonHolder.instance;
	}

	private LoginServer()
	{
		
	}

	public void setNioServer(NioServer nioServer)
	{
		this.nioServer = nioServer;
	}

	/**
	 * Connect to LoginServer and return object representing this connection. This method is blocking and may 
	 * block till connect successful.
	 * 
	 * @return LoginServerConnection
	 */
	public LoginServerConnection connect()
	{
		SocketChannel sc;
		for(;;)
		{
			loginServer = null;
			log.info("Connecting to LS: " + NetworkConfig.LOGIN_ADDRESS);
			try
			{
				sc = SocketChannel.open(NetworkConfig.LOGIN_ADDRESS);
				sc.configureBlocking(false);
				Dispatcher d = nioServer.getReadWriteDispatcher();
				loginServer = new LoginServerConnection(sc, d);
				return loginServer;
			}
			catch(Exception e)
			{
				log.info("Cant connect to LS: " + e.getMessage());
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
	 * This method is called when we lost connection to LoginServer. We will disconnects all aionClients waiting for
	 * LoginServer response and also try reconnect to LoginServer.
	 */
	public void loginServerDown()
	{
		log.warn("Connection with LoginServer lost...");

		loginServer = null;
		synchronized(this)
		{
			/**
			 * We lost connection for LoginServer so client pending authentication should be disconnected [cuz
			 * authentication will never ends]
			 */
			for(AionConnection client : loginRequests.values())
			{
				// TODO! somme error packet!
				client.close(/** closePacket, */true);
			}
			loginRequests.clear();
		}

		/**
		 * Reconnect after 5sIf not server shutdown sequence
		 */
		if (!serverShutdown) {
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					connect();
				}
			}, 5000);
		}
	}

	/**
	 * Notify that client is disconnected - we must clear waiting request to LoginServer if any to prevent leaks. 
	 * Also notify LoginServer that this account is no longer on GameServer side.
	 * 
	 * @param client
	 */
	public void aionClientDisconnected(int accountId)
	{
		synchronized(this)
		{
			loginRequests.remove(accountId);
			loggedInAccounts.remove(accountId);
		}
		sendAccountDisconnected(accountId);
	}
	
	/**
	 * 
	 * @param accountId
	 */
	private void sendAccountDisconnected(int accountId)
	{
		log.info("Sending account disconnected " + accountId);
		if(loginServer != null && loginServer.getState() == State.AUTHED)
			loginServer.sendPacket(new SM_ACCOUNT_DISCONNECTED(accountId));
	}

	/**
	 * Starts authentication procedure of this client - LoginServer will sends response with information about 
	 * account name if authentication is ok.
	 * 
	 * @param accountId 
	 * @param client
	 * @param loginOk
	 * @param playOk1
	 * @param playOk2
	 */
	public void requestAuthenticationOfClient(int accountId, AionConnection client, int loginOk, int playOk1, int playOk2)
	{
		/**
		 * There are no connection to LoginServer. We should disconnect this client since authentication is not
		 * possible.
		 */
		if(loginServer == null || loginServer.getState() != State.AUTHED)
		{
			log.warn("LS !!! " + (loginServer == null ? "NULL" : loginServer.getState()));
			// TODO! somme error packet!
			client.close(/** closePacket, */true);
			return;
		}

		synchronized(this)
		{
			if(loginRequests.containsKey(accountId))
				return;
			loginRequests.put(accountId, client);
		}
		loginServer.sendPacket(new SM_ACCOUNT_AUTH(accountId, loginOk, playOk1, playOk2));
	}

	/**
	 * This method is called by CM_ACCOUNT_AUTH_RESPONSE LoginServer packets to notify GameServer about results of
	 * client authentication.
	 * 
	 * @param accountId
	 * @param accountName
	 * @param result
	 * @param accountTime 
	 */
	public void accountAuthenticationResponse(int accountId, String accountName, boolean result, AccountTime accountTime, byte accessLevel, byte membership)
	{
		AionConnection client = loginRequests.remove(accountId);

		if(client == null)
			return;

		if(result)
		{
			client.setState(AionConnection.State.AUTHED);
			loggedInAccounts.put(accountId, client);
			log.info("Account authed: " + accountId + " = " + accountName);
			client.setAccount(AccountService.getAccount
			(
				accountId, 
				accountName, 
				accountTime, 
				accessLevel, 
				membership
			));
			client.sendPacket(new SM_L2AUTH_LOGIN_CHECK(true));
		}
		else
		{
			log.info("Account not authed: " + accountId);
			client.close(new SM_L2AUTH_LOGIN_CHECK(false), true);
		}
	}

	/**
	 * Starts reconnection to LoginServer procedure. LoginServer in response will send reconnection key.
	 * 
	 * @param client
	 */
	public void requestAuthReconnection(AionConnection client)
	{
		/**
		 * There are no connection to LoginServer. We should disconnect this client since authentication is not
		 * possible.
		 */
		if(loginServer == null || loginServer.getState() != State.AUTHED)
		{
			// TODO! somme error packet!
			client.close(/** closePacket, */false);
			return;
		}

		synchronized(this)
		{
			if(loginRequests.containsKey(client.getAccount().getId()))
				return;
			loginRequests.put(client.getAccount().getId(), client);

		}
		loginServer.sendPacket(new SM_ACCOUNT_RECONNECT_KEY(client.getAccount().getId()));
	}

	/**
	 * This method is called by CM_ACCOUNT_RECONNECT_KEY LoginServer packets to give GameServer reconnection key 
	 * for client that was requesting reconnection.
	 * 
	 * @param accountId
	 * @param reconnectKey
	 */
	public void authReconnectionResponse(int accountId, int reconnectKey)
	{
		AionConnection client = loginRequests.remove(accountId);

		if(client == null)
			return;

		log.info("Account reconnecting: " + accountId + " = " + client.getAccount().getName());
		//client.sendPacket(new SM_RECONNECT_KEY(reconnectKey));
		client.close(new SM_RECONNECT_KEY(reconnectKey), false);
	}

	/**
	 * This method is called by CM_REQUEST_KICK_ACCOUNT LoginServer packets to request GameServer to disconnect 
	 * client with given account id.
	 * 
	 * @param accountId
	 */
	public void kickAccount(int accountId)
	{
		synchronized(this)
		{
			AionConnection client = loggedInAccounts.get(accountId);
			if(client != null)
			{
				PlayerService.playerLoggedOut(client.getActivePlayer());
				closeClientWithCheck(client, accountId);
			}
			//This account is not logged in on this GameServer but LS thinks different...
			else
			{
				sendAccountDisconnected(accountId);
			}
		}
	}
	
	private void closeClientWithCheck(AionConnection client, final int accountId)
	{
		log.info("Closing client connection " + accountId);
		client.close(new SM_QUIT_RESPONSE(), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			
			@Override
			public void run()
			{
				AionConnection client = loggedInAccounts.get(accountId);
				if(client != null)
				{
					log.warn("Removing client from server because of stalled connection");
					client.close(new SM_QUIT_RESPONSE(), true);
					loggedInAccounts.remove(accountId);
					sendAccountDisconnected(accountId);
				}
			}
		}, 5000);
	}

	/**
	 * Returns unmodifiable map with accounts that are logged in to current GS Map Key: Account ID Map Value:
	 * AionConnectionObject
	 * 
	 * @return unmodifiable map wwith accounts
	 */
	public Map<Integer, AionConnection> getLoggedInAccounts()
	{
		return Collections.unmodifiableMap(loggedInAccounts);
	}

	/**
	 * When Game Server shutdown, have to close all pending client connection
	 */
	public void gameServerDisconnected()
	{
		synchronized(this)
		{
			serverShutdown = true;
			/**
			 * GameServer shutting down, must close all pending login requests
			 */
			for(AionConnection client : loginRequests.values())
			{
				// TODO! somme error packet!
				client.close(/** closePacket, */true);
			}
			loginRequests.clear();
			
			loginServer.close(false);
		}

		log.info("GameServer disconnected from the Login Server...");
	}


	public void sendLsControlPacket(String accountName, String playerName, String adminName, int param, int type)
	{
		if(loginServer != null && loginServer.getState() == State.AUTHED)
			loginServer.sendPacket(new SM_LS_CONTROL(accountName, playerName, adminName, param, type));
	}

	public void accountUpdate(int accountId, byte param, int type)
	{
		synchronized(this)
		{
			AionConnection client = loggedInAccounts.get(accountId);
			if (client != null)
			{
				Account account =client.getAccount();
				if (type == 1)
					account.setAccessLevel(param);
				if (type == 2)
					account.setMembership(param);
			}
		}
	}
	
	public void sendBanPacket(byte type, int accountId, String ip, int time, int adminObjId)
	{
		if(loginServer != null && loginServer.getState() == State.AUTHED)
			loginServer.sendPacket(new SM_BAN(type, accountId, ip, time, adminObjId));
	}
	
	public boolean sendBanMacPacket(LsServerPacket pk) {
		if (loginServer != null && loginServer.getState() == State.AUTHED) {
			loginServer.sendPacket(pk);
			return true;
		}
		else
			return false;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final LoginServer instance = new LoginServer();
	}
}