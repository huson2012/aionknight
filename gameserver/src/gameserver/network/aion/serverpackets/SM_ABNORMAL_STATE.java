/**
 * This file is part of Aion-Knight <www.Aion-Knight.com>.
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

public class SM_ABNORMAL_STATE extends AionServerPacket
{
	private Collection<Effect> effects;
	private int abnormals;
	
	public SM_ABNORMAL_STATE(Collection<Effect> effects, int abnormals)
	{
		this.effects = effects;
		this.abnormals = abnormals;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, abnormals);
		writeD(buf, 0x00);
		writeH(buf, effects.size()); 

		for(Effect effect : effects)
		{
			writeD(buf, effect.getEffectorId());
			writeH(buf, effect.getSkillId());
			writeC(buf, effect.getSkillLevel());
			writeC(buf, effect.getTargetSlot());
			writeD(buf, effect.getElapsedTime());
		}

	}
}