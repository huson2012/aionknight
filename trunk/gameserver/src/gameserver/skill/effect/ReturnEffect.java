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
import javax.xml.bind.annotation.XmlType;


import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.skill.model.Effect;
import gameserver.world.World;


/**

 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnEffect")
public class ReturnEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{	
if(effect.getEffected() instanceof Player){
		if(effect.getEffected().getWorldId() == 300200000)
			return;
		else if(effect.getEffected().getWorldId() == 310100000)
		return;
		else if(effect.getEffected().getWorldId() == 300030000)
		return;
     }
		TeleportService.moveToBindLocation((Player) effect.getEffected(), true, 500);
	}

	@Override
	public void calculate(Effect effect)
	{
		if(effect.getEffected().isSpawned())
			super.calculate(effect);
	}
}
