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

import gameserver.model.Race;
import gameserver.model.templates.portal.ExitPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@XmlRootElement(name = "portal_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class PortalData
{
	@XmlElement(name = "portal")
	private List<PortalTemplate> portals;
	private TIntObjectHashMap<PortalTemplate> portalData	= new TIntObjectHashMap<PortalTemplate>();
	private HashMap<Integer, ArrayList<PortalTemplate>> instancesMap = new HashMap<Integer, ArrayList<PortalTemplate>>();
	private HashMap<String, PortalTemplate> namedPortals = new HashMap<String, PortalTemplate>();
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		portalData.clear();
		instancesMap.clear();
		namedPortals.clear();
		
		for(PortalTemplate portal : portals)
		{
			portalData.put(portal.getNpcId(), portal);
			if(portal.isInstance())
			{
				for (ExitPoint exit : portal.getExitPoint())
				{
					int mapId = exit.getMapId();
					ArrayList<PortalTemplate> templates = instancesMap.get(mapId);
					if(templates == null)
					{
						templates = new ArrayList<PortalTemplate>();
						instancesMap.put(mapId, templates);
					}
					templates.add(portal);
				}
			}
			if(portal.getName() != null && !portal.getName().isEmpty())
				namedPortals.put(portal.getName(), portal);
		}
	}
	
	public int size()
	{
		return portalData.size();
	}

	public PortalTemplate getPortalTemplate(int npcId)
	{
		return portalData.get(npcId);
	}

	public PortalTemplate getInstancePortalTemplate(int worldId, Race race)
	{
		List<PortalTemplate> portals = instancesMap.get(worldId);
		
		if (portals == null)
		{

			return null;
		}
		
		for(PortalTemplate portal : portals)
		{
			if(portal.getRace() == null || portal.getRace().equals(race))
				return portal;
		}

		return null;
	}

	public PortalTemplate getTemplateByNameAndWorld(int worldId, String name)
	{
		PortalTemplate portal = namedPortals.get(name);

		if(portal == null)
			return null;
		
		for (ExitPoint point : portal.getExitPoint())
		{
			if(portal != null && point.getMapId() != worldId)
				throw new IllegalArgumentException("Invalid combination of world and name: " + worldId + ' ' + name);
		}	
		return portal;
	}

	public List<PortalTemplate> getPortals()
	{
		return portals;
	}

	public void setPortals(List<PortalTemplate> portals)
	{
		this.portals = portals;
		afterUnmarshal(null, null);
	}
}