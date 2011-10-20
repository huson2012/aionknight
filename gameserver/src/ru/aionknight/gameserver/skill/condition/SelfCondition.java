/* This file is part of aion-unique <aion-unique.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.skill.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.skill.properties.TargetSpeciesAttribute;



/**
 * @author kecimis
 * 
 */
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
     *     possible object is
     *     {@link TargetSpeciesAttribute }
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
