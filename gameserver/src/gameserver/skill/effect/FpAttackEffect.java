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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FpAttackEffect")
public class FpAttackEffect extends AbstractOverTimeEffect
{
  	@XmlAttribute
	protected boolean	percent;	

	@Override
	public void calculate(Effect effect)
	{
		// Only players have FP
		if (!(effect.getEffected() instanceof Player))
			return;

		super.calculate(effect);
	}

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}
	
	@Override
	public void onPeriodicAction(Effect effect)
	{
		/** No Cast exception here because this is called
			only if effected is Player (see startEffect below) **/
		Player effected = (Player)effect.getEffected();
		int maxFP = effected.getLifeStats().getMaxFp();
		int newValue = value;
		// Support for values in percentage
		if (percent)
			newValue = (int)((maxFP * value)/100);
		effected.getLifeStats().reduceFp(newValue);		
	}
}
