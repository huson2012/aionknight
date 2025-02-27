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

import gameserver.controllers.attack.AttackUtil;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelayDamageEffect")
public class DelayDamageEffect extends EffectTemplate
{
	@XmlAttribute
	protected int value;
	
	@XmlAttribute
	protected int delta;
	
	@XmlAttribute
	protected int delay;


	@Override
	public void applyEffect(final Effect effect)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable(){		
			@Override
			public void run()
			{				
				//set type and critical prob
				effect.setDamageType(DamageType.MAGICAL);
				effect.setCriticalProb(criticalProb);
				
				int skillLvl = effect.getSkillLevel();
				int valueWithDelta = value + delta * skillLvl;
				
				int bonusDamage = applyActionModifiers(effect);
			
				// apply pvp damage ratio
				if(effect.getEffected() instanceof Player && effect.getPvpDamage() != 0)
					valueWithDelta = Math.round(valueWithDelta * (effect.getPvpDamage() / 100f));
				
				AttackUtil.calculateMagicalSkillAttackResult(effect, valueWithDelta, getElement(), bonusDamage, true);
				
				effect.getEffected().getController().onAttack(effect.getEffector(), 
					effect.getSkillId(), TYPE.DELAYDAMAGE, effect.getReserved1(), 95, effect.getAttackStatus(), true, true);
				
				//notify observers
				effect.getEffector().getObserveController().notifyAttackObservers(effect.getEffected());
				effect.getEffected().getObserveController().notifyHittedObservers(effect.getEffector(), effect.getDamageType());
			}
		}, delay);	
	}
}
