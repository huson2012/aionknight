/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
				if (skill.getFirstTarget() == null && effectedList.size() > 0)
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
