/**
 * This file is part of Aion-Knight
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
package quest.radiant_ops;


import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;


public class _35503EverythingisRadiant extends QuestHandler
{
	private final static int	questId	= 35503;

	public _35503EverythingisRadiant()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestStartDaily(env))
			return true;
		
		if(qs == null)
			return false;
		if(defaultQuestRewardDialog(env, 799825, 10002) || defaultQuestRewardDialog(env, 799826, 10002))
			return true;
		else
			return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(player.getWorldId() == 600010000)
		{
			if(defaultQuestOnKillPlayerEvent(env, 1, 0, 4, false) || defaultQuestOnKillPlayerEvent(env, 1, 4, 5, true))
				return true;
		}
		return false;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799825).addOnTalkEvent(questId);
		qe.setNpcQuestData(799826).addOnTalkEvent(questId);
		qe.addOnKillPlayer(questId);
	}
}
