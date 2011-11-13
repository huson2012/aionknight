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

public class _3031WantedPirates extends QuestHandler {
	private final static int questId = 3031;
	private final static int[] npc_ids = { 730144, 798172 };
	private final static int[] mob_ids = { 214219, 214220, 214222, 214223 };
	
	public _3031WantedPirates() {
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(730144).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
		for(int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 730144)
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
		
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 798172)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return defaultQuestEndDialog(env);
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

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() != QuestStatus.START)
			return false;
		if(targetId == 214219 || targetId == 214220)
		{
			switch(qs.getQuestVarById(1))
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				{
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);					
					updateQuestStatus(env);

					if (qs.getQuestVarById(1) == 15 && qs.getQuestVarById(2) == 12)
					{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
					}
					return true;
				}
			}
		}
		else if(targetId == 214223 || targetId == 214222)
		{
			switch(qs.getQuestVarById(2))
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				{
					qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);					
					updateQuestStatus(env);

					if (qs.getQuestVarById(1) == 15 && qs.getQuestVarById(2) == 12)
					{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
					}
					return true;
				}
			}
		}
		return false;
	}
}