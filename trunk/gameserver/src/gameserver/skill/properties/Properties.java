/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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
package gameserver.skill.properties;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**

 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Properties")
public class Properties 
{
    @XmlElements({
    	@XmlElement(name = "addweaponrange", type = AddWeaponRangeProperty.class),
    	@XmlElement(name = "firsttarget", type = FirstTargetProperty.class),
    	@XmlElement(name = "firsttargetrange", type = FirstTargetRangeProperty.class),
    	@XmlElement(name = "targetrange", type = TargetRangeProperty.class),
    	@XmlElement(name = "targetspecies", type = TargetSpeciesProperty.class),
        @XmlElement(name = "targetrelation", type = TargetRelationProperty.class),
        @XmlElement(name = "targetstatus", type = TargetStatusProperty.class)
    })
    protected List<Property> properties;

    /**
     * Gets the value of the getProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *   getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirstTargetProperty }
     * {@link TargetRangeProperty }
     * {@link AddWeaponRangeProperty }
     * {@link TargetRelationProperty }
     * {@link FirstTargetRangeProperty }
     * 
     * 
     */
    public List<Property> getProperties() 
    {
        if (properties == null) 
        {
        	properties = new ArrayList<Property>();
        }
        
        return this.properties;
    }

}
