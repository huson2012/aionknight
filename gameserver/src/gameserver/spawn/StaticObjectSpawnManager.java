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

package gameserver.spawn;

import gameserver.controllers.StaticObjectController;
import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.services.ItemService;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.KnownList;
import gameserver.world.World;

public class StaticObjectSpawnManager
{
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
	
	private static void bringIntoWorld(VisibleObject visibleObject, SpawnTemplate spawn, int instanceIndex)
	{
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.setPosition(visibleObject, spawn.getWorldId(), instanceIndex, spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getHeading());
		world.spawn(visibleObject);
	}
}