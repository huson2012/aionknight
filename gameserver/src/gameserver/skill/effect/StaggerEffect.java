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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import gameserver.skill.model.Effect;
import gameserver.skill.model.SpellStatus;
import gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**

 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StaggerEffect")
public class StaggerEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect, StatEnum.STAGGER_RESISTANCE, SpellStatus.STAGGER);
	}

	@Override
	public void startEffect(Effect effect)
	{
		final Creature effected = effect.getEffected();
		effected.getController().cancelCurrentSkill(); 
		effect.setAbnormal(EffectId.STAGGER.getEffectId());
		effect.getEffected().getEffectController().setAbnormal(EffectId.STAGGER.getEffectId());
		PacketSendUtility.broadcastPacketAndReceive(effect.getEffected(), new SM_FORCED_MOVE(effect.getEffector(), effect.getEffected()));
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		effect.getEffected().getEffectController().unsetAbnormal(EffectId.STAGGER.getEffectId());
	}
	
	
}
