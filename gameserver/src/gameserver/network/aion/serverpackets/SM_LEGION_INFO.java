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

import gameserver.model.legion.Legion;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Map;

public class SM_LEGION_INFO extends AionServerPacket
{
	/** Legion information **/
	private Legion	legion;

	/**
	 * This constructor will handle legion info
	 * 
	 * @param legion
	 */
	public SM_LEGION_INFO(Legion legion)
	{
		this.legion = legion;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeS(buf, legion.getLegionName());
		writeC(buf, legion.getLegionLevel());
		writeD(buf, legion.getLegionRank());
	    writeC(buf, legion.getDeputyPermission1());
	    writeC(buf, legion.getDeputyPermission2());
		writeC(buf, legion.getCenturionPermission1());
		writeC(buf, legion.getCenturionPermission2());
		writeC(buf, legion.getLegionaryPermission1());
		writeC(buf, legion.getLegionaryPermission2());
	    writeC(buf, legion.getVolunteerPermission1());
	    writeC(buf, legion.getVolunteerPermission2());
		writeD(buf, legion.getContributionPoints());
		writeD(buf, 0x00); // unk
		writeD(buf, 0x00); // unk
		writeD(buf, 0x00); // unk

		/** Get Announcements List From DB By Legion **/
		Map<Timestamp, String> announcementList = legion.getAnnouncementList().descendingMap();

		/** Show max 7 announcements **/
		int i = 0;
		for(Timestamp unixTime : announcementList.keySet())
		{
			writeS(buf, announcementList.get(unixTime));
			writeD(buf, (int) (unixTime.getTime() / 1000));
			i++;
			if(i >= 7)
				break;
		}

		if(announcementList.size() > 0)
		    writeH(buf, 0);// unk 2.5		
		
		if(legion.getLegionEmblem().getCustomEmblemData() == null)
			writeH(buf, 105);
		else
		  	writeH(buf, 108);
	}
}
