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
import gameserver.skill.effect.EffectId;
import gameserver.skill.model.Skill;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.EnumSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetStatusProperty")
public class TargetStatusProperty extends Property
{

	@XmlAttribute(name = "value", required = true)
	protected String stateSet;
	
	@XmlTransient
	protected EnumSet<EffectId> value = EnumSet.noneOf(EffectId.class);
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		if (stateSet == null || stateSet.isEmpty())
			return;

		String[] states = stateSet.split(" ");
		for (String state : states)
			value.add(EffectId.valueOf(state));

		stateSet = null;
	}
	
	@Override
	public boolean set(Skill skill)
	{
		if (skill.getEffectedList().size() != 1)
			return false;
		
		Creature effected = skill.getEffectedList().first().getCreature();
		boolean result = false;
		
		for (EffectId as : value)
		{
			if (effected.getEffectController().isAbnormalSet(as))
				result = true;
		}
		
		return result;
	}
}