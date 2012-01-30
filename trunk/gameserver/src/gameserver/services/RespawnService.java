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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.spawn.SpawnTime;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.gametime.DayTime;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.world.World;
import java.util.concurrent.Future;

public class RespawnService
{
	/**
	 * @param npc
	 * @return Future<?>
	 */
	public static Future<?> scheduleDecayTask(final Npc npc)
	{
		int respawnInterval = npc.getSpawn().getSpawnGroup().getInterval();
		int decayInterval = Math.round(respawnInterval * 0.8f);
		if(decayInterval > 240)
			decayInterval = 240;
		
		return ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				npc.getController().onDespawn(false);
			}
		}, decayInterval * 1000);
	}
	/**
	 * 
	 * @param visibleObject
	 */
	public static Future<?> scheduleRespawnTask(final VisibleObject visibleObject)
	{
		final World world = World.getInstance();
		final int interval = visibleObject.getSpawn().getSpawnGroup().getInterval();
	
		return ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				SpawnTime spawnTime = visibleObject.getSpawn().getSpawnGroup().getSpawnTime();
				if(spawnTime != null)
				{
					DayTime dayTime = GameTimeManager.getGameTime().getDayTime();
					if(!spawnTime.isAllowedDuring(dayTime))
						return;
				}
				
				int instanceId = visibleObject.getInstanceId();
				int worldId = visibleObject.getSpawn().getWorldId();
				boolean instanceExists = InstanceService.isInstanceExist(worldId, instanceId);
				
				if(visibleObject.getSpawn().isNoRespawn(instanceId) || !instanceExists)
				{
					visibleObject.getController().delete();
				}
				else
				{
					visibleObject.getSpawn().getSpawnGroup().exchangeSpawn(visibleObject);
					world.setPosition(visibleObject, worldId, visibleObject.getSpawn().getX(), visibleObject.getSpawn().getY(), visibleObject.getSpawn().getZ(), visibleObject.getSpawn().getHeading());
					visibleObject.getController().onRespawn();
					world.spawn(visibleObject);
				}
			}
			
		}, interval * 1000);
	}
}
