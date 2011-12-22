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

package gameserver.skill.condition;

import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Skill;
import gameserver.skill.properties.TargetSpeciesAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelfCondition")
public class SelfCondition
    extends Condition
{

    @XmlAttribute(required = true)
    protected TargetSpeciesAttribute value;
    
    @XmlAttribute
    protected FlyRestrictionAttribute restriction;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *    possible object is
     *    {@link TargetSpeciesAttribute }
     *    
     */
    public TargetSpeciesAttribute getValue() {
        return value;
    }

	@Override
	public boolean verify(Skill skill)
	{
		if(skill.getEffector() == null)
			return false;
		
		//0: regular, 1: fly, 2: glide
		if (skill.getEffector() instanceof Player)
		{
			switch (restriction)
			{
				case GROUND:
					if (((Player)skill.getEffector()).getFlyState() != 0)
						return false;
					break;
				case FLY:
					if (((Player)skill.getEffector()).getFlyState() != 1)
						return false;
					break;
			}
		}
		
		return true;
	}
}