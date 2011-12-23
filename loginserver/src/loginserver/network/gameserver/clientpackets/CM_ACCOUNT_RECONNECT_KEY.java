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
import loginserver.controller.AccountController;
import loginserver.model.Account;
import loginserver.model.ReconnectingAccount;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_ACCOUNT_RECONNECT_KEY;
import org.apache.log4j.Logger;
import commons.utils.Rnd;


/**
 * This packet is sended by GameServer when player is requesting fast reconnect to login server. LoginServer in response
 * will send reconectKey.
 */
public class CM_ACCOUNT_RECONNECT_KEY extends GsClientPacket
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(CM_ACCOUNT_RECONNECT_KEY.class);
	/**
	 * accoundId of account that will be reconnecting.
	 */
	private int			accountId;

	/**
	 * Constructor.
	 * 
	 * @param buf
	 * @param client
	 */
	public CM_ACCOUNT_RECONNECT_KEY(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x02);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		int reconectKey = Rnd.nextInt();
		Account acc = getConnection().getGameServerInfo().removeAccountFromGameServer(accountId);
		if (acc == null)
			log.info("This shouldnt happend! [Error]");
		else
			AccountController.addReconnectingAccount(new ReconnectingAccount(acc, reconectKey));
		sendPacket(new SM_ACCOUNT_RECONNECT_KEY(accountId, reconectKey));
	}
}
