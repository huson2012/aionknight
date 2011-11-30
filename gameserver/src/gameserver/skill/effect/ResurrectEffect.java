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

import gameserver.model.TaskId;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_RESURRECT;
import gameserver.skill.model.Effect;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;


/**

 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResurrectEffect")
public class ResurrectEffect extends EffectTemplate
{
	@Override
	public void applyEffect(Effect effect)
	{
		PacketSendUtility.sendPacket((Player) effect.getEffected(), new SM_RESURRECT(effect.getEffector(), effect.getSkillId()));
		
		//add task to player
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				//blank
			}
		}, 5 * 60 * 1000);//5minutes

		((Player)effect.getEffected()).getController().addTask(TaskId.SKILL_RESURRECT, task);

	}

	@Override
	public void calculate(Effect effect)
	{
		if(effect.getEffected() instanceof Player && effect.getEffected().getLifeStats().isAlreadyDead())
			super.calculate(effect);
	}
}
