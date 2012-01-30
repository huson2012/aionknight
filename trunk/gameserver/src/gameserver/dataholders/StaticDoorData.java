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
	
	/** 
	 * A map containing all door templates 
	 */
	private TIntObjectHashMap<StaticDoorTemplate> staticDoorData = new TIntObjectHashMap<StaticDoorTemplate>();

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

	public void setStaticDoors(List<StaticDoorTemplate> staticDoors)
	{
		this.staticDoors = staticDoors;
		afterUnmarshal(null, null);
	}
}