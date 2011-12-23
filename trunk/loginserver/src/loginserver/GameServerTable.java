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
		log.info("GS table: " + gameservers.size() + " registered GS.");
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
			log.info(gsConnection + " requested ID = " + requestedId + " not aviable!");
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