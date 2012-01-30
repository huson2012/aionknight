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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class ActionitemController extends NpcController
{
	private Player lastActor = null;
	
	/**
	 * 0 - clear object
	 * 1 - use object
	 * 3 - convert object
	 */
	@Override
	public void onDialogRequest(final Player player)
	{
		if (!QuestEngine.getInstance().onDialog(new QuestCookie(getOwner(), player, 0 , -1)))
			return;
		if (QuestEngine.getInstance().onActionItem(new QuestCookie(getOwner(), player, 0 , -1)))
			return;
		final int defaultUseTime = 3000;
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), 
			getOwner().getObjectId(), defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), 
					getOwner().getObjectId(), defaultUseTime, 0));
				getOwner().setTarget(player);
				lastActor = player;
				onDie(player);
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

	@Override
	public void onRespawn()
	{
		super.onRespawn();
		DropService.getInstance().unregisterDrop(getOwner());
	}
}