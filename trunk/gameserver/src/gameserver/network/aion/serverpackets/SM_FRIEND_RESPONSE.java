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

public class SM_FRIEND_RESPONSE extends AionServerPacket
{
	/**
	 * The friend was successfully added to your list
	 */
	public static final int TARGET_ADDED = 0x00;
	/**
	 * The target of a friend request is offline
	 */
	public static final int TARGET_OFFLINE = 0x01;
	/**
	 * The target is already a friend
	 */
	public static final int TARGET_ALREADY_FRIEND = 0x02;
	/**
	 * The target does not exist
	 */
	public static final int TARGET_NOT_FOUND = 0x03;
	/**
	 * The friend denied your request to add him
	 */
	public static final int TARGET_DENIED = 0x04;
	/**
	 * The target's friend list is full
	 */
	public static final int TARGET_LIST_FULL = 0x05;
	/**
	 * The friend was removed from your list
	 */
	public static final int TARGET_REMOVED = 0x06;
	/**
	 * The target is in your blocked list, 
	 * and cannot be added to your friends list.
	 */
	public static final int TARGET_BLOCKED = 0x08;
	/**
	 * The target is dead and cannot be befriended yet.
	 */
	public static final int TARGET_DEAD	= 0x09;
	
	private final String player;
	private final int code;
	public SM_FRIEND_RESPONSE(String playerName, int messageType) {
		player = playerName;
		code = messageType;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{

		writeS(buf, player);
		writeC(buf, code);
	}

}
