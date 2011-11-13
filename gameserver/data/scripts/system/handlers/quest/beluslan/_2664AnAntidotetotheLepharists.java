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

package quest.beluslan;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2664AnAntidotetotheLepharists extends QuestHandler {
	private final static int questId = 2664;
	private final static int[] npc_ids = { 204777, 700324 };
	
	public _2664AnAntidotetotheLepharists() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.setNpcQuestData(204777).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
	if(targetId == 204777)
	{
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (env.getDialogId() == 26)
				return sendQuestDialog(env, 4762);
			else
				return defaultQuestStartDialog(env);
		}
	}
	if(qs == null)
	return false;

	int var = qs.getQuestVarById(0);
	if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 204777)
		{
			if (env.getDialogId() == -1)
				return sendQuestDialog(env, 10002);
			else if (env.getDialogId() == 1009)
				return sendQuestDialog(env, 5);
			else return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
	if(targetId == 700324)
	{
		switch(env.getDialogId())
		{
			case -1:
				if(var >= 0 && var < 4)
				{
					if(player.getInventory().getItemCountByItemId(182204489) >= 1 )
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					}
				}
				else if(var == 4)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			return false;
		}
	}
	return false;
     }
}