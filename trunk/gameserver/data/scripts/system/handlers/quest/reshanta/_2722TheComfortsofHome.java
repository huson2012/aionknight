/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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
package quest.reshanta;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;


/**
 * @author Hilgert
 * 
 */
 
public class _2722TheComfortsofHome extends QuestHandler
{
	private final static int	questId	= 2722;

	public _2722TheComfortsofHome()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(278047).addOnQuestStart(questId);
		qe.setNpcQuestData(278056).addOnTalkEvent(questId);
		qe.setNpcQuestData(278126).addOnTalkEvent(questId);
		qe.setNpcQuestData(278043).addOnTalkEvent(questId);
		qe.setNpcQuestData(278032).addOnTalkEvent(questId);
		qe.setNpcQuestData(278037).addOnTalkEvent(questId);
		qe.setNpcQuestData(278040).addOnTalkEvent(questId);
		qe.setNpcQuestData(278068).addOnTalkEvent(questId);
		qe.setNpcQuestData(278066).addOnTalkEvent(questId);
		qe.setNpcQuestData(278047).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 278047)
			{
				if(env.getDialogId() == -1)
					 return sendQuestDialog(env, 4762);
				else if(env.getDialogId() == 1007)
					return sendQuestDialog(env, 4);
				else 
					 return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			int var = qs.getQuestVarById(0);
			if(targetId == 278056)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
			else if(targetId == 278126)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278043)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1693);					 
				else if(env.getDialogId() == 10002)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
			else if(targetId == 278032)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2034);					 
				else if(env.getDialogId() == 10003)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278037)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2375);					 
				else if(env.getDialogId() == 10004)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278040)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2716);					 
				else if(env.getDialogId() == 10005)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278068)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 3057);					 
				else if(env.getDialogId() == 10006)
				{
					 qs.setQuestVarById(0, var + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
			else if(targetId == 278066)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 3398);
					 
				else if(env.getDialogId() == 10255)
				{
					 qs.setStatus(QuestStatus.REWARD);
					 ItemService.addItems(player, Collections.singletonList(new QuestItems(182205654, 1)));
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 278047)
		{
			return defaultQuestEndDialog(env);
		}
		return false;
    }		
}
