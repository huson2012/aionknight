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

import gameserver.model.templates.WorldMapTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WorldMap
{
	private WorldMapTemplate worldMapTemplate;
	private AtomicInteger nextInstanceId = new AtomicInteger(0);
	private Map<Integer, WorldMapInstance> instances = Collections.synchronizedMap(new HashMap<Integer, WorldMapInstance> ());;
	private World world;
	public WorldMap(WorldMapTemplate worldMapTemplate, World world)
	{
		this.world = world;
		this.worldMapTemplate = worldMapTemplate;
		
		if(worldMapTemplate.getTwinCount() != 0)
		{
			for(int i = 1; i <= worldMapTemplate.getTwinCount(); i++)
			{
				int nextId = getNextInstanceId();
				addInstance(nextId, new WorldMapInstance(this, nextId));	
			}			
		}	
		else
		{
			int nextId = getNextInstanceId();
			addInstance(nextId, new WorldMapInstance(this, nextId));
		}
	}

	public String getName()
	{
		return worldMapTemplate.getName();
	}

	public int getWaterLevel()
	{
		return worldMapTemplate.getWaterLevel();
	}

	public int getDeathLevel()
	{
		return worldMapTemplate.getDeathLevel();
	}

	public WorldType getWorldType()
	{
		return worldMapTemplate.getWorldType();
	}

	public Integer getMapId()
	{
		return worldMapTemplate.getMapId();
	}

	public int getInstanceCount()
	{
		int twinCount = worldMapTemplate.getTwinCount();
		return twinCount > 0 ? twinCount : 1;
	}

	public WorldMapInstance getWorldMapInstance()
	{
		return getWorldMapInstance(1);
	}

	public WorldMapInstance getWorldMapInstanceById(int instanceId)
	{
		if(worldMapTemplate.getTwinCount() !=0)
		{
			if(instanceId > worldMapTemplate.getTwinCount())
			{
				throw new IllegalArgumentException("WorldMapInstance " + worldMapTemplate.getMapId() + " has lower instances count than " + instanceId);
			}		
		}
		return getWorldMapInstance(instanceId);
	}

	private WorldMapInstance getWorldMapInstance(int instanceId)
	{
		return instances.get(instanceId);
	}

	public void removeWorldMapInstance(int instanceId)
	{
		instances.remove(instanceId);
	}

	public void addInstance(int instanceId, WorldMapInstance instance)
	{
		instances.put(instanceId, instance);
	}

	public World getWorld()
	{
		return world;
	}

	public int getNextInstanceId()
	{
		return nextInstanceId.incrementAndGet();
	}

	public boolean isInstanceType()
	{
		return worldMapTemplate.isInstance();
	}

	public Collection<WorldMapInstance> getInstances()
	{
		return instances.values();
	}

	public Set<Integer> getInstanceIds()
	{
		return instances.keySet();
	}
	
}