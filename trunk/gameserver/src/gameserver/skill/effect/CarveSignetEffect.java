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

import commons.utils.Rnd;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SkillTemplate;


/**

 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CarveSignetEffect")
public class CarveSignetEffect extends DamageEffect
{
	@XmlAttribute(required = true)
    protected int signetlvl;
    @XmlAttribute(required = true)
    protected int signetid;
    @XmlAttribute(required = true)
    protected String signet;
    @XmlAttribute(name = "probability",required = true)
    protected int prob;
	    
	@Override
	public void applyEffect(Effect effect)
	{
					
		super.applyEffect(effect);
			
		if (Rnd.get(0, 100) > prob)
			return;
		
		int nextSignetlvl = effect.getCarvedSignet();
		
		Effect placedSignet = getPlacedSignetOnTargt(effect);
		
		
		if(nextSignetlvl > signetlvl || nextSignetlvl > 5)
			return;
		if (placedSignet != null)
			placedSignet.endEffect();
		
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate((signetid-signetlvl) + nextSignetlvl );
		Effect newEffect = new Effect(effect.getEffector(), effect.getEffected(), template, nextSignetlvl, 0);
		newEffect.initialize();
		newEffect.applyEffect();		
			
	}

	@Override
	public void calculate(Effect effect)
	{
		
		Creature effected = effect.getEffected();
		int nextSignetlvl = 1;
		for (int i=1;i <= 5;i++)
		{
			if (effected.getEffectController().getAbnormalEffect(signet+"_"+i) != null)
				nextSignetlvl = i+1;
		}
		
		if (nextSignetlvl == 1 && effect.getSkillTemplate().getStack().contains("SKILL_AS_REFLECTSIGNET"))
			effect.setCarvedSignet(2);
	
		else if (effect.getSkillTemplate().getStack().contains("SKILL_AS_TIGERBEAT"))
			effect.setCarvedSignet(5);


		else
	    effect.setCarvedSignet(nextSignetlvl);

		if(effect.getSkillId() == 2280){
			effect.setCarvedSignet(5);
		}
		
		super.calculate(effect, DamageType.PHYSICAL, true);
				
	}
	
	private Effect getPlacedSignetOnTargt(Effect effect)
	{
		
		Creature effected = effect.getEffected();
		Effect placedSignet = null;
		
		for (int i = 1; i<=signetlvl; i++)
		{
			Effect tmpPlacedSignet = effected.getEffectController().getAbnormalEffect(signet+"_"+i);
			if (tmpPlacedSignet != null)
				placedSignet = tmpPlacedSignet;
					
		}		
		
		return placedSignet;
	}
	
	
}
