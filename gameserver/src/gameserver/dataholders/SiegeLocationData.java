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

import gameserver.model.siege.SiegeLocation;
import gameserver.model.templates.siege.SiegeLocationTemplate;
import org.apache.log4j.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "siege_locations")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiegeLocationData
{
	@XmlElement(name = "siege_location")
	private List<SiegeLocationTemplate> siegeLocationTemplates;

	private HashMap<Integer, SiegeLocation> siegeLocations = new HashMap<Integer, SiegeLocation>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		Logger.getLogger(SiegeLocationData.class).debug("After unmarshal in SiegeLocationData...");
		siegeLocations.clear();
		for (SiegeLocationTemplate template : siegeLocationTemplates)
		{
			Logger.getLogger(SiegeLocationData.class).debug("["+siegeLocations.size()+ '/' +siegeLocationTemplates.size()+"]Loading SiegeLocation #"+template.getId()+" with type "+template.getType());
			switch(template.getType())
			{
				case FORTRESS:
					siegeLocations.put(template.getId(), new SiegeLocation(template));
					break;
				case ARTIFACT:
					siegeLocations.put(template.getId(), new SiegeLocation(template));
					break;
				case BOSSRAID_LIGHT:
				case BOSSRAID_DARK:
					siegeLocations.put(template.getId(), new SiegeLocation(template));
					break;
				default:
					break;
			}
			Logger.getLogger(SiegeLocationData.class).debug("now there is "+siegeLocations.size()+" sieges locations...");
		}
	}
	
	public int size()
	{
		return siegeLocations.size();
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations()
	{
		return siegeLocations;
	}
}