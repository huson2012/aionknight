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

import java.nio.ByteBuffer;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.skill.model.Effect;
import gameserver.skill.model.Skill;

public class SM_CASTSPELL_END extends AionServerPacket
{
	private Skill skill;

	public SM_CASTSPELL_END(Skill skill)
	{
		this.skill = skill;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, skill.getEffector().getObjectId());
		writeC(buf, skill.getTargetType());
		switch(skill.getTargetType())
		{
			case 0:
				writeD(buf, skill.getFirstTarget().getObjectId());
				break;
			case 1:
				writeF(buf, skill.getX());
				writeF(buf, skill.getY());
				writeF(buf, skill.getZ() + 0.4f);
				break;
			case 3:
				writeD(buf, 0);
				break;
		}
		writeH(buf, skill.getSkillTemplate().getSkillId());
		writeC(buf, skill.getSkillLevel());
		writeD(buf, skill.getSkillTemplate().getCooldown());
		writeH(buf, skill.getTime());
		writeC(buf, 0);

		if (skill.getChainSuccess())
			writeH(buf, 32);
		else if (skill.getEffects().isEmpty())
			writeH(buf, 16);
		else	
			writeH(buf, 0);
			
		if (skill.getDashParam() == null)
			writeC(buf, 0);
		else
		{
			writeC(buf, skill.getDashParam().getType());
			switch(skill.getDashParam().getType())
			{
				case 1:
				case 2:
				case 4:
					writeC(buf, skill.getDashParam().getHeading());
					writeF(buf, skill.getDashParam().getX());
					writeF(buf, skill.getDashParam().getY());
					writeF(buf, skill.getDashParam().getZ());
					break;
				default:
					break;
			}
		}

		writeH(buf, skill.getEffects().size());

		for(Effect effect : skill.getEffects())
		{
			int effectorMaxHp = effect.getEffector().getLifeStats().getMaxHp();
			int effectorCurrHp = effect.getEffector().getLifeStats().getCurrentHp();
			if (effect.getEffected() == null)
			{
				writeD(buf, 0);
				writeC(buf, 0);
				writeC(buf, 0);
				writeC(buf, 100 * effectorCurrHp / effectorMaxHp);
			}
			else
			{
				writeD(buf, effect.getEffected().getObjectId());//effectedObjectId
				writeC(buf, 0); // unk

				int effectedMaxHp = effect.getEffected().getLifeStats().getMaxHp();//effected
				int effectedCurrHp = effect.getEffected().getLifeStats().getCurrentHp();//effected
				
				if (effect.getReserved1() > 0)
				{	
					if (effectedCurrHp - effect.getReserved1() <= 0)
						effectedCurrHp = 0;
					else
						effectedCurrHp = effectedCurrHp - effect.getReserved1();
				}
				writeC(buf, 100 * effectedCurrHp / effectedMaxHp);
				writeC(buf, 100 * effectorCurrHp / effectorMaxHp);
			}
			
			writeC(buf, effect.getSpellStatus().getId());
			writeC(buf, 16);
			writeH(buf, 0x00);
			writeC(buf, effect.getCarvedSignet());

			switch(effect.getSpellStatus().getId())
			{
				case 1:
				case 2:
				case 4:
				case 8:
					writeF(buf, effect.getEffected().getX());
					writeF(buf, effect.getEffected().getY());
					writeF(buf, effect.getEffected().getZ() + 0.4f);
					break;
				case 16:
					writeC(buf, effect.getEffected().getHeading());
					break;
				default:
					break;
			}

			writeC(buf, 1);
			if (effect.isMpHeal())
				writeC(buf, 1);
			else
				writeC(buf, 0);
			writeD(buf, effect.getReserved1());
			writeC(buf, effect.getAttackStatus().getId());
			writeC(buf, effect.getShieldType());

			switch(effect.getShieldType())
			{
				case 0:
				case 2:
					break;
				default:
					writeD(buf, 0x00);
					writeD(buf, 0x00);
					writeD(buf, 0x00);
					writeD(buf, effect.getReflectorDamage());
					writeD(buf, effect.getReflectorSkillId());
					break;
			}			
		}
	}
}