/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.spawn;


import ru.aionknight.gameserver.controllers.StaticObjectController;
import ru.aionknight.gameserver.model.gameobjects.StaticObject;
import ru.aionknight.gameserver.model.gameobjects.VisibleObject;
import ru.aionknight.gameserver.model.templates.VisibleObjectTemplate;
import ru.aionknight.gameserver.model.templates.spawn.SpawnGroup;
import ru.aionknight.gameserver.model.templates.spawn.SpawnTemplate;
import ru.aionknight.gameserver.services.ItemService;
import ru.aionknight.gameserver.utils.idfactory.IDFactory;
import ru.aionknight.gameserver.world.KnownList;
import ru.aionknight.gameserver.world.World;

/**
 * @author ATracer
 *
 */
public class StaticObjectSpawnManager
{

	/**
	 * 
	 * @param spawnGroup
	 * @param instanceIndex
	 */
	public static void spawnGroup(SpawnGroup spawnGroup, int instanceIndex)
	{
		VisibleObjectTemplate objectTemplate = ItemService.getItemTemplate(spawnGroup.getNpcid());
		if(objectTemplate == null)
			return;
		
		int pool = spawnGroup.getPool();
		for(int i = 0; i < pool; i++)
		{
			SpawnTemplate spawn = spawnGroup.getNextAvailableTemplate(instanceIndex);
			int objectId = IDFactory.getInstance().nextId();
			StaticObject staticObject = new StaticObject(objectId, new StaticObjectController(), spawn, objectTemplate);
			staticObject.setKnownlist(new KnownList(staticObject));
			bringIntoWorld(staticObject, spawn, instanceIndex);
		}
		spawnGroup.clearLastSpawnedTemplate();
	}
	
	/**
	 * 
	 * @param visibleObject
	 * @param spawn
	 * @param instanceIndex
	 */
	private static void bringIntoWorld(VisibleObject visibleObject, SpawnTemplate spawn, int instanceIndex)
	{
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.setPosition(visibleObject, spawn.getWorldId(), instanceIndex, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getHeading());
		world.spawn(visibleObject);
	}
}
