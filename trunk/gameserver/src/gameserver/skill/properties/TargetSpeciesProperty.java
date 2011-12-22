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
			case NONE:
				break;
		}
		
		return true;
	}
}