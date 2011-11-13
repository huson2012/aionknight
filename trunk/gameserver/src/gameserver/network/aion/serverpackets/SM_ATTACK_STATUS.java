/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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

import java.nio.ByteBuffer;
import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

public class SM_ATTACK_STATUS extends AionServerPacket
{
    private Creature creature;
    private TYPE type;
    private int skillId;
    private int value;
    private int logId;
   
    public static enum TYPE
    {
    	NATURAL_HP(3), USED_HP(4), REGULAR(5), HP(7), DELAYDAMAGE(10),
    	FALL_DAMAGE(17), HEALED_MP(19), ABSORBED_MP(20), MP(21),
    	NATURAL_MP(22), FP_RINGS(23), FP(25), NATURAL_FP(26);
    	
    	private int value;
    	
    	private TYPE(int value)
    	{
    		this.value = value;
    	}
    	
    	public int getValue()
    	{
    		return this.value;
    	}
    }
	
    public SM_ATTACK_STATUS(Creature creature, TYPE type, int value, int skillId, int logId)
    {
    	this.creature = creature;
		this.type = type;
		this.skillId = skillId;
		this.value = value;
    	this.logId = logId;
    }
    
    public SM_ATTACK_STATUS(Creature creature, TYPE type, int value)
    {
    	this.creature = creature;
		this.type = type;
		this.skillId = 0;
		this.value = value;
    	this.logId = 170;
    }
    
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, creature.getObjectId());
		writeD(buf, value);
		writeC(buf, type.getValue());
		writeC(buf, creature.getLifeStats().getHpPercentage());
		writeH(buf, skillId);
		writeH(buf, logId);
	}	
}