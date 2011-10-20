package ru.aionknight.loginserver.network.gameserver.clientpackets;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.openaion.commons.utils.Rnd;

import ru.aionknight.loginserver.controller.AccountController;
import ru.aionknight.loginserver.model.Account;
import ru.aionknight.loginserver.model.ReconnectingAccount;
import ru.aionknight.loginserver.network.gameserver.GsClientPacket;
import ru.aionknight.loginserver.network.gameserver.GsConnection;
import ru.aionknight.loginserver.network.gameserver.serverpackets.SM_ACCOUNT_RECONNECT_KEY;


/**
 * This packet is sended by GameServer when player is requesting fast reconnect to login server. LoginServer in response
 * will send reconectKey.
 * 
 * @author -Nemesiss-
 * 
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
	 *  {@inheritDoc}
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
