/*
 * This file is part of aion-unique <aion-unique.org>.
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

package gameserver.dataholders;

import gameserver.model.templates.staticdoor.StaticDoorTemplate;
import gnu.trove.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "staticdoor_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticDoorData
{
	@XmlElement(name = "staticdoor")
	private List<StaticDoorTemplate> staticDoors;
	
	/** A map containing all door templates */
	private TIntObjectHashMap<StaticDoorTemplate> staticDoorData	= new TIntObjectHashMap<StaticDoorTemplate>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		staticDoorData.clear();
		
		for(StaticDoorTemplate staticDoor : staticDoors)
		{
			staticDoorData.put(staticDoor.getDoorId(), staticDoor);
		}
	}
	
	public int size()
	{
		return staticDoorData.size();
	}
	
	/**
	 * 
	 * @param doorId
	 * @return
	 */
	public StaticDoorTemplate getStaticDoorTemplate(int doorId)
	{
		return staticDoorData.get(doorId);
	}

	/**
	 * @return the static doors
	 */
	public List<StaticDoorTemplate> getStaticDoors()
	{
		return staticDoors;
	}

	/**
	 *
	 */
	public void setStaticDoors(List<StaticDoorTemplate> staticDoors)
	{
		this.staticDoors = staticDoors;
		afterUnmarshal(null, null);
	}
}