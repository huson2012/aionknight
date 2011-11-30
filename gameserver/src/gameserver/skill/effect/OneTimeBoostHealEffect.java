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

import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author ViAl
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OneTimeBoostHealEffect")
public class OneTimeBoostHealEffect extends EffectTemplate
{

	@XmlAttribute(required = true)
	protected float percent;

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();		
	}
	
	@Override
	public void startEffect(Effect effect)
	{
		float healRate = effect.getEffected().getController().getHealRate();
		float newRate = healRate+percent/100;
		effect.getEffected().getController().setHealRate(newRate);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		float healRate = effect.getEffected().getController().getHealRate();
		effect.getEffected().getController().setHealRate(healRate-percent/100);
		super.endEffect(effect);
	}

}