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

package gameserver.services;

import java.util.concurrent.Future;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.spawn.SpawnTime;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.gametime.DayTime;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.world.World;

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