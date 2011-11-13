/**
 * This file is part of Aion-Knight [http://aion-knight.ru]
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
import javax.xml.bind.annotation.XmlType;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skill.model.Effect;
import gameserver.skill.model.HealType;


/**

 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealInstantEffect")
public class HealInstantEffect
	extends AbstractHealEffect
{
	
	@Override
	public void applyEffect(Effect effect)
	{
		super.applyEffect(effect,HealType.HP);
		if((effect.getSkillId() == 1788 || effect.getSkillId() == 1771) && effect.getEffector() instanceof Player)
		{
		    // give 30% of maxPM to effector
		    Player player = (Player)effect.getEffector();
		    player.getLifeStats().increaseMp(TYPE.MP, (player.getLifeStats().getMaxMp()/100)*(effect.getSkillId() == 1788 ? 30 : 25));
		}
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect,HealType.HP,true);
	}
}
