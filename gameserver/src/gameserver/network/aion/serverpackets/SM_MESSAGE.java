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

import gameserver.configs.main.CustomConfig;
import gameserver.model.ChatType;
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * Massage [chat, etc]
 */
public class SM_MESSAGE extends AionServerPacket
{
	/**
	 * Player.
	 */
	private Player		player;
	/**
	 * Object that is saying smth or null.
	 */
	private int			senderObjectId;

	/**
	 * Message.
	 */
	private String		message;

	/**
	 * Name of the sender
	 */
	private String		senderName;

	/**
	 * Sender race
	 */
	private Race		race;

	/**
	 * Chat type
	 */
	private ChatType	chatType;

	/**
	 * Sender coordinates
	 */
	private float		x;
	private float		y;
	private float		z;

	/**
	 * Constructs new <tt>SM_MESSAGE </tt> packet
	 * 
	 * @param player
	 *           who sent message
	 * @param message
	 *           actual message
	 * @param chatType
	 *           what chat type should be used
	 */
	public SM_MESSAGE(Player player, String message, ChatType chatType)
	{
		this.player = player;
		this.senderObjectId = player.getObjectId();
		this.senderName = player.getName();
		this.message = message;
		this.race = player.getCommonData().getRace();
		this.chatType = chatType;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}

	/**
	 * Manual creation of chat message.<br>
	 * 
	 * @param senderObjectId
	 *           - can be 0 if system message(like announcements)
	 * @param senderName
	 *           - used for shout ATM, can be null in other cases
	 * @param message
	 *           - actual text
	 * @param chatType
	 *           type of chat, Normal, Shout, Announcements, Etc...
	 */
	public SM_MESSAGE(int senderObjectId, String senderName, String message, ChatType chatType)
	{
		this.senderObjectId = senderObjectId;
		this.senderName = senderName;
		this.message = message;
		this.chatType = chatType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		boolean canRead = true;

		if(race != null)
		{
			canRead = chatType.isSysMsg() || CustomConfig.FACTIONS_SPEAKING_MODE == 1 || player.getAccessLevel() > 0
				|| (con.getActivePlayer() != null && con.getActivePlayer().getAccessLevel() > 0);
		}

		writeC(buf, chatType.toInteger()); // type

		/**
		 * 0 : all 1 : elyos 2 : asmodians
		 */
		writeC(buf, canRead ? 0 : race.getRaceId() + 1);
		writeD(buf, senderObjectId); // sender object id

		switch(chatType)
		{
			case NORMAL:
			case ANNOUNCEMENTS:
			case PERIOD_NOTICE:
			case PERIOD_ANNOUNCEMENTS:
			case SYSTEM_NOTICE:
				writeH(buf, 0x00); // unknown
				writeS(buf, message);
				break;
			case SHOUT:
				writeS(buf, senderName);
				writeS(buf, message);
				writeF(buf, x);
				writeF(buf, y);
				writeF(buf, z);
				break;
			case ALLIANCE:
			case GROUP:
			case GROUP_LEADER:
			case LEGION:
			case WHISPER:
				writeS(buf, senderName);
				writeS(buf, message);
				break;
		}
	}
}