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



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


import gameserver.controllers.movement.AttackShieldObserver;
import gameserver.skill.model.Effect;



/**
 * @author ginho1
 * @edit kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReflectorEffect")
public class ReflectorEffect extends ShieldEffect
{
	@XmlAttribute
	protected int	radius;

	@Override
	public void calculate(Effect effect)
	{
		this.noresist  = true;
		super.calculate(effect, null, null);
	}
	
	@Override
	public void startEffect(final Effect effect)
	{
		int hit = hitvalue + hitdelta * effect.getSkillLevel();
		
		AttackShieldObserver asObserver = new AttackShieldObserver(hit, radius, percent, effect, attacktype, probability, 1);
		
		effect.getEffected().getObserveController().addAttackCalcObserver(asObserver);
		effect.setAttackShieldObserver(asObserver, position);
	}
}
