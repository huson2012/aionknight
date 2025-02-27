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
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;


/**
 * @author Balthazar
 */

public class _1482ATeleportationAdventure extends QuestHandler
{
	private final static int	questId	= 1482;

	public _1482ATeleportationAdventure()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203919).addOnQuestStart(questId);
		qe.setNpcQuestData(203919).addOnTalkEvent(questId);
		qe.setNpcQuestData(203337).addOnTalkEvent(questId);
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
			if(targetId == 203919)
			{
				if(env.getDialogId() == 26)
				{
					return sendQuestDialog(env, 4762);
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		if(qs == null)
			return false;

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 203337:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							switch(qs.getQuestVarById(0))
							{
								case 0:
								{
									return sendQuestDialog(env, 1011);
								}
								case 1:
								{
									long itemCount1 = player.getInventory().getItemCountByItemId(182201399);
									if(itemCount1 >= 3)
									{
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(env);
										return sendQuestDialog(env, 1352);
									}
									else
										return sendQuestDialog(env, 10001);
								}
								case 2:
								{
									return sendQuestDialog(env, 1693);
								}
								case 3:
								{
									return sendQuestDialog(env, 10002);
								}
							}
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 10));
							return true;
						}
						case 10002:
						{
							qs.setQuestVar(3);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							TeleportService.teleportTo(player, 220020000, 1, 638, 2337, 425, (byte) 20, 0);
							return true;
						}
						default:
							return defaultQuestStartDialog(env);
					}
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203337)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}