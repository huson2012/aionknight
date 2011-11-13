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
 
package quest.theobomos;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
 
public class _3075ClearingTheBarrens extends QuestHandler
{
    private final static int    questId    = 3075;
	
    public _3075ClearingTheBarrens()
    {
        super(questId);
    }
    
    @Override
    public void register()
    {
        qe.setNpcQuestData(798222).addOnQuestStart(questId);
        qe.setNpcQuestData(798222).addOnTalkEvent(questId);
        qe.setNpcQuestData(798158).addOnTalkEvent(questId);
		qe.setNpcQuestData(214173).addOnKillEvent(questId);
		qe.setNpcQuestData(214174).addOnKillEvent(questId);
		qe.setNpcQuestData(214182).addOnKillEvent(questId);
		qe.setNpcQuestData(214183).addOnKillEvent(questId);
    }
	
	@Override
    public boolean onDialogEvent(QuestCookie env)
    {
        final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 798222)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 1011);
					}
					default: return defaultQuestStartDialog(env);
				}
			}
		}
		
		if(qs == null)
			return false;
		
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			switch(targetId)
			{
				case 798158:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							return sendQuestDialog(env, 1352);
						}
						case 1009:
						{
							return sendQuestDialog(env, 5);
						}
						default: return defaultQuestEndDialog(env);
					}
				}
			}
		}
		return false;
    }
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null || qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		
		int targetId = 0;
		
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(targetId == 214173 || targetId == 214174)
		{
			switch(qs.getQuestVarById(0))
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
				case 17:
				case 18:
				case 19:
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					
					if(qs.getQuestVarById(0) == 20 && qs.getQuestVarById(1) == 15)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
					return true;
				}
			}
		}
		else if(targetId == 214182 || targetId == 214183)
		{
			switch(qs.getQuestVarById(1))
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				{
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
					
					if(qs.getQuestVarById(0) == 20 && qs.getQuestVarById(1) == 15)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
					return true;
				}
			}
		}
		return false;
	}
}