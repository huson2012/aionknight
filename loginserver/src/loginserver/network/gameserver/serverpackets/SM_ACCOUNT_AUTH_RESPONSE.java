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

package loginserver.network.gameserver.serverpackets;

import java.nio.ByteBuffer;
import loginserver.model.AccountTime;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.GsServerPacket;

/**
 * In this packet LoginServer is answering on GameServer request about valid authentication data and also sends account
 * name of user that is authenticating on GameServer.
 */
public class SM_ACCOUNT_AUTH_RESPONSE extends GsServerPacket
{
	/**
	 * Account id
	 */
	private final int accountId;

	/**
	 * True if account is authenticated.
	 */
	private final boolean ok;

	/**
	 * account name
	 */
	private final String accountName;
	
	/**
	 * Access level
	 */
	private final byte accessLevel;
	
	/**
	 * Membership
	 */
	private final byte membership;

	/**
	 * Constructor.
	 *
	 * @param accountId
	 * @param ok
	 * @param accountName
	 * @param accessLevel 
	 * @param membership 
	 */
	public SM_ACCOUNT_AUTH_RESPONSE(int accountId, boolean ok, String accountName, byte accessLevel, byte membership)
	{
		super(0x01);

		this.accountId   = accountId;
		this.ok          = ok;
		this.accountName = accountName;
		this.accessLevel = accessLevel;
		this.membership  = membership;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GsConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeC(buf, ok ? 1 : 0);

		if(ok)
		{
			writeS(buf, accountName);

			AccountTime	accountTime = con.getGameServerInfo().getAccountFromGameServer(accountId).getAccountTime();

			writeQ(buf, accountTime.getAccumulatedOnlineTime());
			writeQ(buf, accountTime.getAccumulatedRestTime());
			writeC(buf, accessLevel);
			writeC(buf, membership);
		}
	}
}