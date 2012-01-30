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

import gameserver.model.templates.WorldMapTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Iterator;
import java.util.List;

@XmlRootElement(name="world_maps")
@XmlAccessorType(XmlAccessType.NONE)
public class WorldMapsData implements Iterable<WorldMapTemplate>
{
	@XmlElement(name = "map")
	private List<WorldMapTemplate>	worldMaps;	
	private TIntObjectHashMap<WorldMapTemplate>	worldIdMap	= new TIntObjectHashMap<WorldMapTemplate>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for(WorldMapTemplate map : worldMaps)
		{
			worldIdMap.put(map.getMapId(), map);
		}
	}

	@Override
	public Iterator<WorldMapTemplate> iterator()
	{
		return worldMaps.iterator();
	}

	public int size()
	{
		return worldMaps == null ? 0 : worldMaps.size();
	}

	public WorldMapTemplate getTemplate(int worldId)
	{
		return worldIdMap.get(worldId);
	}
}