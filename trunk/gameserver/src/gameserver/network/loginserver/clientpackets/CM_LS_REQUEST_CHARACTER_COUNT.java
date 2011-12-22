/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.LsClientPacket;
import gameserver.network.loginserver.serverpackets.SM_LS_CHARACTER_COUNT;
import gameserver.services.AccountService;

/**
 * Packet sent by login server to request account characters count
 */
public class CM_LS_REQUEST_CHARACTER_COUNT extends LsClientPacket
{	
	private int	accountId;
	
	/**
	 * @param opcode
	 */
	public CM_LS_REQUEST_CHARACTER_COUNT(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		accountId = readD();
	}

	@Override
	protected void runImpl()
	{
		int characterCount = AccountService.getCharacterCountFor(accountId);
		sendPacket(new SM_LS_CHARACTER_COUNT(accountId, characterCount));
	}
}