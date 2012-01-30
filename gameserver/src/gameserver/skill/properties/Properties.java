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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
