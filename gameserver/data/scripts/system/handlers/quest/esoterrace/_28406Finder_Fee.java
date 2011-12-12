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

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _28406Finder_Fee extends QuestHandler
{
    private final static int questId = 28406;
	
    public _28406Finder_Fee()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        Player player = env.getPlayer();
		
        if (env.getTargetId() == 0)
            return defaultQuestStartItem(env);
			
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;
			
        int var = qs.getQuestVarById(0);
		
        if (qs.getStatus() == QuestStatus.START)
		{
            if (env.getTargetId() == 799557)
			{
                switch (env.getDialogId())
				{
                    case 26:
                        if (var == 0)
                        return sendQuestDialog(env, 2375);
                    case 1009:
                        defaultQuestRemoveItem(env, 182215015, 1);
                        return defaultCloseDialog(env, 0, 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799557, 2375);
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(799557).addOnTalkEvent(questId);
    }
}