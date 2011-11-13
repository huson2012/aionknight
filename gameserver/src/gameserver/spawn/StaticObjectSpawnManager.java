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