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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.ObjectContainer;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.exceptions.AlreadySpawnedException;
import gameserver.world.exceptions.WorldMapNotExistException;
import org.apache.log4j.Logger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class World extends ObjectContainer
{
	private static final Logger	log	= Logger.getLogger(World.class);
	private Map<Integer, WorldMap>	worldMaps;
	private World()
	{
		super();
		worldMaps	= Collections.synchronizedMap(new HashMap<Integer, WorldMap> ());

		for(WorldMapTemplate template : DataManager.WORLD_MAPS_DATA)
		{
			worldMaps.put(template.getMapId(), new WorldMap(template, this));
		}
		log.info("World: "+worldMaps.size()+" worlds map created.");
	}

	public static final World getInstance()
	{
		return SingletonHolder.instance;
	}
	public WorldMap getWorldMap(int id)
	{
		WorldMap map = worldMaps.get(id);
		if(map == null)
			throw new WorldMapNotExistException("Map: " + id + " not exist!");
		return map;
	}

	public void updatePosition(VisibleObject object, float newX, float newY, float newZ, byte newHeading)
	{
		this.updatePosition(object, newX, newY, newZ, newHeading, true);
	}

	public void updatePosition(VisibleObject object, float newX, float newY, float newZ, byte newHeading, boolean updateKnownList)
	{
		if(!object.isSpawned())
			return;
		
		object.getPosition().setXYZH(newX, newY, newZ, newHeading);

		MapRegion oldRegion = object.getActiveRegion();
		if(oldRegion == null)
		{
			log.warn(String.format("CHECKPOINT: oldregion is null, object coordinates - %d %d %d", object.getX(), object.getY(), object.getY()));
			return;
		}
		
		MapRegion newRegion = oldRegion.getParent().getRegion(object);

		if(newRegion != oldRegion)
		{
			newRegion.storeObject(object);
			oldRegion.removeObject(object);
			object.getPosition().setMapRegion(newRegion);
		}
		if(updateKnownList && (object instanceof Player || object.getKnownList().getPlayersCount() > 0))
			object.updateKnownlist();
	}

	public void setPosition(VisibleObject object, int mapId, float x, float y, float z, byte heading)
	{
		int instanceId = 1;	
		if(object.getWorldId() == mapId)
		{
			instanceId = object.getInstanceId();
		}
		this.setPosition(object, mapId, instanceId, x, y, z, heading);
	}

	public void setPosition(VisibleObject object, int mapId, int instance, float x, float y, float z, byte heading)
	{
		if(object.isSpawned())
			despawn(object);
		object.getPosition().setXYZH(x, y, z, heading);
		object.getPosition().setMapId(mapId);
		object.getPosition().setMapRegion(getWorldMap(mapId).getWorldMapInstanceById(instance).getRegion(object));
	}

	public WorldPosition createPosition(int mapId, float x, float y, float z, byte heading)
	{
		WorldPosition position = new WorldPosition();
		position.setXYZH(x, y, z, heading);
		position.setMapId(mapId);
		position.setMapRegion(getWorldMap(mapId).getWorldMapInstance().getRegion(x, y));
		return position;
	}

	public void spawn(VisibleObject object)
	{
		if(object.isSpawned())
			throw new AlreadySpawnedException();

		if (!allObjects.containsKey(object.getObjectId()))
		{
			storeObject(object);
		}
		
		object.getPosition().setIsSpawned(true);
		if(object.getSpawn() != null)
			object.getSpawn().setSpawned(true, object.getInstanceId());
		object.getActiveRegion().getParent().storeObject(object);
		object.getActiveRegion().storeObject(object);

		object.updateKnownlist();
	}
	
	public void despawn(VisibleObject object)
	{
		despawn(object, false);
	}

	public void despawn(VisibleObject object, boolean instance)
	{	
		if(object.getActiveRegion() != null)
		{
			if(object.getActiveRegion().getParent() != null)
				object.getActiveRegion().getParent().removeObject(object);
			
			if (object.getActiveRegion()!=null)
				object.getActiveRegion().removeObject(object);
		}
		
		object.getPosition().setIsSpawned(false);
		
		if(object.getSpawn() != null)
		{
			object.getSpawn().setSpawned(false, object.getInstanceId());
		}	
		
		object.clearKnownlist();
		
		if(object instanceof Npc && instance)
		{
			Npc npc = (Npc)object;
			npc.stopShoutThread();
			npc.getAi().clearDesires();
			npc.getAi().clearEventHandler();
			npc.getAi().clearStateHandler();
			npc.getEffectController().removeAllEffects();
			npc.getLifeStats().cancelAllTasks();
			npc.getAggroList().clear();
			npc.setPosition(null);
			npc.setObserveController(null);
			npc.setKnownlist(null);
			npc.setGameStats(null);
			npc.setLifeStats(null);
			npc.setMoveController(null);
			npc.setAi(null);
			npc.setEffectController(null);
		}
		
		super.removeObject(object);
	}
	
	@Override
	public void removeObject (AionObject object)
	{
		super.removeObject(object);
		
		if (object instanceof Npc)
			IDFactory.getInstance().releaseId(object.getObjectId());
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final World instance = new World();
	}
}