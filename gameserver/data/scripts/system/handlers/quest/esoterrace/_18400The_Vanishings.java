/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.If not, see <http://www.gnu.org/licenses/>.
 */

package quest.esoterrace;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _18400The_Vanishings extends QuestHandler
{
    private final static int questId = 18400;
	
    public _18400The_Vanishings()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
			
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        if (targetId != 799584) //Lanuaga/Koray.
            return false;
        if (qs.getStatus() == QuestStatus.START)
		{
            if (env.getDialogId() == 26)
                return sendQuestDialog(env, 1011);
            else if (env.getDialogId() == 1007)
			{
                qs.setStatus(QuestStatus.REWARD);
                qs.setQuestVarById(0, 1);
                updateQuestStatus(env);
                return sendQuestDialog(env, 5);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public boolean onLvlUpEvent(QuestCookie env)
	{
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
        if (qs != null || !lvlCheck)
            return false;
        QuestService.startQuest(env, QuestStatus.START);
        return true;
    }
	
    @Override
    public void register()
	{
        qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799584).addOnTalkEvent(questId); //Lanuaga.
		qe.setNpcQuestData(799838).addOnTalkEvent(questId); //Koray.
    }
}