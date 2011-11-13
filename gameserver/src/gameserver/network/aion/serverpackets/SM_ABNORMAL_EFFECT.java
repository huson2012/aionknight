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
import java.util.Collection;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.Effect;

public class SM_ABNORMAL_EFFECT extends AionServerPacket
{	
	private int effectedId;
	private int abnormals;
	private Collection<Effect> effects;
	
	public SM_ABNORMAL_EFFECT(int effectedId, int abnormals,  Collection<Effect> effects)
	{
		this.effects = effects;
		this.abnormals = abnormals;
		this.effectedId = effectedId;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, effectedId); 
		writeC(buf, 1);
		writeD(buf, 0);
		writeD(buf, abnormals);
		writeD(buf, 0);

		writeH(buf, effects.size()); 
		
		for(Effect effect : effects)
		{
			writeH(buf, effect.getSkillId()); 
			writeC(buf, effect.getSkillLevel());
			writeC(buf, effect.getTargetSlot()); 
			writeD(buf, effect.getElapsedTime());
		}
	}
}