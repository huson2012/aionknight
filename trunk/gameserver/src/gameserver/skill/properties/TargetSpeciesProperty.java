/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.skill.properties;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Skill;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.Iterator;
import java.util.TreeSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetSpeciesProperty")
public class TargetSpeciesProperty extends Property
{

	@XmlAttribute(required = true)
	protected TargetSpeciesAttribute	value;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link TargetSpeciesAttribute }
	 * 
	 */
	public TargetSpeciesAttribute getValue()
	{
		return value;
	}

	@Override
	public boolean set(Skill skill)
	{
		TreeSet<CreatureWithDistance> effectedList = skill.getEffectedList();
		if (skill.getFirstTarget() == null && skill.getTargetType() == 1)
			return true;
		else if (skill.getFirstTarget() == null)
			return false;
		
		switch(value)
		{
			case PC:
				if (!(skill.getFirstTarget() instanceof Player) && skill.getEffector() instanceof Player && !skill.checkNonTargetAOE())
				{
					PacketSendUtility.sendPacket((Player)skill.getEffector(), SM_SYSTEM_MESSAGE.INVALID_TARGET());
					return false;
				}
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					if (nextEffected instanceof Player)
						continue;

					iter.remove();
				}

				break;
			case NPC:
				if (!(skill.getFirstTarget() instanceof Npc) && !skill.checkNonTargetAOE())
				{
					if (skill.getEffector() instanceof Player)
						PacketSendUtility.sendPacket((Player)skill.getEffector(), SM_SYSTEM_MESSAGE.INVALID_TARGET());
					return false;
				}
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					if (nextEffected instanceof Npc)
						continue;

					iter.remove();
				}

				break;
			case ALL:
			    	if (skill.getEffector() instanceof Npc && !(skill.getEffector().getMaster() instanceof Player))
			    	{
			    	    for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
			    	    {
					Creature nextEffected = iter.next().getCreature();

					if (nextEffected instanceof Player)
					{
					    continue;
					}

					iter.remove();
			    	    }
			    	}
				break;
			case NONE:
				break;
		}
		
		return true;
	}
}
