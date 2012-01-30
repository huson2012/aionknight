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
