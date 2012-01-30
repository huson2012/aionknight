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

package gameserver.controllers;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.dataholders.DataManager;
import gameserver.model.ChatType;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.chest.ChestTemplate;
import gameserver.model.templates.chest.KeyItem;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import java.util.List;
import java.util.concurrent.Future;

public class ChestController extends NpcController
{
	ChestTemplate	chestTemplate;
	private Player lastActor = null;
	private ChestState state = ChestState.IDLE;
	private Future<?> openingTask;
	
	private final int defaultUseTime = 3000;
	
	private enum ChestState
	{
		IDLE,
		OPENING
	}
	
	

	@Override
	public void setOwner(Creature owner)
	{
		super.setOwner(owner);
		chestTemplate = DataManager.CHEST_DATA.getChestTemplate(owner.getObjectTemplate().getTemplateId());
	}

	@Override
	public void onDialogRequest(final Player player)
	{
		if(chestTemplate == null || state == ChestState.OPENING)
			return;

		final List<KeyItem> keyItems = chestTemplate.getKeyItem();
		for(KeyItem keyItem : keyItems)
		{
			if(keyItem.getItemId() != 0 && player.getInventory().getItemCountByItemId(keyItem.getItemId()) < 1)
			{
				PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
					"I need a key to open this chest! ", ChatType.NORMAL), true);
				return;
			}
		}
		
		state = ChestState.OPENING;
		
		player.getObserveController().attach(new StartMovingListener(){
			@Override
			public void moved()
			{
				cancelOpening(player);
			}
		});

		player.getObserveController().attach(new ActionObserver(ObserverType.ATTACKED){
			@Override
			public void attacked(Creature creature)
			{		
				cancelOpening(player);
			}
		});
		
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner()
			.getObjectId()), true);
		
		openingTask = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
					defaultUseTime, 0));
				getOwner().setTarget(player);
				lastActor = player;
				onDie(player);
				
				// Remove keys
				for(KeyItem keyItem : keyItems)
					if(keyItem.getItemId() != 0)
						player.getInventory().removeFromBagByItemId(keyItem.getItemId(), keyItem.getQuantity());				
			}
		}, defaultUseTime);
		
	}
	
	@Override
	public void doReward()
	{
		if (lastActor == null || getOwner() == null)
			return;
		
		DropService.getInstance().registerDrop(getOwner() , lastActor, lastActor.getLevel());
		DropService.getInstance().requestDropList(lastActor, getOwner().getObjectId());
		lastActor = null;
	}
	
	private void cancelOpening(Player player)
	{
		state = ChestState.IDLE;
		openingTask.cancel(false);
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 0));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0,
			getOwner().getObjectId()), true);
	}
}