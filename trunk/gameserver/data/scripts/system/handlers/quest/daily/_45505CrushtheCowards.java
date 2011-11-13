/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package quest.daily;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;

public class _45505CrushtheCowards extends QuestHandler
{
	private final static int	questId	= 45505;

	public _45505CrushtheCowards()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799865).addOnTalkEvent(questId);
		qe.setNpcQuestData(799866).addOnTalkEvent(questId);
		qe.addOnKillPlayer(questId);
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(player.getWorldId() == 210050000)
		{
			if(defaultQuestOnKillPlayerEvent(env, 1, 0, 4, false) || defaultQuestOnKillPlayerEvent(env, 1, 4, 5, true))
				return true;
		}
		return false;
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
		if(defaultQuestRewardDialog(env, 799865, 10002) || defaultQuestRewardDialog(env, 799866, 10002))
			return true;
		else
			return false;
	}
}