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

import gameserver.model.DuelResult;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_DUEL extends AionServerPacket
{
	private String		playerName;
	private DuelResult	result;
	private int			requesterObjId;
	private int			type;

	private SM_DUEL(int type)
	{
		this.type = type;
	}
	
	public static SM_DUEL SM_DUEL_STARTED (int requesterObjId)
	{
		SM_DUEL packet = new SM_DUEL(0x00);
		packet.setRequesterObjId(requesterObjId);
		return packet;
	}
	
	private void setRequesterObjId (int requesterObjId) {
		this.requesterObjId = requesterObjId;
	}
	
	public static SM_DUEL SM_DUEL_RESULT (DuelResult result, String playerName)
	{
		SM_DUEL packet = new SM_DUEL(0x01);
		packet.setPlayerName(playerName);
		packet.setResult(result);
		return packet;
	}
	
	private void setPlayerName (String playerName) {
		this.playerName = playerName;
	}

	private void setResult (DuelResult result) {
		this.result = result;
	}
	
	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, type);

		switch(type)
		{
			case 0x00:
				writeD(buf, requesterObjId);
				break;
			case 0x01:
				writeC(buf, result.getResultId()); // unknown
				writeD(buf, result.getMsgId());
				writeS(buf, playerName);
				break;
			case 0xE0:
				break;
			default:
				throw new IllegalArgumentException("invalid SM_DUEL packet type " + type);
		}
	}
}
