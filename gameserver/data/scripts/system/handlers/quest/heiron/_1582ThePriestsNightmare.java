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
package quest.heiron;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

/**
 * @author Orpheo 
 */
 
public class _1582ThePriestsNightmare extends QuestHandler
{
	private final static int	questId	= 1582;
	
	public _1582ThePriestsNightmare()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204560).addOnQuestStart(questId);
		qe.setNpcQuestData(204560).addOnTalkEvent(questId);
		qe.setNpcQuestData(700196).addOnTalkEvent(questId);
		qe.setNpcQuestData(204573).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204560)
			{			
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700196:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
								updateQuestStatus(env);
								return true;
							}
						}
					}
				}
				case 204560:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case 10001:
						{
							if(qs.getQuestVarById(0) == 1)
							{
								qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
								updateQuestStatus(env);
								return sendQuestDialog(env, 10);
							}
						}
					}
				}
				case 204573:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 2)
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case 10002:
						{
							if(qs.getQuestVarById(0) == 2)
							{
								qs.setQuestVar(2);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);				
								return sendQuestDialog(env, 10);
							}
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 700196)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
		}		
		return false;
	}
}