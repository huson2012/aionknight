/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


import ru.aionknight.gameserver.controllers.movement.ActionObserver;
import ru.aionknight.gameserver.controllers.movement.ActionObserver.ObserverType;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.stats.CreatureLifeStats;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import ru.aionknight.gameserver.skill.model.Effect;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.skill.model.SkillType;


/**
 * @author ViAl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MagicCounterAtkEffect")
public class MagicCounterAtkEffect extends EffectTemplate
{
	@XmlAttribute
	protected int	percent;
	@XmlAttribute
	protected int	maxdmg;
	
	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}
	
	@Override
	public void startEffect(final Effect effect)
	{
		final Creature effector = effect.getEffector();
		final Creature effected = effect.getEffected();
		final CreatureLifeStats<? extends Creature> cls = effect.getEffected().getLifeStats();
		ActionObserver observer = null;
		
		observer = new ActionObserver(ObserverType.SKILLUSE)
			{
				@Override
				public void skilluse(Skill skill)
				{
					if (skill.getSkillTemplate().getType()==SkillType.MAGICAL)
					{
						if (cls.getMaxHp()/100*percent<=maxdmg)
							effected.getController().onAttack(effector, effect.getSkillId(), TYPE.HP, cls.getMaxHp()/100*percent,effect.getAttackStatus(), true);
						else
							effected.getController().onAttack(effector, maxdmg, effect.getAttackStatus(), true);
					}
						
				}
			};

		effect.setActionObserver(observer, position);
		effected.getObserveController().addObserver(observer);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		ActionObserver observer = effect.getActionObserver(position);
		if (observer != null)
			effect.getEffected().getObserveController().removeObserver(observer);
	}
}
