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

import gameserver.model.gameobjects.player.Friend;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;

public class SM_FRIEND_UPDATE extends AionServerPacket
{
	private int friendObjId;
	
	private static Logger log = Logger.getLogger(SM_FRIEND_UPDATE.class);
	public SM_FRIEND_UPDATE(int friendObjId)
	{
		this.friendObjId = friendObjId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		Friend f = con.getActivePlayer().getFriendList().getFriend(friendObjId);
		if (f == null)
			log.debug("Attempted to update friend list status of " + friendObjId + " for " + con.getActivePlayer().getName() + " - object ID not found on friend list");
		else
		{
			writeS(buf, f.getName());
			writeD(buf, f.getLevel());
			writeD(buf, f.getPlayerClass().getClassId());
			writeC(buf, f.isOnline() ? 1 : 0); // Online status - No idea why this and f.getStatus are used
			writeD(buf, f.getMapId());
			writeD(buf, f.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
			writeS(buf, f.getNote());
			writeC(buf, f.getStatus().getIntValue());
		}
	}
}