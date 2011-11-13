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
package gameserver.skill.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Skill;


/**

 * 
 * Effector: Player only
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DpUseAction")
public class DpUseAction extends Action
{
	@Override
	public void act(Skill skill)
	{
		Player effector = (Player) skill.getEffector();
		int currentDp = effector.getCommonData().getDp();

		if(currentDp <= 0 || currentDp < value)
			return;

		effector.getCommonData().setDp(currentDp - value);	
	}
}
