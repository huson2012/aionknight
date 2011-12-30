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

import gameserver.model.Petition;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.PetitionService;
import java.nio.ByteBuffer;

public class SM_PETITION extends AionServerPacket
{
	private Petition petition;
	
    public SM_PETITION()
    {
        this.petition = null;
    }
    
    public SM_PETITION(Petition petition)
    {
    	this.petition = petition;
    }

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(petition == null)
		{
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeH(buf, 0x00);
			writeC(buf, 0x00);
		}
		else
		{
			writeC(buf, 0x01); // Action ID ?
			writeD(buf, 100); // unk (total online players ?)
			writeH(buf, PetitionService.getInstance().getWaitingPlayers(con.getActivePlayer().getObjectId())); // Users waiting for Support
			writeS(buf, Integer.toString(petition.getPetitionId())); // Ticket ID
			writeH(buf, 0x00);
			writeC(buf, 50); // Total Petitions
			writeC(buf, 49); // Remaining Petitions
			writeH(buf, PetitionService.getInstance().calculateWaitTime(petition.getPlayerObjId())); // Estimated minutes before GM reply
			writeD(buf, 0x00);
		}
	}
}
