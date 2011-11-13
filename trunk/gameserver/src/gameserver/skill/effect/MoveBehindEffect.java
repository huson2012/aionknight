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


import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.action.DamageType;
import gameserver.skill.model.DashParam;
import gameserver.skill.model.Effect;
import gameserver.skill.model.DashParam.DashType;
import gameserver.utils.MathUtil;
import gameserver.world.World;


/**
 * @author Sarynth
 * @edit kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveBehindEffect")
public class MoveBehindEffect extends DamageEffect
{
	@Override
	public void applyEffect(Effect effect)
	{
		Player effector = (Player)effect.getEffector();
		
		DashParam dash = effect.getDashParam();
		World.getInstance().updatePosition(
			effector,
			dash.getX(),
			dash.getY(),
			dash.getZ(),
			(byte)dash.getHeading());
		
		super.applyEffect(effect);
	}
	
	@Override
	public void calculate(Effect effect)
	{
		Creature effected = effect.getEffected();
		
		// Move Effector to Effected
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effected.getHeading()));
		float x1 = (float)(Math.cos(Math.PI + radian) * 1.3F);
		float y1 = (float)(Math.sin(Math.PI + radian) * 1.3F);

		effect.setDashParam(new DashParam(DashType.MOVEBEHIND, effected.getX() + x1, effected.getY() + y1, effected.getZ() + 0.25f, effected.getHeading()));
		
		this.calculate(effect, DamageType.PHYSICAL, true);
	}
	
	
}
