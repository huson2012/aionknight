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

package quest.theobomos;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _3085TheRiddlePoem extends QuestHandler {
	private final static int questId = 3085;

	public _3085TheRiddlePoem() {
		super(questId);
	}

	@Override
	public void register() {
		qe.setNpcQuestData(798144).addOnQuestStart(questId);
        	qe.setNpcQuestData(798144).addOnTalkEvent(questId);
        	qe.setNpcQuestData(203830).addOnTalkEvent(questId);
		qe.setNpcQuestData(798132).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	final QuestState qs = player.getQuestStateList().getQuestState(questId);
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	if(targetId == 798144)
	{
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getDialogId() == 26)
				return sendQuestDialog(env, 1011);
			else
				return defaultQuestStartDialog(env);
		}
	}
	if(qs == null)
	return false;

	int var = qs.getQuestVarById(0);
	if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 798132)
		{
			if(env.getDialogId() == -1)
				return sendQuestDialog(env, 2375);
			else if(env.getDialogId() == 1009)
				return sendQuestDialog(env, 5);
			else
				return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
	if(targetId == 203830)
	{
		switch(env.getDialogId())
		{
			case 26:
				if(var == 0)
					return sendQuestDialog(env, 1352);
			case 10000:
				if(var == 0)
				{
					qs.setQuestVarById(0, var + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			return false;
		}
	}
		return false;
	}
}