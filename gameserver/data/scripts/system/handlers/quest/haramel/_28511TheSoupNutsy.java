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
package quest.haramel;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;


/**
 * @author Nephis
 * 
 */
public class _28511TheSoupNutsy extends QuestHandler
{
	private final static int	questId	= 28511;

	public _28511TheSoupNutsy()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799523).addOnQuestStart(questId);
		qe.setNpcQuestData(799523).addOnTalkEvent(questId);
		qe.setNpcQuestData(798031).addOnTalkEvent(questId);
		qe.setNpcQuestData(700954).addOnTalkEvent(questId);
		qe.setNpcQuestData(730359).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(targetId == 799523)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
			
		else if(targetId == 730359)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 34)
				{
					if(QuestService.collectItemCheck(env, true))
					{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
							return sendQuestDialog(env, 1352);
					}
					else
						return sendQuestDialog(env, 10001);	
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if(targetId == 700954)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
				return true;
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798031)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}
}
