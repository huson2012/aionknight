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
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _3056InSearchOfZirius extends QuestHandler {
	private final static int questId = 3056;
	private final static int[] npc_ids = { 730147, 798213 };
	
	public _3056InSearchOfZirius() {
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(730147).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
		qe.setNpcQuestData(214578).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 730147)
		{
			if(env.getDialogId() == -1)
				return sendQuestDialog(env, 4762);
			else if(env.getDialogId() == 10000)
			{
	                		QuestService.startQuest(env, QuestStatus.START);
                			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                			return true;
			}
		}
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798213)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 10002);
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
		if(targetId == 798213)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 10001:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);								
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() != QuestStatus.START)
			return false;
		switch(targetId)
		{
			case 214578:
				if(var == 1)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
		}
		return false;
	}
}