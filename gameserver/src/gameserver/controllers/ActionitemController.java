/**
 * This file is part of Aion-Knight [http://www.aion-knight.ru]
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


/**

 *
 */
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
