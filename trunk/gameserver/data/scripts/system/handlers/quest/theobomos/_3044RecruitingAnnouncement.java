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

public class _3044RecruitingAnnouncement extends QuestHandler
{
	private final static int	questId	= 3044;

	public _3044RecruitingAnnouncement()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(730145).addOnQuestStart(questId);
		qe.setNpcQuestData(730145).addOnTalkEvent(questId);
		qe.setNpcQuestData(798206).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 730145)
		{
			if(env.getDialogId() == -1
				&& (qs == null || qs.getStatus() == QuestStatus.LOCKED || qs.getStatus() == QuestStatus.NONE))
				return sendQuestDialog(env, 4762);
			else if(env.getDialogId() == 10000)
			{
				QuestService.startQuest(env, QuestStatus.START);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
				return true;
			}
			else
				return true;
		}
		else if(targetId == 798206)
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1009)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
			{
				return defaultQuestEndDialog(env);
			}
		}

		return false;
	}
}