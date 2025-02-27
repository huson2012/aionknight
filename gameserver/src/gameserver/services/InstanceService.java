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

package gameserver.services;

import commons.utils.Rnd;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.group.PlayerGroup;
import gameserver.model.instances.Dredgion;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.model.templates.portal.EntryPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import gameserver.world.WorldMap;
import gameserver.world.WorldMapInstance;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstanceService
{
	private static Logger	log	= Logger.getLogger(InstanceService.class);

	/**
	 * @param worldId
	 * @param destroyTime
	 * @return
	 */
	public synchronized static WorldMapInstance getNextAvailableInstance(int worldId)
	{
		WorldMap map = World.getInstance().getWorldMap(worldId);

		if(!map.isInstanceType())
			throw new UnsupportedOperationException("Invalid call for next available instance  of " + worldId);

		int nextInstanceId = map.getNextInstanceId();

		log.info("Creating new instance: " + worldId + ' ' + nextInstanceId);

		WorldMapInstance worldMapInstance = new WorldMapInstance(map, nextInstanceId);
		startInstanceChecker(worldMapInstance);
		map.addInstance(nextInstanceId, worldMapInstance);
		SpawnEngine.getInstance().spawnInstance(worldId, worldMapInstance.getInstanceId());
		
		return worldMapInstance;
	}

	/**
	 * Instance will be destroyed All players moved to bind location All objects - deleted
	 */
	private static void destroyInstance(WorldMapInstance instance)
	{
		instance.getEmptyInstanceTask().cancel(false);
		
		final int worldId = instance.getMapId();
		int instanceId = instance.getInstanceId();

		WorldMap map = World.getInstance().getWorldMap(worldId);
		map.removeWorldMapInstance(instanceId);

		log.info("Destroying instance:" + worldId + ' ' + instanceId);

		instance.doOnAllObjects(new Executor<AionObject>(){
			@Override
			public boolean run(AionObject obj)
			{
				if(obj instanceof Player)
				{			
					Player player = (Player) obj;
					if(DredgionInstanceService.isDredgion(worldId))
						TeleportService.moveToBindLocation(player, true);
					else
					{
						PortalTemplate portal = DataManager.PORTAL_DATA.getInstancePortalTemplate(worldId, player.getCommonData().getRace());
						moveToEntryPoint((Player) obj, portal, true);
					}
				}
				else if (obj instanceof VisibleObject)
				{
					((VisibleObject)obj).getController().delete();
				}
				return true;
			}
		}, true);
	}
	

	public static VisibleObject addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading, boolean noRespawn)
	{
		SpawnTemplate spawn = SpawnEngine.getInstance().addNewSpawn(worldId, instanceId, templateId, x, y, z, heading, 0, 0, noRespawn);
		return SpawnEngine.getInstance().spawnObject(spawn, instanceId);
	}

	
	/**
	 * 
	 * @param instance
	 * @param player
	 */
	public static void registerPlayerWithInstance(WorldMapInstance instance, Player player)
	{
		instance.register(player.getObjectId());
	}
	
	/**
	 * 
	 * @param instance
	 * @param group
	 */
	public static void registerGroupWithInstance(WorldMapInstance instance, PlayerGroup group)
	{
		group.setInstanceStartTimeNow();
		group.setGroupInstancePoints(0);
		instance.registerGroup(group);
	}
	
	/**
	 * 
	 * @param worldId
	 * @param objectId
	 * @return instance or null
	 */
	public static WorldMapInstance getRegisteredInstance(int worldId, int objectId)
	{
		for (WorldMapInstance instance : World.getInstance().getWorldMap(worldId).getInstances())
		{
			if(instance.isRegistered(objectId))
				return instance;
		}
		return null;
	}
	
	public static Map<Integer, Integer> getTimeInfo(Player player)
	{
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		long currentTime = Calendar.getInstance().getTimeInMillis();
        int remainingTime;
		
		if(!player.getInstanceCDs().isEmpty())
		{
			for(int i : player.getInstanceCDs().keys())
			{
				remainingTime = (int) ((player.getInstanceCD(i).getCDEndTime().getTime() - currentTime) / 1000);
				if(remainingTime < 0)
					remainingTime = 0;
				result.put(i, remainingTime);
			}
		}
		
		return result;
	}
	
	public static boolean canEnterInstance(Player player, int instanceMapId, int instanceId)
	{
		if(player.getInstanceCD(instanceMapId) == null)
			return true;
		else
		{
			Timestamp endTime = player.getInstanceCD(instanceMapId).getCDEndTime();
			if(endTime.getTime() - System.currentTimeMillis() > 0)
			{
                return player.getInstanceCD(instanceMapId).getInstanceId() == instanceId && player.getPlayerGroup().getGroupId() == player.getInstanceCD(instanceMapId).getGroupId();
			}
			else
			{
				player.removeInstanceCD(instanceMapId);
				return true;
			}
		}
	}

	/**
	 * @param player
	 */
	public static void onPlayerLogin(Player player)
	{
		int worldId = player.getWorldId();
		
		WorldMapTemplate worldTemplate = DataManager.WORLD_MAPS_DATA.getTemplate(worldId);
		if(worldTemplate.isInstance())
		{
			if(DredgionInstanceService.isDredgion(worldId))
			{
				TeleportService.moveToBindLocation(player, true);
				return;
			}

			PortalTemplate portalTemplate = null;
			
			try {
				portalTemplate = DataManager.PORTAL_DATA.getInstancePortalTemplate(worldId, player.getCommonData().getRace());
			} catch (IllegalArgumentException e)
			{
				log.error("No portal template found for " + worldId);
				return;
			}
			
			if (portalTemplate == null)
			{
				log.error("No portal template found for " + worldId);
				return;
			}
			
			int lookupId = player.getObjectId();
			if(portalTemplate.isGroup() && player.getPlayerGroup() != null)
			{
				int instanceMapId = DataManager.WORLD_MAPS_DATA.getTemplate(worldId).getInstanceMapId();

				if(player.getInstanceCD(instanceMapId) == null)
					return;
				lookupId = player.getInstanceCD(instanceMapId).getGroupId();
			}

			WorldMapInstance registeredInstance = getRegisteredInstance(worldId, lookupId);
			if(registeredInstance != null)
			{
				World.getInstance().setPosition(player, worldId, registeredInstance.getInstanceId(), player.getX(), player.getY(),
					player.getZ(), player.getHeading());
				return;
			}
			
			moveToEntryPoint(player, portalTemplate, false);
		}
	}
	
	/**
	 * 
	 * @param player
	 * @param portalTemplates
	 */
	public static void moveToEntryPoint(Player player, PortalTemplate portalTemplate, boolean useTeleport)
	{
		EntryPoint entryPoint = null;
		List<EntryPoint> entryPoints = portalTemplate.getEntryPoint();

		for(EntryPoint point : entryPoints)
		{
			if(point.getRace() == null || point.getRace().equals(player.getCommonData().getRace()))
			{
				entryPoint = point;
				break;
			}
		}
		
		if(entryPoint == null)
		{
			log.warn("Entry point not found for " + player.getCommonData().getRace() + " " + player.getWorldId());
			return;
		}
		
		if(useTeleport)
		{
			TeleportService.teleportTo(player, entryPoint.getMapId(), 1,  entryPoint.getX(), entryPoint.getY(),
				entryPoint.getZ(), 0);
		}
		else
		{
			World.getInstance().setPosition(player, entryPoint.getMapId(), 1, entryPoint.getX(), entryPoint.getY(),
				entryPoint.getZ(), player.getHeading());
		}	
		
	}

	/**
	 * @param worldId
	 * @param instanceId
	 * @return
	 */
	public static boolean isInstanceExist(int worldId, int instanceId)
	{
		return World.getInstance().getWorldMap(worldId).getWorldMapInstanceById(instanceId) != null;
	}
	
	/**
	 * 
	 * @param worldMapInstance
	 */
	protected static void startInstanceChecker(WorldMapInstance worldMapInstance)
	{
		int delay = 60000 + Rnd.get(-10, 10);
		worldMapInstance.setEmptyInstanceTask(ThreadPoolManager.getInstance().scheduleAtFixedRate(
			new EmptyInstanceCheckerTask(worldMapInstance), delay, delay));
	}

	private static class EmptyInstanceCheckerTask implements Runnable
	{
		private WorldMapInstance worldMapInstance;

		private EmptyInstanceCheckerTask(WorldMapInstance worldMapInstance)
		{
			this.worldMapInstance = worldMapInstance;
		}

		@Override
		public void run()
		{
			PortalTemplate portalTemplate = DataManager.PORTAL_DATA.getInstancePortalTemplate(worldMapInstance.getMapId(), null);

			if(portalTemplate != null && portalTemplate.isGroup())
			{
				PlayerGroup registeredGroup = worldMapInstance.getRegisteredGroup();

				if(registeredGroup == null)
				{
					if(worldMapInstance.getPlayersCount() == 0)
					{
						destroyInstance(worldMapInstance);
						return;
					}
				}
				else if(registeredGroup.size() == 0)
				{
					destroyInstance(worldMapInstance);
					return;
				}
			}
			else
			{
				if(worldMapInstance.getPlayersCount() == 0)
				{
					destroyInstance(worldMapInstance);
					return;
				}
			}
			
			if(worldMapInstance instanceof Dredgion)
			{				
				Dredgion dred = (Dredgion)worldMapInstance;
				
				PlayerGroup secondGroup = dred.getSecondGroup();
				if(secondGroup == null)
				{
					if(dred.getPlayersCount() == 0)
					{
						destroyInstance(dred);
						return;
					}
				}
				else if(secondGroup.size() == 0)
				{
					destroyInstance(dred);
				}
			}
		}
	}
}