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

public class SM_LEGION_EDIT extends AionServerPacket
{
	private int					type;
	private Legion				legion;
	private int					unixTime;
	private String				announcement;

	public SM_LEGION_EDIT(int type)
	{
		this.type = type;
	}

	public SM_LEGION_EDIT(int type, Legion legion)
	{
		this.type = type;
		this.legion = legion;
	}

	public SM_LEGION_EDIT(int type, int unixTime)
	{
		this.type = type;
		this.unixTime = unixTime;
	}

	public SM_LEGION_EDIT(int type, int unixTime, String announcement)
	{
		this.type = type;
		this.announcement = announcement;
		this.unixTime = unixTime;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
        writeC(buf, type);
		switch(type)
		{
			/** Change Legion Level **/
			case 0x00:
				writeC(buf, legion.getLegionLevel());
				break;
			/** Change Legion Rank **/
			case 0x01:
				writeD(buf, legion.getLegionRank());
				break;
			/** Change Legion Permissions **/
			case 0x02:
	            writeC(buf, legion.getDeputyPermission1());
	            writeC(buf, legion.getDeputyPermission2());
				writeC(buf, legion.getCenturionPermission1());
				writeC(buf, legion.getCenturionPermission2());
				writeC(buf, legion.getLegionaryPermission1());
				writeC(buf, legion.getLegionaryPermission2());
                writeC(buf, legion.getVolunteerPermission1());
                writeC(buf, legion.getVolunteerPermission2());
				break;
			/** Change Legion Contributions **/
			case 0x03:
				writeD(buf, legion.getContributionPoints()); // get Contributions
				break;
			/** Change Legion Announcement **/
			case 0x05:
				writeS(buf, announcement);
				writeD(buf, unixTime);
				break;
			/** Disband Legion **/
			case 0x06:
				writeD(buf, unixTime);
				break;
			/** Recover Legion **/
			case 0x07:
				break;
			/** Refresh Legion Announcement? **/
			case 0x08:
				break;
		}
	}
}