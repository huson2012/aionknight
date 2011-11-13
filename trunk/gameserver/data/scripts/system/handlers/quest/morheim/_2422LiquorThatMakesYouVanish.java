/**
 * This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 * Zetta-Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Zetta-Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Zetta-Core. If not, see <http://www.gnu.org/licenses/>.
 */

package quest.morheim;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.TeleportService;

/**
 * @author XRONOS remod By Xmen
 */

public class _2422LiquorThatMakesYouVanish extends QuestHandler
{
	private final static int questId	= 2422;
	private final static int[] npcs = {204326, 204327, 204375};
	
	public _2422LiquorThatMakesYouVanish()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204326).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 204326, 4762))
			return true;

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204327:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204375:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							
							TeleportService.teleportTo(player, 210020000, 1, 565, 2516, 329, (byte)90, 0);
								return defaultCloseDialog(env, 1, 2, true, false);					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 204326, 10002);
	}
}
