/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.beshmundir_temple;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;


public class _30225Lupukin_Request extends QuestHandler
{
    private final static int questId = 30225;
	
    public _30225Lupukin_Request()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		
        if(qs == null || qs.getStatus() == QuestStatus.NONE)
        {
	        if(targetId == 216527) //Lupukin
	        {
		       if(env.getDialogId() == 26)
		       {
			      return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs == null)
            return false;
			
        if(qs == null || qs.getStatus() == QuestStatus.COMPLETE) 
        {
	        if(targetId == 216527) //Lupukin
	        {
		       if(env.getDialogId() == 26)
		       {
			     return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                case 216527: //Lupukin
                    switch (env.getDialogId())
					{
                        case 26:
                            return sendQuestDialog(env, 2375);
                         case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        //Collect Balaur Relics (6)
                            if (QuestService.collectItemCheck(env, true))
			  {                 
			                    player.getInventory().removeFromBagByItemId(182209621, 6); //Balaur Relics
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else
							{
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 216527) //Lupukin
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(216527).addOnQuestStart(questId); //Lupukin
        qe.setNpcQuestData(216527).addOnTalkEvent(questId); //Lupukin
    }
}