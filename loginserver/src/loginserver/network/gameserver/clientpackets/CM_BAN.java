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

package loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.controller.AccountController;
import loginserver.controller.BannedIpController;
import loginserver.dao.AccountDAO;
import loginserver.dao.AccountTimeDAO;
import loginserver.model.Account;
import loginserver.model.AccountTime;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_BAN_RESPONSE;
import commons.database.dao.DAOManager;

/**
 * The universal packet for account/IP bans
 */
public class CM_BAN extends GsClientPacket
{
	/**
	 * Ban type
	 * 1 = account
	 * 2 = IP
	 * 3 = Full ban (account and IP)
	 */
	private byte		type;
	
	/**
	 * Account to ban
	 */
	private int			accountId;

	/**
	 * IP or mask to ban
	 */
	private String		ip;

	/**
	 * Time in minutes. 0 = infinity;
	 * If time < 0 then it's unban command
	 */
	private int			time;
	
	/**
	 * Object ID of Admin, who request the ban
	 */
	private int			adminObjId;

	/**
	 * Constructor.
	 * 
	 * @param buf
	 * @param client
	 */
	public CM_BAN(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x06);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		this.type = (byte) readC();
		this.accountId = readD();
		this.ip = readS();
		this.time = readD();
		this.adminObjId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		boolean result = false;
		
		// Ban account
		if ((type == 1 || type == 3) && accountId != 0)
		{
			Account account = null;
			
			// Find account on GameServers
			for (GameServerInfo gsi : GameServerTable.getGameServers())
			{
				if (gsi.isAccountOnGameServer(accountId))
				{
					account = gsi.getAccountFromGameServer(accountId);
					break;
				}
			}
			
			// 1000 is 'infinity' value
			Timestamp newTime = null;
			if (time >= 0)
				newTime = new Timestamp(time == 0 ? 1000 : System.currentTimeMillis() + time * 60000);
			
			if (account != null)
			{
				AccountTime accountTime = account.getAccountTime();
				accountTime.setPenaltyEnd(newTime);
				account.setAccountTime(accountTime);
				result = true;
			}
			else
			{
				AccountTime accountTime = DAOManager.getDAO(AccountTimeDAO.class).getAccountTime(accountId);
				accountTime.setPenaltyEnd(newTime);
				result = DAOManager.getDAO(AccountTimeDAO.class).updateAccountTime(accountId, accountTime);
			}
		}
		
		// Ban IP
		if (type == 2 || type == 3)
		{
			if (accountId != 0) // If we got account ID, then ban last IP
			{
				String newip = DAOManager.getDAO(AccountDAO.class).getLastIp(accountId);
				if (!newip.isEmpty())
					ip = newip;
			}
			if (!ip.isEmpty())
			{
				// Unban first. For banning it needs to update time
				if (BannedIpController.isBanned(ip))
				{
					// Result set for unban request
					result = BannedIpController.unbanIp(ip);
				}
				if (time >= 0) // Ban
				{
					Timestamp newTime = time != 0 ? new Timestamp(System.currentTimeMillis() + time * 60000) : null;
					result = BannedIpController.banIp(ip, newTime);
				}
			}
		}
		
		// Now kick account
		if (accountId != 0)
		{
			AccountController.kickAccount(accountId);
		}
		
		// Respond to GS
		sendPacket(new SM_BAN_RESPONSE(type, accountId, ip, time, adminObjId, result));
	}
}
