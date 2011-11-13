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

package quest.abyssal_splinter;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _30355TheProtectorsMadness extends QuestHandler
{

	private final static int	questId	= 30355;
	private final static int[]	npcs = {260265, 700856, 278001};
	
	public _30355TheProtectorsMadness()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(260265).addOnQuestStart(questId);
		qe.setNpcQuestData(216952).addOnKillEvent(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 260265, 4762))
			return true;

		if(qs == null)
			return false;
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 700856:
					return defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
			}
		}
		return defaultQuestRewardDialog(env, 278001, 10002);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 216952, 0, 1))
			return true;
		else
			return false;
	}
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		qs.setStatus(QuestStatus.REWARD);
		updateQuestStatus(env);
	}
}