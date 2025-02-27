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

import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.group.PlayerGroup;

public class KysisInstanceService
{
	protected VisibleObject					chests[];
	int mapId = 300120000;
	
	public void onGroupReward(Monster monster, PlayerGroup group)
	{
		if(monster.getObjectTemplate().getTemplateId() == 215179)
		{
			SpawnChest(group);
		}
	}
	
	public void SpawnChest(PlayerGroup group)
	{	
		chests = new VisibleObject[12];
		
		chests[0] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700542, (float) 576.8508, (float) 836.40424, (float) 200, (byte) 44, true);
		chests[1] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 477.4225, (float) 814.993, (float) 199.70894, (byte) 11, true);
		chests[2] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 470.9954, (float) 834.474, (float) 199.70894, (byte) 118, true);
		chests[3] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 470.5955, (float) 854.473, (float) 199.70894, (byte) 117, true);
		chests[4] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 476.9531, (float) 874.273, (float) 199.70882, (byte) 108, true);
		chests[5] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 489.9968, (float) 889.522, (float) 199.70882, (byte) 103, true);
		chests[6] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 507.7754, (float) 900.224, (float) 199.70882, (byte) 98, true);
		chests[7] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 528.1088, (float) 903.631, (float) 199.70882, (byte) 90, true);
		chests[8] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 547.4296, (float) 900.064, (float) 199.70882, (byte) 83, true);
		chests[9] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 565.3545, (float) 890.399, (float) 199.70882, (byte) 78, true);
		chests[10] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700541, (float) 578.5674, (float) 875.200, (float) 199.70882, (byte) 70, true);
		chests[11] = (Npc) InstanceService.addNewSpawn(mapId, group.getGroupLeader().getInstanceId(), 700560, (float) 586.5694, (float) 856.1183, (float) 199.70882, (byte) 64, true);
	}
	
	public static KysisInstanceService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final KysisInstanceService	instance	= new KysisInstanceService();
	}
}
