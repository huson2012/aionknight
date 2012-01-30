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
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;



/**
 * @author Sippolo
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FpAttackInstantEffect")
public class FpAttackInstantEffect extends EffectTemplate
{
	@XmlAttribute
	protected int		value;
	@XmlAttribute
	protected boolean		percent;
	@Override
	public void applyEffect(Effect effect)
	{
		Player player = (Player)effect.getEffected();
		int maxFP = player.getLifeStats().getMaxFp();
		int newValue = value;
		// Support for values in percentage
		if (percent)
			newValue = (int)((maxFP * value)/100);
		player.getLifeStats().reduceFp(newValue);
	}

	@Override
	public void calculate(Effect effect)
	{
		if (effect.getEffected() instanceof Player)
			super.calculate(effect);
	}
}
