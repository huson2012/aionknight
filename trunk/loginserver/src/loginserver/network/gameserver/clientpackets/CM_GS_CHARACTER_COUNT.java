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
import loginserver.GameServerInfo;
import loginserver.controller.AccountController;
import loginserver.network.gameserver.GsClientPacket;
import loginserver.network.gameserver.GsConnection;

/**
 * Packet sent to game server to request account characters count
 * When all characters count have been received, send server list to client 
 */
public class CM_GS_CHARACTER_COUNT extends GsClientPacket
{
	private int accountId;
	private int characterCount;
	
	/**
	 * @param buf
	 * @param client
	 */
	public CM_GS_CHARACTER_COUNT(ByteBuffer buf, GsConnection client)
	{
		super(buf, client, 0x07);
	}

	@Override
	protected void readImpl()
	{
		accountId = readD();
		characterCount = readC();
	}

	@Override
	protected void runImpl()
	{
		GameServerInfo gsi = getConnection().getGameServerInfo();
		
		AccountController.addCharacterCountFor(accountId, gsi.getId(), characterCount);
		
		if(AccountController.hasAllCharacterCounts(accountId))
		{
			AccountController.sendServerListFor(accountId);
		}
	}
}
