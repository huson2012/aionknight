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

import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_FORCED_MOVE extends AionServerPacket
{
	private Creature creature;
	private Creature target;
	private float x = 0;
	private float y = 0;
	private float z = 0;
	
	public SM_FORCED_MOVE(Creature creature, float x, float y, float z)
	{
		this.creature = creature;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public SM_FORCED_MOVE(Creature creature, Creature target)
	{
		this.creature = creature;
		this.target = target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, creature.getObjectId());
		if(target != null)
			writeD(buf, target.getObjectId());
		else
			writeD(buf, creature.getObjectId());
		writeC(buf, 16); // unk
		if(x == 0 && y == 0 && z == 0)
		{
			writeF(buf, target.getX());
			writeF(buf, target.getY());
			writeF(buf, target.getZ() + 0.25f);
		}
		else
		{
			writeF(buf, x);
			writeF(buf, y);
			writeF(buf, z);
		}
	}
}