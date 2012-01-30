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

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.stats.modifiers.ObjectContainer;
import gameserver.model.group.PlayerGroup;
import java.util.*;
import java.util.concurrent.Future;

public class WorldMapInstance extends ObjectContainer
{
	public static final int			regionSize			= 500;
	private static final int		maxWorldSize		= 10000;
	private WorldMap				parent;
	private Map<Integer, MapRegion>	regions				= Collections.synchronizedMap(new HashMap<Integer, MapRegion> ());
	protected Set<Integer>			registeredObjects	= Collections.newSetFromMap(Collections.synchronizedMap(new HashMap<Integer, Boolean>()));
	private PlayerGroup				registeredGroup		= null;
	private Future<?>				emptyInstanceTask	= null;
	private int						instanceId;
	private Calendar 				timerEnd 			= null;
	public WorldMapInstance(WorldMap parent, int instanceId)
	{
		super();
		this.parent = parent;
		this.instanceId = instanceId;
	}
	public Integer getMapId()
	{
		return parent.getMapId();
	}
	public WorldMap getParent()
	{
		return parent;
	}

	MapRegion getRegion(VisibleObject object)
	{
		return getRegion(object.getX(), object.getY());
	}

	MapRegion getRegion(float x, float y)
	{
		Integer regionId = getRegionId(x, y);
		MapRegion region = regions.get(regionId);
		if(region == null)
		{
			synchronized(this)
			{
				region = regions.get(regionId);
				if(region == null)
				{
					region = createMapRegion(regionId);
				}
			}
		}
		return region;
	}

	private Integer getRegionId(float x, float y)
	{
		return ((int) x) / regionSize * maxWorldSize + ((int) y) / regionSize;
	}

	private MapRegion createMapRegion(Integer regionId)
	{
		MapRegion r = new MapRegion(regionId, this);
		regions.put(regionId, r);

		int rx = regionId / maxWorldSize;
		int ry = regionId % maxWorldSize;

		for(int x = rx - 1; x <= rx + 1; x++)
		{
			for(int y = ry - 1; y <= ry + 1; y++)
			{
				if(x == rx && y == ry)
					continue;
				int neighbourId = x * maxWorldSize + y;

				MapRegion neighbour = regions.get(neighbourId);
				if(neighbour != null)
				{
					r.addNeighbourRegion(neighbour);
					neighbour.addNeighbourRegion(r);
				}
			}
		}
		return r;
	}

	public World getWorld()
	{
		return parent.getWorld();
	}

	public int getInstanceId()
	{
		return instanceId;
	}

	public boolean isInInstance(int objId)
	{
		return allObjects.containsKey(objId);
	}
	
	public void registerGroup(PlayerGroup group) {
		registeredGroup = group;
		register(group.getGroupId());
	}

	public void register(int objectId)
	{
		registeredObjects.add(objectId);
	}
	
	public void removeRegisteredGroup()
	{
		registeredGroup = null;
	}

	public boolean isRegistered(int objectId)
	{
		return registeredObjects.contains(objectId);
	}

	public Future<?> getEmptyInstanceTask()
	{
		return emptyInstanceTask;
	}

	public void setEmptyInstanceTask(Future<?> emptyInstanceTask)
	{
		this.emptyInstanceTask = emptyInstanceTask;
	}

	public PlayerGroup getRegisteredGroup()
	{
		return registeredGroup;
	}

	public void setTimerEnd(int timeInSeconds)
	{
		timerEnd = Calendar.getInstance();
		timerEnd.setTimeInMillis(Calendar.getInstance().getTimeInMillis() + timeInSeconds*1000);
	}

	public Calendar getTimerEnd()
	{
		return timerEnd;
	}
}