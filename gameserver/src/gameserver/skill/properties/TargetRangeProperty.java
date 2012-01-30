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

import gameserver.geo.GeoEngine;
import gameserver.model.alliance.PlayerAllianceMember;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.skill.model.CreatureWithDistance;
import gameserver.skill.model.Skill;
import gameserver.utils.MathUtil;
import org.apache.log4j.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.TreeSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetRangeProperty")
public class TargetRangeProperty
extends Property
{

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TargetRangeProperty.class);
	
	@XmlAttribute(required = true)
	protected TargetRangeAttribute value;

	@XmlAttribute
	protected int distance;
	
	@XmlAttribute
	protected int maxcount;

	/**
	 * Gets the value of the value property.
	 *    
	 */
	public TargetRangeAttribute getValue() {
		return value;
	}
	public int getDistance() {
		return distance;
	}

	@Override
	public boolean set(final Skill skill)
	{
		final TreeSet<CreatureWithDistance> effectedList = skill.getEffectedList();
		
		skill.setTargetRangeAttribute(value);
		switch(value)
		{
			case ONLYONE:
				skill.setMaxEffected(1);
				break;
			case AREA:	
				final Creature firstTarget = skill.getFirstTarget();
				
				skill.setMaxEffected(maxcount);
				
				final float newDistance = (float)distance + (skill.getAddWeaponRangeProperty()?((float)skill.getEffector().getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f):0) + 1.5f;
				
				skill.getEffector().getKnownList().doOnAllObjects(new Executor<AionObject>(){	
					@Override
					public boolean run(AionObject nextObject)
					{
						// firstTarget is already added, look: FirstTargetProperty
						if(firstTarget == nextObject)
							return true;

						if(!(nextObject instanceof Creature))
							return true;
						
						Creature nextCreature = (Creature) nextObject;
						
						// Creature with no life stats are not supposed to be attacked
						if(nextCreature.getLifeStats() == null || nextCreature.getLifeStats().isAlreadyDead())
							return true;
						
						// TODO maybe better controller?
						if (skill.getTargetType() == 1)// point-area skill
						{
							if (MathUtil.getDistance(nextCreature, skill.getX(), skill.getY(), skill.getZ()) <= newDistance &&
								GeoEngine.getInstance().canSee(skill.getEffector().getWorldId(), skill.getX(), skill.getY(), skill.getZ(), nextCreature.getX(), nextCreature.getY(), nextCreature.getZ()))
								effectedList.add(new CreatureWithDistance(nextCreature, (float)MathUtil.getDistance(nextCreature, skill.getX(), skill.getY(), skill.getZ())));
						}
						else 
						{	
							if (MathUtil.isIn3dRange(firstTarget, nextCreature, newDistance) &&
								GeoEngine.getInstance().canSee(firstTarget, nextCreature))
								effectedList.add(new CreatureWithDistance(nextCreature, (float)MathUtil.getDistance(nextCreature, firstTarget)));
						}
						return true;
					}	
				}, true);
				if (skill.getFirstTarget() == null && !effectedList.isEmpty())
					skill.setFirstTarget(effectedList.first().getCreature());
				break;
			case PARTY:
				if(skill.getEffector() instanceof Player)
				{
					skill.setMaxEffected((maxcount == 0 ? 6 : maxcount));
					
					if(skill.getMaxEffected() == effectedList.size())
						break;
					
					Player effector = (Player)skill.getEffector();
					if (effector.isInAlliance())
					{
						effectedList.clear();
						for(PlayerAllianceMember allianceMember : effector.getPlayerAlliance().getMembersForGroup(effector.getObjectId()))
						{
							if (!allianceMember.isOnline()) continue;
							Player member = allianceMember.getPlayer();
							if(MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5))&&
								GeoEngine.getInstance().canSee(effector, member))
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
						}
					}
					else if (effector.isInGroup())
					{
						effectedList.clear();
						for(Player member : effector.getPlayerGroup().getMembers())
						{
							// TODO maybe better controller?
							if(member != null && MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5))&&
								GeoEngine.getInstance().canSee(effector, member))
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
						}
					}
				}
				break;
			case NONE:
			case POINT:
				//no effecteds
				break;
			case PARTY_WITHPET:
				if(skill.getEffector() instanceof Player)
				{
					skill.setMaxEffected((maxcount == 0 ? 12 : maxcount));
					Player effector = (Player)skill.getEffector();
					if (effector.isInAlliance())
					{
						effectedList.clear();
						for(PlayerAllianceMember allianceMember : effector.getPlayerAlliance().getMembersForGroup(effector.getObjectId()))
						{
							if (!allianceMember.isOnline()) continue;
							Player member = allianceMember.getPlayer();
							if(MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5)) &&
								GeoEngine.getInstance().canSee(effector, member))
							{
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
								if (member.getSummon() != null && GeoEngine.getInstance().canSee(effector, member.getSummon()))
									effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
							}
						}
					}
					else if (effector.isInGroup())
					{
						effectedList.clear();
						for(Player member : effector.getPlayerGroup().getMembers())
						{
							// TODO maybe better controller?
							if(member != null && MathUtil.isIn3dRange(effector, member, (float)(distance + 1.5)) &&
								GeoEngine.getInstance().canSee(effector, member))
							{
								effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member)));
								if (member.getSummon() != null && GeoEngine.getInstance().canSee(effector, member.getSummon()))
									effectedList.add(new CreatureWithDistance(member, (float)MathUtil.getDistance(effector, member.getSummon())));
							}
						}
					}
				}
				break;
		}
		return true;
	}
}
