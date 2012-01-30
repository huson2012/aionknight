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
import gameserver.skill.model.Effect;
import gameserver.skill.model.Skill;
import java.nio.ByteBuffer;

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
