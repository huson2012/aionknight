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
package quest.pandaemonium;


import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

 
/**
 * @author Hellboy
 *
 */
public class _4941PandaemoniumHonors extends QuestHandler
{
	private final static int	questId	= 4941;

	public _4941PandaemoniumHonors()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204060).addOnQuestStart(questId);
		qe.setNpcQuestData(204060).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 204060, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 204060)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1011);
						else if(var == 1)
							return sendQuestDialog(env, 1352);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
					case 10001:
						return defaultCloseDialog(env, 1, 0);
					case 34:
						return defaultQuestItemCheck(env, 1, 0, true, 5, 10001);
				}
			}
		}
		return defaultQuestRewardDialog(env, 204060, 0);
	}
}
