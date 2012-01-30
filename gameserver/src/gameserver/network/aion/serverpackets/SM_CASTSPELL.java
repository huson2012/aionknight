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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CASTSPELL extends AionServerPacket
{
	private int attackerObjectId;
	private int	spellId;
	private int	level;
	private int	targetType; 
	private int duration;	
	private int	targetObjectId;
	private float x;
	private float y;
	private float z;
	
	public SM_CASTSPELL(int attackerObjectId, int spellId, int level, int targetType, int targetObjectId, int duration)
	{
		this.attackerObjectId = attackerObjectId;
		this.spellId = spellId;
		this.level = level;
		this.targetType = targetType;
		this.targetObjectId = targetObjectId;
		this.duration = duration;
	}
	
	public SM_CASTSPELL(int attackerObjectId, int spellId, int level, int targetType, float x, float y, float z, int duration)
	{
		this(attackerObjectId, spellId, level, targetType, 0, duration);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, attackerObjectId);
		writeH(buf, spellId); 
		writeC(buf, level);
		
		writeC(buf, targetType);
		switch(targetType)
		{
			case 0:
				writeD(buf, targetObjectId); 
				break;
			case 1:
				writeF(buf, x);
				writeF(buf, y);
				writeF(buf, z);
				break;
			case 3:
				writeD(buf, targetObjectId);
				break;
		}
		
		writeH(buf, duration);
		writeC(buf, 0);
		writeF(buf, 1.0F);
		writeC(buf, 1);
	}
}
