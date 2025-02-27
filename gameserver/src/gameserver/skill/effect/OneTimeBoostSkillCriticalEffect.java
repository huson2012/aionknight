/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.skill.effect;

import gameserver.controllers.attack.AttackStatus;
import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.controllers.movement.AttackStatusObserver;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author blakawk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OneTimeBoostSkillCriticalEffect")
public class OneTimeBoostSkillCriticalEffect extends EffectTemplate
{
	@XmlAttribute
	private int count;
	
	@Override
	public void startEffect(final Effect effect)
	{
		super.startEffect(effect);
		
		AttackStatusObserver observer = new AttackStatusObserver(count, AttackStatus.CRITICAL) {

			private int myCount = 0;
			
			@Override
			public boolean checkAttackerStatus(AttackStatus status)
			{
				if (status == AttackStatus.CRITICAL)
				{
					if(myCount++ < count)
					{
						return true;
					}
					else
						effect.getEffected().getEffectController().removeEffect(effect.getSkillId());
				}
				return false;
			}
		};
		
		effect.getEffected().getObserveController().addAttackCalcObserver(observer);
		effect.setAttackStatusObserver(observer, position);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		AttackCalcObserver observer = effect.getAttackStatusObserver(position);
		if (observer != null)
			effect.getEffected().getObserveController().removeAttackCalcObserver(observer);
	}

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}
}
