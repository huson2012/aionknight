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
package quest.morheim;


import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy
 * 
 */
public class _2477ADishForDukar extends QuestHandler
{
	private final static int	questId	= 2477;
	private final static int[]	npcs = {204355, 204100};

	public _2477ADishForDukar()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204355).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 204355))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId()) 
			{
				case 204355:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 2);
						case 34:
							return defaultQuestItemCheck(env, 0, 2, false, 10000, 10001, 182204234, 1);
					}
					break;
				case 204100:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10255:
							return defaultCloseDialog(env, 2, 3, true, false, 182204197, 1, 182204234, 1);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 204355, 10002);
	}
}