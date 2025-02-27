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
package gameserver.skill.effect;

import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**

 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpellAtkDrainInstantEffect")
public class SpellAtkDrainInstantEffect extends DamageEffect
{
	@XmlAttribute(name = "hp")
	protected int HPpercent;
	@XmlAttribute(name = "mp")
	protected int MPpercent;
	@XmlAttribute
	protected boolean percent;
	

	@Override
	public void applyEffect(Effect effect)
	{
		super.applyEffect(effect);
		int value = 0;
		//TODO hp drain type
		if(HPpercent > 0)
		{
			value = effect.getReserved1() * HPpercent / 100;
			effect.getEffector().getLifeStats().increaseHp(TYPE.HP, value, effect.getSkillId(), 24);
		}
		
		if(MPpercent > 0)
		{
			value = effect.getReserved1() * MPpercent / 100;
			effect.getEffector().getLifeStats().increaseMp(TYPE.ABSORBED_MP, value, effect.getSkillId(), 24);
		}
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, DamageType.MAGICAL, true);
	}

}
