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

				if(effectedList.size() == 0)
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

				if(effectedList.size() == 0)
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
