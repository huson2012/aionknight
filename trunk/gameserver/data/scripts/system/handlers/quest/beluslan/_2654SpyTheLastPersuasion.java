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

package quest.beluslan;

import gameserver.model.gameobjects.Npc;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2654SpyTheLastPersuasion extends QuestHandler
{
   private final static int   questId = 2654;
   
   public _2654SpyTheLastPersuasion()
   {
      super(questId);
   }
   
   @Override
   public void register()
   {
      qe.setNpcQuestData(204775).addOnQuestStart(questId);
      qe.setNpcQuestData(204775).addOnTalkEvent(questId);
      qe.setNpcQuestData(204621).addOnTalkEvent(questId);
   }
   
   public boolean onLvlUpEvent(QuestCookie env)
   {
	   return defaultQuestOnLvlUpEvent(env, 2653);
   }
   
   @Override
   public boolean onDialogEvent(QuestCookie env)
   {
	   int targetId = 0;
	   if(env.getVisibleObject() instanceof Npc)
		   targetId = ((Npc) env.getVisibleObject()).getNpcId();
	   final QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
	   if(targetId == 204775)
	   {
		   if(qs == null || qs.getStatus() == QuestStatus.NONE)
		   {
			   if(env.getDialogId() == 26)
				   return sendQuestDialog(env, 1011);
			   else 
				   return defaultQuestStartDialog(env);
		   }
      }
      else if(targetId == 204655)
      {
          if(qs != null)
          {
             if(env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START)
                return sendQuestDialog(env, 2375);
             else if(env.getDialogId() == 1009)
             {
                qs.setQuestVar(0);
                qs.setStatus(QuestStatus.REWARD);
                updateQuestStatus(env);
                return defaultQuestEndDialog(env);
             }
             else
                return defaultQuestEndDialog(env);
          }
      }
	   return false;
   }   
}