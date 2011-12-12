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

public class _18409Tiamat_Power_Unleashed extends QuestHandler
{
	private final static int questId = 18409;
	
	public _18409Tiamat_Power_Unleashed()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(env.getTargetId() == 0)
			return defaultQuestStartItem(env);
		
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 799552:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							return sendQuestDialog(env, 4762);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 205232:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
							return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2, true, false);
					}
					break;
			 }
		}
		return defaultQuestRewardDialog(env, 799552, 2375);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 215795, 0, 1))
		   
			return true;
		else
			return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(799552).addOnQuestStart(questId);
		qe.setNpcQuestData(205232).addOnTalkEvent(questId);
		qe.setNpcQuestData(215795).addOnKillEvent(questId);
	}
}