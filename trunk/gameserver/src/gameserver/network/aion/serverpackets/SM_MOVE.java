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

import gameserver.controllers.movement.MovementType;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * This packet is displaying movement of players etc.
 */
public class SM_MOVE extends AionServerPacket
{
	/**
	 * Object that is moving.
	 */
	private final int				movingCreatureId;
	private final float				x, y, z, x2, y2, z2;
	private final byte				heading;
	private final MovementType		moveType;
	private final byte				glideFlag;

	private boolean					hasDirection;
	private boolean					hasGlideFlag;

	/**
	 * Constructs new <tt>SM_MOVE</tt> packet
	 * 
	 * @param movingCreature
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param heading
	 * @param glideFlag
	 * @param moveType
	 */
	public SM_MOVE(int movingCreatureId, float x, float y, float z, float x2, float y2, float z2,
		byte heading, byte glideFlag, MovementType moveType)
	{
		this.movingCreatureId = movingCreatureId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.heading = heading;
		this.glideFlag = glideFlag;
		this.moveType = moveType;

		this.hasDirection = true;
		this.hasGlideFlag = true;
	}

	public SM_MOVE(int movingCreatureId, float x, float y, float z, float x2, float y2, float z2,
		byte heading, MovementType moveType)
	{
		this(movingCreatureId, x, y, z, x2, y2, z2, heading, (byte)0, moveType);
		this.hasDirection = true;
		this.hasGlideFlag = false;
	}

	public SM_MOVE(int movingCreatureId, float x, float y, float z, byte heading,
		MovementType moveType)
	{
		this(movingCreatureId, x, y, z, 0, 0, 0, heading, (byte)0, moveType);
		this.hasDirection = false;
		this.hasGlideFlag = false;
	}

	public SM_MOVE(int movingCreatureId, float x, float y, float z, byte heading,
		byte glideFlag, MovementType moveType)
	{
		this(movingCreatureId, x, y, z, 0, 0, 0, heading, glideFlag, moveType);
		this.hasDirection = false;
		this.hasGlideFlag = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, movingCreatureId);
		writeF(buf, x);
		writeF(buf, y);
		writeF(buf, z);
		writeC(buf, heading);
		writeC(buf, moveType.getMovementTypeId());

		if(this.hasDirection)
		{
			writeF(buf, x2);
			writeF(buf, y2);
			writeF(buf, z2);
		}

		if(this.hasGlideFlag)
		{
			writeC(buf, glideFlag);
		}
	}
}
