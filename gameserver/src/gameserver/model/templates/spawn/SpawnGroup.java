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

package gameserver.model.templates.spawn;

import commons.utils.Rnd;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.siege.SiegeRace;
import gameserver.spawn.SpawnHandlerType;
import org.apache.log4j.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "spawn")
public class SpawnGroup
{
	/**
	 * XML attributes
	 * Order should be reversed to XML attributes order
	 */
	@XmlAttribute(name = "time")
	private SpawnTime spawnTime;
	@XmlAttribute(name = "anchor")
	private String anchor;
	@XmlAttribute(name = "handler")
	private SpawnHandlerType handler;
	@XmlAttribute(name = "interval")
	private int interval;
	@XmlAttribute(name = "pool")
	private int pool = 0;
	@XmlAttribute(name = "npcid")
	private int npcid;
	@XmlAttribute(name = "map")
	private int mapid;
	@XmlAttribute(name = "rw")
	private int randomWalk;
	@XmlAttribute(name = "npcid_dr")
	private int siegeNpcId_balaur;
	@XmlAttribute(name = "npcid_da")
	private int siegeNpcId_asmodians;
	@XmlAttribute(name = "npcid_li")
	private int siegeNpcId_elyos;
	@XmlAttribute(name = "boss")
	private boolean boss;
	@XmlElement(name = "object")
	private List<SpawnTemplate> objects;
	
	/**
	 * Real-time properties
	 */
	@XmlTransient
	private Map<Integer, Integer> lastSpawnedTemplate = new HashMap<Integer, Integer>();
	
	/**
	 * Constructor used by unmarshaller
	 */
	public SpawnGroup()
	{
	}

	/**
	 * Constructor used to create new spawns not defined in xml
	 * 
	 * @param mapid
	 * @param npcid
	 * @param interval
	 * @param pool
	 */
	public SpawnGroup(int mapid, int npcid, int interval, int pool)
	{
		super();
		this.mapid = mapid;
		this.npcid = npcid;
		this.interval = interval;
		this.pool = pool;
	}
	
	void afterUnmarshal (Unmarshaller u, Object parent)
	{		
		if(objects != null && pool > objects.size())
		{
			Logger.getLogger(SpawnGroup.class).warn(
				"Incorrect pool value for spawn group. MapId:" + mapid + " Npc: " + npcid);
			this.pool = objects.size();
		}

		if(randomWalk != 0)
		{
			for(SpawnTemplate spawn : objects)
			{
				spawn.setRandomWalkNr(randomWalk);
			}
		}
	}

	/**
	 * @return the mapid
	 */
	public int getMapid()
	{
		return mapid;
	}

	/**
	 * @return the npcid
	 */
	public int getNpcid()
	{
		return npcid;
	}

	/**
	 * @return the interval
	 */
	public int getInterval()
	{
		return interval;
	}

	/**
	 * @return the pool
	 */
	public int getPool()
	{
		if(pool == 0)
			pool = size();
		return pool;
	}

	/**
	 * @return the objects
	 */
	public List<SpawnTemplate> getObjects()
	{
		if(this.objects == null)
			this.objects = new ArrayList<SpawnTemplate>();
		
		return this.objects;
	}

	/**
	 * @return the handler
	 */
	public SpawnHandlerType getHandler()
	{
		return handler;
	}

	/**
	 * @return the anchor
	 */
	public String getAnchor()
	{
		return anchor;
	}

	/**
	 * @return the dayTime
	 */
	public SpawnTime getSpawnTime()
	{
		return spawnTime;
	}
	
	/**
	 * Returns next template to spawn
	 * 
	 * @param instance
	 * @return SpawnTemplate
	 */
	public SpawnTemplate getNextAvailableTemplate(int instance)
	{
		for(int i = 0; i < getObjects().size(); i++)
		{
			Integer lastSpawnCounter = lastSpawnedTemplate.get(instance);
			int nextSpawnCounter = lastSpawnCounter == null ? 0 : lastSpawnCounter + 1;
			
			if(nextSpawnCounter >= objects.size())
				nextSpawnCounter = 0;
			
			SpawnTemplate nextSpawn = objects.get(nextSpawnCounter);
			if(nextSpawn.isSpawned(instance))
				continue;
			
			lastSpawnedTemplate.put(instance, nextSpawnCounter);
			
			return nextSpawn;
		}
		return null;
	}
	
	public void clearLastSpawnedTemplate()
	{
		if(!lastSpawnedTemplate.isEmpty())
			lastSpawnedTemplate.clear();
	}
	
	public int size()
	{
		return getObjects().size();
	}

	/**
	 * 
	 */
	public SpawnTemplate getNextRandomTemplate()
	{
		return objects.get(Rnd.get(0, size() - 1));
	}

	/**
	 * Last spawn counter will be reseted during instance respawn
	 * 
	 * @param instanceIndex
	 */
	public void resetLastSpawnCounter(int instanceIndex)
	{
		if(lastSpawnedTemplate.containsKey(instanceIndex))
			lastSpawnedTemplate.remove(instanceIndex);
	}
	
	/**
	 * Check whether pool size is equal to number of defined objects
	 * For such pools no exchange template should be done
	 * 
	 * @return
	 */
	public boolean isFullPool()
	{
		return pool == objects.size();
	}

	/**
	 * @param visibleObject
	 */
	public synchronized void  exchangeSpawn(VisibleObject visibleObject)
	{
		if(isFullPool())
			return;
		
		int instanceId = visibleObject.getInstanceId();
		SpawnTemplate nextSpawn = getNextAvailableTemplate(instanceId);	
		clearLastSpawnedTemplate();
		if(nextSpawn != null)
			visibleObject.setSpawn(nextSpawn);
	}
	
	public int getSiegeNpcIdByRace(SiegeRace race)
	{
		switch(race)
		{
			case ASMODIANS: return siegeNpcId_asmodians;
			case BALAUR: return siegeNpcId_balaur;
			case ELYOS: return siegeNpcId_elyos;
			default: return 0;
		}
	}
	
	public boolean isBoss()
	{
		return boss;
	}
}