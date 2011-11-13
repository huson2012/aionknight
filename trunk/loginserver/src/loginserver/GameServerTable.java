/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package loginserver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import loginserver.dao.GameServersDAO;
import loginserver.model.Account;
import loginserver.network.gameserver.GsAuthResponse;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;
import org.apache.log4j.Logger;
import commons.database.dao.DAOManager;
import commons.network.IPRange;
import commons.utils.NetworkUtils;

public class GameServerTable
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(GameServerTable.class);

	/**
	 * Map<Id,GameServer>
	 */
	private static Map<Byte, GameServerInfo> gameservers;

	/**
	 * Return collection contains all registered [up/down] GameServers.
	 * 
	 * @return collection of GameServers.
	 */
	public static Collection<GameServerInfo> getGameServers()
	{
		return Collections.unmodifiableCollection(gameservers.values());
	}

	/**
	 * Load GameServers from database.
	 */
	public static void load()
	{
		gameservers = getDAO().getAllGameServers();
		log.info("GS table: " + gameservers.size() + " registered GameServers.");
	}
	public static GsAuthResponse registerGameServer(GsConnection gsConnection, byte requestedId, byte[] defaultAddress,
		List<IPRange> ipRanges, int port, int maxPlayers, int requiredAccess, String password)
	{
		GameServerInfo gsi = gameservers.get(requestedId);

		/**
		 * This id is not Registered at LoginServer.
		 */
		if (gsi == null)
		{
			log.info(gsConnection + " requestedID=" + requestedId + " not aviable!");
			return GsAuthResponse.NOT_AUTHED;
		}

		/**
		 * Check if this GameServer is not already registered.
		 */
		if (gsi.getGsConnection() != null)
			return GsAuthResponse.ALREADY_REGISTERED;

		/**
		 * Check if password and ip are ok.
		 */
		if (!gsi.getPassword().equals(password) || !NetworkUtils.checkIPMatching(gsi.getIp(), gsConnection.getIP()))
		{
			log.info(gsConnection + " wrong ip or password!");
			return GsAuthResponse.NOT_AUTHED;
		}

		gsi.setDefaultAddress(defaultAddress);
		gsi.setIpRanges(ipRanges);
		gsi.setPort(port);
		gsi.setMaxPlayers(maxPlayers);
		gsi.setRequiredAccess(requiredAccess);
		gsi.setGsConnection(gsConnection);

		gsConnection.setGameServerInfo(gsi);
		return GsAuthResponse.AUTHED;
	}

	/**
	 * Returns GameSererInfo object for given gameserverId.
	 * 
	 * @param gameServerId
	 * @return GameSererInfo object for given gameserverId.
	 */
	public static GameServerInfo getGameServerInfo(byte gameServerId)
	{
		return gameservers.get(gameServerId);
	}

	/**
	 * Check if account is already in use on any GameServer. If so - kick account from GameServer.
	 * 
	 * @param acc account to check
	 * @return true is account is logged in on one of GameServers
	 */
	public static boolean isAccountOnAnyGameServer(Account acc)
	{
		for (GameServerInfo gsi : getGameServers())
		{
			if (gsi.isAccountOnGameServer(acc.getId()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Helper method, used to kick account from any gameServer if it's logged in
	 * @param account account to kick
	 */
	public static void kickAccountFromGameServer(Account account)
	{
		for (GameServerInfo gsi : getGameServers())
		{
			if (gsi.isAccountOnGameServer(account.getId()))
			{
				gsi.getGsConnection().sendPacket(new SM_REQUEST_KICK_ACCOUNT(account.getId()));
				break;
			}
		}
	}

	/**
	 * Retuns {@link loginserver.dao.GameServersDAO} , just a shortcut
	 * 
	 * @return {@link loginserver.dao.GameServersDAO}
	 */
	private static GameServersDAO getDAO()
	{
		return DAOManager.getDAO(GameServersDAO.class);
	}
}