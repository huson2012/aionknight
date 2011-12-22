/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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