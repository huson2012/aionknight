/*
 * This file is part of zetta-core <zetta-core.org>.
 *
 *  zetta-core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  zetta-core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with zetta-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.skill.action.DamageType;
import ru.aionknight.gameserver.skill.model.Effect;
import ru.aionknight.gameserver.utils.ThreadPoolManager;


/**
 * @author Sippolo
 *  
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelayedFPAttackInstantEffect")
public class DelayedFPAttackInstantEffect extends DamageEffect
{
	@XmlAttribute
	protected int delay;
	@XmlAttribute
	protected boolean		percent;

	@Override
	public void calculate(Effect effect)
	{
		if (!(effect.getEffected() instanceof Player))
			return;
		super.calculate(effect, DamageType.MAGICAL, true);
	}
	@Override
	public void applyEffect(final Effect effect)
	{
		final Player effected = (Player)effect.getEffected();
		int maxFP = effected.getLifeStats().getMaxFp();
		final int newValue = (percent) ? (int)((maxFP * value)/100) : value;
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){		
			@Override
			public void run()
			{				
				effected.getLifeStats().reduceFp(newValue);
			}
		}, delay);	
	}
}