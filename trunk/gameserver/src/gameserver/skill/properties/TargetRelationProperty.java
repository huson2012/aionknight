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

import gameserver.model.alliance.PlayerAllianceGroup;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.SkillAreaNpc;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Skill;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.Iterator;
import java.util.TreeSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetRelationProperty")
public class TargetRelationProperty extends Property
{

	@XmlAttribute(required = true)
	protected TargetRelationAttribute	value;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link TargetRelationAttribute }
	 * 
	 */
	public TargetRelationAttribute getValue()
	{
		return value;
	}

	@Override
	public boolean set(Skill skill)
	{
		TreeSet<CreatureWithDistance> effectedList = skill.getEffectedList();
		Creature effector = skill.getEffector();
		
		switch(value)
		{
			case ALL:
				break;
			case ENEMY:
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					if(effector.isEnemy(nextEffected))
						continue;

					iter.remove();
				}
				break;
			case FRIEND:
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();
					
					if (nextEffected instanceof SkillAreaNpc)
					{
						iter.remove();
						continue;
					}
					
					if(!effector.isEnemy(nextEffected))
						continue;
					
					iter.remove();
				}

				if(effectedList.isEmpty())
				{
					skill.setFirstTarget(skill.getEffector());
					effectedList.add(new CreatureWithDistance(skill.getEffector(), 0));
				}
				break;
			case MYPARTY:
				for(Iterator<CreatureWithDistance> iter = effectedList.iterator(); iter.hasNext();)
				{
					Creature nextEffected = iter.next().getCreature();

					Player skillEffector = null;
					if (effector.getActingCreature() instanceof Player)
						skillEffector = (Player)effector.getActingCreature();
										
					
					if (nextEffected instanceof Player && skillEffector != null)
					{
						Player player = (Player) nextEffected;
						if(skillEffector.isInAlliance())
						{
							PlayerAllianceGroup pag = skillEffector.getPlayerAlliance()
								.getPlayerAllianceGroupForMember(skillEffector.getObjectId());
							if(pag != null
								&& pag.isInSamePlayerAllianceGroup(skillEffector.getObjectId(), player.getObjectId()))
								continue;
						}
						else if(skillEffector.isInGroup())
						{
							if((skillEffector).getPlayerGroup() != null && player.getPlayerGroup() != null)
							{
								if((skillEffector).getPlayerGroup().getGroupId() == player.getPlayerGroup()
									.getGroupId())
									continue;
							}
						}
						else if(player == skillEffector)
							continue;

					}
					

					iter.remove();
				}

				if(effectedList.isEmpty())
				{
					skill.setFirstTarget(skill.getEffector());
					effectedList.add(new CreatureWithDistance(skill.getEffector(), 0));
				}
				break;
		}
		
		if(effectedList.size() > skill.getMaxEffected())
		{
			Iterator<CreatureWithDistance> iter = effectedList.iterator();
			int i = 1;
			while(iter.hasNext())
			{
				iter.next();
				if (i > skill.getMaxEffected())
					iter.remove();

				i++;
			}
		}
		return true;
	}
}
