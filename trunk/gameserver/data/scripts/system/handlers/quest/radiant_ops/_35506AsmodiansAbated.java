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


public class _35506AsmodiansAbated extends QuestHandler
{
	private final static int	questId	= 35506;

	public _35506AsmodiansAbated()
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
		return defaultQuestRewardDialog(env, 799831, 10002);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(player.getWorldId() == 210020000 || player.getWorldId() == 210040000)
		{
			if(defaultQuestOnKillPlayerEvent(env, 1, 0, 1, false) || defaultQuestOnKillPlayerEvent(env, 1, 1, 2, true))
				return true;
		}
		return false;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799831).addOnTalkEvent(questId);
		qe.addOnKillPlayer(questId);
	}
}
