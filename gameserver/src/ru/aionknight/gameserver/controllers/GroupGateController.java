/*
 * This file is part of the requirements for the Illusion Gate Skill.
 */
package ru.aionknight.gameserver.controllers;



import ru.aionknight.gameserver.controllers.movement.ActionObserver;
import ru.aionknight.gameserver.controllers.movement.ActionObserver.ObserverType;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.GroupGate;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.gameobjects.player.RequestResponseHandler;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.services.TeleportService;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.utils.MathUtil;
import ru.aionknight.gameserver.utils.PacketSendUtility;

/**
 * @author LokiReborn
 */
public class GroupGateController extends NpcWithCreatorController
{
	
	@Override
	public void onDialogRequest(final Player player)
	{
		final GroupGate groupgate = (GroupGate)this.getOwner();
		
		if (MathUtil.getDistance(player, groupgate) > 10)
			return;
		
		//check race
		if (((Player)groupgate.getCreator()).getCommonData().getRace() != player.getCommonData().getRace())
			return;
		//check level
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
			
			//if players moves or uses skill, request is denied
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
