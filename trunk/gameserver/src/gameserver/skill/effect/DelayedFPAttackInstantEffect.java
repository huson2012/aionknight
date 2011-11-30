/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru].
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

import gameserver.model.gameobjects.player.Player;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

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