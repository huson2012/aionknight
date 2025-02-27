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

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**

 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SleepEffect")
public class SleepEffect extends EffectTemplate
{
	@Override
	public void applyEffect(Effect effect)
	{
		//creature can be debuffed with only one sleep at a time
		boolean abort = false;
		for(Effect ef : effect.getEffected().getEffectController().getAbnormalEffects())
		{
			if (effect.getSkillId() == ef.getSkillId())
				continue;
			for(EffectTemplate et : ef.getEffectTemplates())
			{
				if (et.getClass() == this.getClass())
					abort = true;
	
			}
			if (abort)
			{
				ef.endEffect();
				break;
			}
		}

		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, StatEnum.SLEEP_RESISTANCE, null);
	}

	@Override
	public void startEffect(final Effect effect)
	{
		final Creature effected = effect.getEffected();
		effected.getController().cancelCurrentSkill(); 
		effect.setAbnormal(EffectId.SLEEP.getEffectId());
		effected.getEffectController().setAbnormal(EffectId.SLEEP.getEffectId());
		
		effected.getObserveController().attach(
			new ActionObserver(ObserverType.ATTACKED)
			{
				@Override
				public void attacked(Creature creature)
				{
					effected.getEffectController().removeEffect(effect.getSkillId());
				}			
			}
		);
		effected.getObserveController().attach(
			new ActionObserver(ObserverType.DOT)
			{
				@Override
				public void onDot(Creature creature)
				{
					effected.getEffectController().removeEffect(effect.getSkillId());
				}			
			}
		);
	}

	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.SLEEP.getEffectId());
	}
}
