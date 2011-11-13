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
 * @author XRONOS
 * 
 */
public class _2498TheSoddenScroll extends QuestHandler
{

	private final static int	questId	= 2498;

	public _2498TheSoddenScroll()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798125).addOnTalkEvent(questId);
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
			else
				return false;
		
		if(qs == null)
			return false;
		
		return defaultQuestRewardDialog(env, 798125, 2375);
	}
}
