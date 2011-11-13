/** This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.skill.condition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Skill;
import gameserver.skill.properties.TargetSpeciesAttribute;

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