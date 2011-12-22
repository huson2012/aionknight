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