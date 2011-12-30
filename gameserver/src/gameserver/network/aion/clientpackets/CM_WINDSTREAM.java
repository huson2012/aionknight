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

package gameserver.network.aion.clientpackets;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_STATS_INFO;
import gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

/**
 * Packet concerning windstreams.
 */
public class CM_WINDSTREAM extends AionClientPacket
{
	int teleportId;
	int distance;
	int state;

	private static final Logger	log	= Logger.getLogger(CM_WINDSTREAM.class);

	/**
	 * @param opcode
	 */
	public CM_WINDSTREAM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		teleportId = readD(); //typical teleport id (ex : 94001 for talloc hallow in inggison)
		distance = readD();	 // 600 for talloc.
		state = readD(); // 0 or 1.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		if(player == null)
			return;
		
		switch(state)
		{
		case 0:
		case 4:
		case 8:
			//TODO:	Find in which cases second variable is 0 & 1
			//		Jego's example packets had server refuse with 0 and client kept retrying.
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state,1));
			break;
		case 1:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM, teleportId, distance), true);
			player.setEnterWindstream(1);
			break;
		case 2:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_END, 0, 0), true);			
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state,1));
			log.info("Player entered Windstream with telID: " + teleportId + ", distance: " + distance + " and state: " + state);
			break;
		case 7:
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_BOOST, 0, 0), true);
			player.setEnterWindstream(7);			
			break;
		default:
			log.error("Unknown Windstream state #" + state + " was found!" );
		}
	}
}
