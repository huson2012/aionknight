/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.skill.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.utils.PacketSendUtility;



/**
 * @author kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrowCheckCondition")
public class ArrowCheckCondition
    extends Condition
{

	@Override
	public boolean verify(Skill skill)
	{
		if (skill.getEffector() instanceof Player)
		{
			Player player = (Player)skill.getEffector();
			if (player.getEquipment().isArrowEquipped())
				return true;

			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANT_ATTACK_NO_ARROW);
			return false;
		}
		else
		{
			return true;
		}
	}
}
