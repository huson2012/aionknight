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
package ru.aionknight.gameserver.controllers;

import java.util.List;


import ru.aionknight.gameserver.controllers.attack.AttackStatus;
import ru.aionknight.gameserver.model.EmotionType;
import ru.aionknight.gameserver.model.TaskId;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.Kisk;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.gameobjects.player.RequestResponseHandler;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_EMOTION;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import ru.aionknight.gameserver.services.KiskService;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.ThreadPoolManager;
import ru.aionknight.gameserver.world.World;


/**
 * @author Sarynth
 *
 */
public class KiskController extends NpcController
{
	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
	{
		Kisk kisk = (Kisk)this.getOwner();

		if (kisk.getLifeStats().isFullyRestoredHp())
		{
			List<Player> members = kisk.getCurrentMemberList();
			for(Player member : members)
			{
				PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_ATTACKED);
			}
		}
		
		super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
		
	}
	
	@Override
	public void onDespawn(boolean forced)
	{
		final Kisk kisk = (Kisk)this.getOwner();
		kisk.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_REMOVED);
		removeKisk(kisk);
	}
	
	@Override
	public void onDie(Creature lastAttacker)
	{
		final Kisk kisk = (Kisk)this.getOwner();
		PacketSendUtility.broadcastPacket(kisk, new SM_EMOTION(kisk, EmotionType.DIE, 0, 0));
		kisk.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_DESTROYED);
		removeKisk(kisk);
	}
	
	private void removeKisk(final Kisk kisk)
	{
		KiskService.removeKisk(kisk);
		
		// Schedule World Removal
		addTask(TaskId.DECAY, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (kisk != null && kisk.isSpawned())
					World.getInstance().despawn(kisk);
			}
		}, 3 * 1000));
	}
	
	@Override
	public void onDialogRequest(Player player)
	{
		final Kisk kisk = (Kisk)this.getOwner();
		
		if (player.getKisk() == kisk)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_ALREADY_REGISTERED);
			return;
		}
		
		if (kisk.canBind(player))
		{
			RequestResponseHandler responseHandler = new RequestResponseHandler(kisk) {
				
				@Override
				public void acceptRequest(Creature requester, Player responder)
				{
					Kisk kisk = (Kisk)requester;
					
					// Check again if it's full (If they waited to press OK)
					if (!kisk.canBind(responder))
					{
						PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
						return;
					}
					KiskService.onBind(kisk, responder);
				}
	
				@Override
				public void denyRequest(Creature requester, Player responder)
				{
					// Nothing Happens
				}
			};
			
			boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, responseHandler);
			if (requested)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, player.getObjectId()));
			}
		}
		else if (kisk.getCurrentMemberCount() >= kisk.getMaxMembers())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FULL);
		}
		else
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
		}
	}
	
}
