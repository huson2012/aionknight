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
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.GroupGate;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.TeleportService;
import gameserver.skill.model.Skill;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;

public class GroupGateController extends NpcWithCreatorController
{
	
	@Override
	public void onDialogRequest(final Player player)
	{
		final GroupGate groupgate = (GroupGate)this.getOwner();
		
		if (MathUtil.getDistance(player, groupgate) > 10)
			return;
		
		// Check race
		if (((Player)groupgate.getCreator()).getCommonData().getRace() != player.getCommonData().getRace())
			return;
		
		// Check level
		if (player.getLevel() < 9)
			return;
		
		boolean isMember = false;
		
		if(player.getObjectId() == ((Player)groupgate.getCreator()).getObjectId()) isMember = true;
		
		if (player.isInGroup())
		{
			for(Player member : player.getPlayerGroup().getMembers())
			{
				if (member.getObjectId() == ((Player)groupgate.getCreator()).getObjectId()) {
					isMember = true;
					break;
				}
			}
		}
		
		if (isMember)
		{
			final RequestResponseHandler responseHandler = new RequestResponseHandler(groupgate) {
				
				@Override
				public void acceptRequest(Creature requester, Player responder)
				{
					switch(groupgate.getNpcId())
					{
						case 749017:
							TeleportService.teleportTo(responder, 110010000, 1, 1444.9f, 1577.2f, 572.9f, 0);
							break;
						case 749083:
							TeleportService.teleportTo(responder, 120010000, 1, 1657.5f, 1398.7f, 194.7f, 0);
						break;
					}
				}
	
				@Override
				public void denyRequest(Creature requester, Player responder)
				{
					// Nothing Happens
				}
			};
			
			boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, responseHandler);
			
			// If players moves or uses skill, request is denied
			final ActionObserver obSkill = new ActionObserver(ObserverType.SKILLUSE)
				{
					@Override
					public void skilluse(Skill skill)
					{		
						player.getResponseRequester().respond(SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, 0);
					}
				};
			
			player.getObserveController().attach(obSkill);	
			player.getObserveController().attach(new ActionObserver(ObserverType.MOVE)
			{
				@Override
				public void moved()
				{		
					if (obSkill != null)
						player.getObserveController().removeObserver(obSkill);
					
					player.getResponseRequester().respond(SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, 0);
				}
			}
		);
			if (requested)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, player.getObjectId()));
			}
			
		}
		else 
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_MAGIC_PASSAGE);
		}
	}
}