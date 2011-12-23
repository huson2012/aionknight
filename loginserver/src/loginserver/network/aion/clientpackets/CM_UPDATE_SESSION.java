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
import loginserver.controller.AccountController;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;

/**
 * This packet is send when client was connected to game server and now is reconnection to login server.
 */
public class CM_UPDATE_SESSION extends AionClientPacket
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
	 * reconectKey is key that server sends to client for fast reconnection to login server - we will check if this key
	 * is valid.
	 */
	private int	reconnectKey;

	/**
	 * Constructs new instance of <tt>CM_UPDATE_SESSION </tt> packet.
	 * 
	 * @param buf       packet data
	 * @param client    client
	 */
	public CM_UPDATE_SESSION(ByteBuffer buf, AionConnection client)
	{
		super(buf, client, 0x08);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountId = readD();
		loginOk = readD();
		reconnectKey = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AccountController.authReconnectingAccount(accountId, loginOk, reconnectKey, getConnection());
	}
}
