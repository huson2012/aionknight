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
 
package quest.verteron;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

public class _1146DelicateMandrake extends QuestHandler
{
	private final static int	questId	= 1146;

	public _1146DelicateMandrake()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203123).addOnQuestStart(questId);
		qe.setNpcQuestData(203123).addOnTalkEvent(questId);
		qe.setNpcQuestData(203139).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203123)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1007:
						return sendQuestDialog(env, 4);
					case 1002:
						if (!player.getQuestTimerOn() && !ItemService.addItems(player, Collections.singletonList(new QuestItems(182200519, 1))));
						QuestService.questTimerStart(env, 900);
						if (QuestService.startQuest(env, QuestStatus.START))
							return sendQuestDialog(env, 1003);
						else 
							return false;
					case 1003:
						return sendQuestDialog(env, 1004);
				}
			}
		}
		else if(targetId == 203139)
		{
			long itemCount = player.getInventory().getItemCountByItemId(182200519);
				if(itemCount >= 1)
			{
				if ((qs.getQuestVarById(0) == 0 || qs.getQuestVarById(0) == 0) && env.getDialogId() == -1)
				{
					qs.setQuestVar(2);
					player.getInventory().removeFromBagByItemId(182200011, itemCount);
					QuestService.questTimerEnd(env);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return defaultQuestEndDialog(env);
				}
				else
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onQuestTimerEndEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		//int var = qs.getQuestVars().getQuestVars();
		PacketSendUtility.sendMessage(player, "ToDo: Set what happens when timer ends..... And remove temp from QuestService");
		return true;
	}	
}