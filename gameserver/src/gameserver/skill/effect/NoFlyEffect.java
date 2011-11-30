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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NoFlyEffect")
public class NoFlyEffect extends EffectTemplate
{
	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void startEffect(Effect effect)
	{
		Creature effected = effect.getEffected();
		if (effected instanceof Player)
		{
			if (((Player)effected).getFlyState() > 0)
			{
				((Player)effected).getFlyController().endFly();
			}
		}
		effect.setAbnormal(EffectId.SHAPECHANGE.getEffectId());
		effected.getEffectController().setAbnormal(EffectId.SHAPECHANGE.getEffectId());
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.SHAPECHANGE.getEffectId());
	}
}
