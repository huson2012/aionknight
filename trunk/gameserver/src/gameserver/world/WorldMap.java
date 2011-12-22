/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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