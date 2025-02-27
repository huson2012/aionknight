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
package quest.eltnen;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;


/**
 * @author Balthazar
 */

public class _1467TheFourLeaders extends QuestHandler
{
	private final static int	questId	= 1467;

	public _1467TheFourLeaders()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204045).addOnQuestStart(questId);
		qe.setNpcQuestData(204045).addOnTalkEvent(questId);
		qe.setNpcQuestData(211696).addOnKillEvent(questId);
		qe.setNpcQuestData(211697).addOnKillEvent(questId);
		qe.setNpcQuestData(211698).addOnKillEvent(questId);
		qe.setNpcQuestData(211699).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 204045)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 4762);
					}
					case 1002:
					{
						return sendQuestDialog(env, 1011);
					}
					case 10000:
					{
						QuestService.startQuest(env, QuestStatus.START);
						qs = player.getQuestStateList().getQuestState(questId);
						qs.setQuestVarById(0, 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),
							10));
						return true;
					}
					case 10001:
					{
						QuestService.startQuest(env, QuestStatus.START);
						qs = player.getQuestStateList().getQuestState(questId);
						qs.setQuestVarById(0, 2);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),
							10));
						return true;
					}
					case 10002:
					{
						QuestService.startQuest(env, QuestStatus.START);
						qs = player.getQuestStateList().getQuestState(questId);
						qs.setQuestVarById(0, 3);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),
							10));
						return true;
					}
					case 10003:
					{
						QuestService.startQuest(env, QuestStatus.START);
						qs = player.getQuestStateList().getQuestState(questId);
						qs.setQuestVarById(0, 4);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),
							10));
						return true;
					}
					default:
						return defaultQuestStartDialog(env);
				}
			}
		}

		if(qs == null)
			return false;

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204045)
			{
				switch(env.getDialogId())
				{
					case -1:
					{
						switch(qs.getQuestVarById(0))
						{
							case 1:
							{
								return sendQuestDialog(env, 5);
							}
							case 2:
							{
								return sendQuestDialog(env, 6);
							}
							case 3:
							{
								return sendQuestDialog(env, 7);
							}
							case 4:
							{
								return sendQuestDialog(env, 8);
							}
						}
					}
					case 17:
					{
						QuestService.questFinish(env, qs.getQuestVarById(0) - 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),
							10));
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 211696, 1, true) || defaultQuestOnKillEvent(env, 211697, 2, true) || defaultQuestOnKillEvent(env, 211698, 3, true) || defaultQuestOnKillEvent(env, 211699, 4, true))
			return true;
		else
			return false;
	}
}