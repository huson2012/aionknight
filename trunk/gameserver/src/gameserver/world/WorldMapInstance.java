/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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
		return getParent().getMapId();
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
		return getParent().getWorld();
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