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
		return parent.getMapId();
	}

	public World getWorld()
	{
		return parent.getWorld();
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
