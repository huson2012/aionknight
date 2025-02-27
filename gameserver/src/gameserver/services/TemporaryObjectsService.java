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

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_DELETE_WAREHOUSE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Future;

public class TemporaryObjectsService
{
	private FastMap<Integer, AionObject> temporaryObjects = new FastMap<Integer, AionObject>().shared();
	private SortedMap<Long, Integer> objectsTimeCollection = new TreeMap<Long, Integer>();
	private int CHECK_TIME_PERIOD = 1000;
	private static final Logger	log = Logger.getLogger(TemporaryObjectsService.class);
	private boolean isStarted = false;
	private Future<?> checkTask = null;
	public static final TemporaryObjectsService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void start()
	{
		checkTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

			@Override
			public void run()
			{
				checkExpiredObjects();
			}
		}, CHECK_TIME_PERIOD, CHECK_TIME_PERIOD);
		
		isStarted = true;
	}
	
	public void addObject(AionObject object)
	{		
		if(!temporaryObjects.containsKey(object.getObjectId()))
		{		
			if(object instanceof Item)
			{	
				temporaryObjects.put(object.getObjectId(), object);
				objectsTimeCollection.put(((Item)object).getTempItemExpireTime(), object.getObjectId());
			}
			else
				log.warn("Temporary unknown object ADD operation. ObjectId: " + String.valueOf(object.getObjectId()));
		}
		
		if(!isStarted)
			start();
	}
	
	public void removeObject(AionObject object)
	{
		if(temporaryObjects.containsKey(object.getObjectId()))	
			temporaryObjects.remove(object.getObjectId());
	}
	
	private void checkExpiredObjects()
	{
		if(objectsTimeCollection.isEmpty() && temporaryObjects.isEmpty())
		{
			if(isStarted)
			{
				checkTask.cancel(false);
				checkTask = null;
				isStarted = false;
			}
			return;
		}
		
		else if(objectsTimeCollection.isEmpty() && !temporaryObjects.isEmpty())
		{
			List<AionObject>objectsToRemove = new ArrayList<AionObject>();
			
			for(AionObject obj : temporaryObjects.values())
			{
				objectsToRemove.add(obj);
			}
			
			for(AionObject obj : objectsToRemove)
			{
				if(obj instanceof Item)
				{
					Item tempItem = (Item)obj;
					
					int ownerId = tempItem.getOwnerId();
					int storageType = tempItem.getItemLocation();
					
					Player player = World.getInstance().findPlayer(ownerId);
					
					if(player != null)
					{
						if(player.getStorage(storageType).removeFromBag(tempItem, true))						
							if(storageType == StorageType.CUBE.getId())
								PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(tempItem.getObjectId()));
							else
								PacketSendUtility.sendPacket(player, new SM_DELETE_WAREHOUSE_ITEM(storageType, tempItem.getObjectId()));
						return;
					}
				}
				
				temporaryObjects.remove(obj.getObjectId());
			}
			
			if(isStarted)
			{
				checkTask.cancel(false);
				checkTask = null;
				isStarted = false;
			}
			return;
		}
		else if(!objectsTimeCollection.isEmpty() && temporaryObjects.isEmpty())
		{
			objectsTimeCollection.clear();
			
			if(isStarted)
			{
				checkTask.cancel(false);
				checkTask = null;
				isStarted = false;
			}			
			return;
		}
		
		SortedMap<Long, Integer> head = objectsTimeCollection.headMap(System.currentTimeMillis() + 1000);
		
		if(head.size() <= 0)
			return;
		
		while(!head.isEmpty())
		{			
			long headTime = head.firstKey();
			int headObjId = head.get(headTime);

			if(!temporaryObjects.containsKey(headObjId))
			{
				objectsTimeCollection.remove(headTime);
				head.remove(headTime);
				continue;
			}
		
			AionObject obj = temporaryObjects.get(headObjId);
		
			if(obj == null)
			{
				temporaryObjects.remove(headObjId);
				objectsTimeCollection.remove(headTime);
				head.remove(headTime);
				continue;
			}
		
			if(obj instanceof Item)
			{
				Item tempItem = (Item)obj;
				if(tempItem.getTempItemTimeLeft() <= 0)
				{
					destroyItem(tempItem, headTime, headObjId);
					head.remove(headTime);
				}
			}
			else
			{
				log.warn("Unk object in temp service. ID: " + String.valueOf(obj.getObjectId()));
				temporaryObjects.remove(headObjId);
				objectsTimeCollection.remove(headTime);
				head.remove(headTime);
			}
		}
	}
	
	private void destroyItem(Item tempItem, long headTime, int headObjId)
	{		
		int ownerId = tempItem.getOwnerId();
		int storageType = tempItem.getItemLocation();
		
		Player player = World.getInstance().findPlayer(ownerId);
		
		if(player != null)
		{
			boolean canRemove = true;
			
			if(tempItem.isEquipped())
			{
				canRemove = player.getEquipment().removeTemporaryItem(tempItem.getObjectId(), tempItem.getEquipmentSlot());
				
				PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(),
					player.getEquipment().getEquippedItemsWithoutStigma()), true);	
			}
			else
				canRemove = player.getStorage(storageType).removeFromBag(tempItem, true);
			
			if(storageType == StorageType.CUBE.getId())
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(tempItem.getObjectId()));
			else
				PacketSendUtility.sendPacket(player, new SM_DELETE_WAREHOUSE_ITEM(storageType, tempItem.getObjectId()));
			
			if(canRemove)
			{
				objectsTimeCollection.remove(headTime);
				temporaryObjects.remove(headObjId);
			}
		}	
		else
		{
			objectsTimeCollection.remove(headTime);
			temporaryObjects.remove(headObjId);
		}
	}

	public void onPlayerLogout(Player player)
	{
		for(Item item : player.getEquipment().getEquippedItems())
		{
			if(item.getTempItemSettedTime() > 0)
				removeObject(item);
		}
		
		for(Item item : player.getInventory().getAllItems())
		{
			if(item.getTempItemSettedTime() > 0)
				removeObject(item);
		}
		
		for(Item item : player.getWarehouse().getAllItems())
		{
			if(item.getTempItemSettedTime() > 0)
				removeObject(item);
		}
	}
		
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final TemporaryObjectsService instance = new TemporaryObjectsService();
	}
}
