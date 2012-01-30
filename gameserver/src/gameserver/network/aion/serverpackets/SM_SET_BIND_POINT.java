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

import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_SET_BIND_POINT extends AionServerPacket
{
	private final int mapId;
	private final float x;
	private final float	y;
	private final float	z;
	private final Kisk kisk;

	public SM_SET_BIND_POINT(int mapId, float x, float y, float z, Player player)
	{
		this.mapId = mapId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.kisk	= player.getKisk();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		// Appears 0x04 if bound to a kisk. 0x00If not.
		writeC(buf, (kisk == null ? 0x00 : 0x04)); 
		
		writeC(buf, 0x01);// unk
		writeD(buf, mapId);// map id
		writeF(buf, x); // coordinate x
		writeF(buf, y); // coordinate y
		writeF(buf, z); // coordinate z
		writeD(buf, (kisk == null ? 0x00 : kisk.getObjectId())); // kisk object id
	}
}