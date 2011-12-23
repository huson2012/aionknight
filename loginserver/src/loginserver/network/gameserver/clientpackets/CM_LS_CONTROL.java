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
import loginserver.dao.AccountDAO;
import loginserver.model.Account;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;
import loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;
import commons.database.dao.DAOManager;

public class CM_LS_CONTROL extends GsClientPacket
{
	private String accountName;
	private int param;
	private int	type;
	private String playerName;
	private String adminName;
	private boolean result;

	public CM_LS_CONTROL(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x05);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{

		type = readC();
		adminName = readS();
		accountName = readS();
		playerName = readS();
		param = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{

		Account	account = DAOManager.getDAO(AccountDAO.class).getAccount(accountName);
		switch (type)
		{
			case 1:
				account.setAccessLevel((byte)param);
				break;
			case 2:
				account.setMembership((byte)param);
			break;
		}
		result = DAOManager.getDAO(AccountDAO.class).updateAccount(account);
		sendPacket(new SM_LS_CONTROL_RESPONSE(type, result, playerName, account.getId(), param, adminName));	
	}
}