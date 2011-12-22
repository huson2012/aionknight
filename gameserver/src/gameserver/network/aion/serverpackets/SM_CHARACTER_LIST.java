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

package gameserver.network.aion.serverpackets;

import gameserver.model.account.Account;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.PlayerInfo;
import java.nio.ByteBuffer;

public class SM_CHARACTER_LIST extends PlayerInfo
{
	private final int	playOk2;

    public SM_CHARACTER_LIST(int playOk2)
	{
		this.playOk2 = playOk2;
	}

    @Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playOk2);

		Account account = con.getAccount();
		writeC(buf, account.size());

		for(PlayerAccountData playerData : account.getSortedAccountsList())
		{
			writePlayerInfo(buf, playerData);

			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeC(buf, 0);
			writeC(buf, 0);
			writeB(buf, new byte[28]);
		}
	}
}