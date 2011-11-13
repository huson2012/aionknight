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

package gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import gameserver.model.templates.WorldMapTemplate;

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