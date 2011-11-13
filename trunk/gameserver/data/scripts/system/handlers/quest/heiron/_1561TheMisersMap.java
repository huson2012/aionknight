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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _1561TheMisersMap extends QuestHandler
{

	private final static int	questId	= 1561;

	public _1561TheMisersMap()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(700188).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(env.getTargetId() == 0)
			if(defaultQuestStartItem(env))
			{
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		
		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 700188)
			{
				if(env.getDialogId() == -1)
					return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		sendQuestDialog(env, 2375);
	}
}