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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LEGION_UPDATE_MEMBER extends AionServerPacket
{
	private static final int	OFFLINE	= 0x00;
	private static final int	ONLINE	= 0x01;
	private Player				player;
	private int					msgId;
	private String				text;

	public SM_LEGION_UPDATE_MEMBER(Player player, int msgId, String text)
	{
		this.player = player;
		this.msgId = msgId;
		this.text = text;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, player.getObjectId());
		writeC(buf, player.getLegionMember().getRank().getRankId());
		writeC(buf, player.getCommonData().getPlayerClass().getClassId());
		writeC(buf, player.getLevel());
		writeD(buf, player.getPosition().getMapId());
		writeC(buf, player.isOnline() ? ONLINE : OFFLINE);
		writeD(buf, player.getLastOnline());
		writeD(buf, msgId);
		writeS(buf, text);
	}
}

// MAP ID: 90 9E 8E 06
// ONLINE: 00
// 00 00 00 00
// D8 74 7C 4B 00 00 4B 00 00

// MAP ID: 90 9E 8E 06
// ONLINE: 01
// 00 00 00 00
// 00 00 00 00

// MAP ID: 90 9E 8E 06
// ONLINE: 01
// 00 00 00 00
// 00 00 00 00
// 00 00

// ONLINE: 01
// UNK: 00 00 00 00
// MEMBER ID: 31 D7 13 00
// MEMBER NAME: 40 00 60 00 60 00 00 00
// but why is it longer?