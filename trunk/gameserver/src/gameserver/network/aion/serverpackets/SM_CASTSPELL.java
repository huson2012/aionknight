/**
 * This file is part of Aion-Knight <aionunique.smfnew.com>.
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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