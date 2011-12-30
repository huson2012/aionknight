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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_LEGION_LEAVE_MEMBER extends AionServerPacket
{
	private String	name;
	private String	name1;
	private int		playerObjId;
	private int		msgId;

	public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name)
	{
		this.msgId = msgId;
		this.playerObjId = playerObjId;
		this.name = name;
	}

	public SM_LEGION_LEAVE_MEMBER(int msgId, int playerObjId, String name, String name1)
	{
		this.msgId = msgId;
		this.playerObjId = playerObjId;
		this.name = name;
		this.name1 = name1;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerObjId);
		writeC(buf, 0x00); // isMember ? 1 : 0
		writeD(buf, 0x00); // unix time for log off
		writeD(buf, msgId);
		writeS(buf, name);
		writeS(buf, name1);
	}
}