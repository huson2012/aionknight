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
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Effect;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DualMasteryEffect")
public class DualMasteryEffect extends BufEffect
{

	@XmlAttribute
	protected int value;
	
	@Override
	public void calculate(Effect effect)
	{
		
		//right now only players are affected
		Player player = (Player)  effect.getEffector();
		//check best mastery skill
		Integer skillId = player.getSkillList().getDualMasterySkill();
		if(skillId != null && skillId != effect.getSkillId())
			return;
		//check weather already skill applied and weapons isEquipeed
		boolean dualMasterySet = player.getEffectController().isDualMasterySet(skillId);
		boolean weaponsEquiped = player.getEquipment().isDualWieldEquipped();
		if(!dualMasterySet && weaponsEquiped)
			super.calculate(effect);
	}

	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		Player player = (Player)  effect.getEffector();
		player.getEffectController().unsetDualMastery();
		player.getEffectController().unsetDualEffect();
	}

	@Override
	public void startEffect(Effect effect)
	{
		Player player = (Player)  effect.getEffector();
		player.getEffectController().removePassiveEffect(player.getEffectController().getDualMastery());
		super.startEffect(effect);
		player.getEffectController().setDualMastery(effect.getSkillId());
		player.getEffectController().setDualEffect(value);
	}	
}