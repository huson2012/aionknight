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

package loginserver.network.aion.clientpackets;

import java.nio.ByteBuffer;
import loginserver.GameServerInfo;
import loginserver.GameServerTable;
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.SessionKey;
import loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import loginserver.network.aion.serverpackets.SM_PLAY_FAIL;
import loginserver.network.aion.serverpackets.SM_PLAY_OK;

public class CM_PLAY extends AionClientPacket
{
	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private int	accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private int	loginOk;
	/**
	 * Id of game server that this client is trying to play on.
	 */
	private byte	servId;

	/**
	 * Constructs new instance of <tt>SM_PLAY_FAIL</tt> packet.
	 * 
	 * @param buf       packet data
	 * @param client    client
	 */
	public CM_PLAY(ByteBuffer buf, AionConnection client)
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
		loginOk = readD();
		servId = (byte) readC();	
		readD();
		readD();
		readD();
		readH();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection con = getConnection();
		SessionKey key = con.getSessionKey();
		
		if (key.checkLogin(accountId, loginOk))
		{
			GameServerInfo gsi = GameServerTable.getGameServerInfo(servId);
			if (gsi == null || !gsi.isOnline())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_DOWN));
			// else if(serv gm only)
			// con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.GM_ONLY));
			else if(gsi.isFull())
				con.sendPacket(new SM_PLAY_FAIL(AionAuthResponse.SERVER_FULL));
			else
			{
				con.setJoinedGs();
				sendPacket(new SM_PLAY_OK(key,servId));
			}
		}
		else
			con.close(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR), true);
	}
}
