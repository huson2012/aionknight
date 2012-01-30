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

package gameserver.itemengine.actions;

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ToyPetSpawnAction")
public class ToyPetSpawnAction extends AbstractItemAction
{
	
	@XmlAttribute
	protected int npcid;
	
	@XmlAttribute
	protected int time;	
	
	/**
	 * 
	 * @return the Npc Id
	 */
	public int getNpcId()
	{
		return npcid;
	}
	
	public int getTime()
	{
		return time;
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (player.getFlyState() != 0)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_BINDSTONE_ITEM_WHILE_FLYING);
			return false;
		}
		if(player.isInInstance())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FAR_FROM_NPC);
			return false;
		}
		if(player.getWorldId() == 110010000 
			|| player.getWorldId() == 120010000 
			|| player.getWorldId() == 110020000
			|| player.getWorldId() == 120020000
			|| player.getWorldId() == 210010000
			|| player.getWorldId() == 220010000)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FAR_FROM_NPC);
			return false;
		}

		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		SpawnEngine spawnEngine = SpawnEngine.getInstance();
		float x = player.getX();
		float y = player.getY();
		float z = player.getZ();
		byte heading = (byte) ((player.getHeading() + 60)%120);
		int worldId = player.getWorldId();
		int instanceId = player.getInstanceId();

		SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, 
			instanceId, npcid, x, y, z, heading, 0, 0, true, true);
		
		final Kisk kisk = spawnEngine.spawnKisk(spawn, instanceId, player);

		// Schedule Despawn Action
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				kisk.getController().onDespawn(true);
			}
		}, 7200000);
		// Fixed 2 hours 2 * 60 * 60 * 1000
		
		kisk.getController().addTask(TaskId.DESPAWN, task);
		
		//ShowAction
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
			parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId()), true);
			
		//RemoveKisk
		player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);
	}
}