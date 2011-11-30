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

package gameserver.world;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.ObjectContainer;

import java.util.ArrayList;
import java.util.List;

public class MapRegion extends ObjectContainer
{

	private Integer	regionId;
	private WorldMapInstance parent;
	private ArrayList<MapRegion> neighbours	= new ArrayList<MapRegion>(9);
	private boolean regionActive = false;

	MapRegion(Integer id, WorldMapInstance parent)
	{
		this.regionId = id;
		this.parent = parent;
		this.neighbours.add(this);
	}

	public Integer getMapId()
	{
		return getParent().getMapId();
	}

	public World getWorld()
	{
		return getParent().getWorld();
	}

	public Integer getRegionId()
	{
		return regionId;
	}

	public WorldMapInstance getParent()
	{
		return parent;
	}

	public List<MapRegion> getNeighbours()
	{
		return neighbours;
	}

	void addNeighbourRegion(MapRegion neighbour)
	{
		neighbours.add(neighbour);
	}

	@Override
	public void storeObject (AionObject object)
	{
		super.storeObject(object);
		
		if (!regionActive && object instanceof Player)
			regionActive = true;
	}

	@Override
	public void removeObject(AionObject object)
	{
		super.removeObject(object);
		
		if (getPlayersCount() == 0)
			regionActive = false;
	}
	
	public boolean isMapRegionActive()
	{
		for (MapRegion r : neighbours)
		{
			if (r.regionActive)
				return true;
		}
		return false;
	}
}