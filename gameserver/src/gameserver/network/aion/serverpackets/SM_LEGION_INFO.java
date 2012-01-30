/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
		for(Map.Entry<Timestamp, String> entry : announcementList.entrySet())
		{
			writeS(buf, entry.getValue());
			writeD(buf, (int) (entry.getKey().getTime() / 1000));
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