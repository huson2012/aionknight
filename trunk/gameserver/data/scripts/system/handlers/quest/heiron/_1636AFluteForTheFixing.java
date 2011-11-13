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
package quest.heiron;

import java.util.Collections;


import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**
 * @author Balthazar
 */

public class _1636AFluteForTheFixing extends QuestHandler
{
	private final static int	questId	= 1636;

	public _1636AFluteForTheFixing()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204535).addOnQuestStart(questId);
		qe.setNpcQuestData(204535).addOnTalkEvent(questId);
		qe.setNpcQuestData(203792).addOnTalkEvent(questId);
		qe.setQuestItemIds(182201785).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 204535)
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
				case 203792:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201786);
							long itemCount2 = player.getInventory().getItemCountByItemId(152020034);
							long itemCount3 = player.getInventory().getItemCountByItemId(152020091);
							long itemCount4 = player.getInventory().getItemCountByItemId(169400060);

							if(qs.getQuestVarById(0) == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if(qs.getQuestVarById(0) == 1 && itemCount1 >= 1 && itemCount2 >= 1 && itemCount3 >= 1
								&& itemCount4 >= 1)
							{
								return sendQuestDialog(env, 1352);
							}
							else
								return sendQuestDialog(env, 10001);
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							return true;
						}
						case 34:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 2);
							updateQuestStatus(env);

							if(player.getInventory().getItemCountByItemId(182201785) == 0)
							{
								if(!ItemService.addItems(player, Collections
									.singletonList(new QuestItems(182201785, 1))))
								{
									return true;
								}
							}
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject()
								.getObjectId(), 0));
							return true;
						}
					}
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204535)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if(id != 182201785 || qs == null)
			return HandlerResult.UNKNOWN;

		if (qs.getStatus() != QuestStatus.START)
			return HandlerResult.FAILED;

		int var = qs.getQuestVars().getQuestVars();
		if(var != 3)
		{
			return HandlerResult.FAILED;
		}

		if(MathUtil.getDistance(182, 2703, 143, player.getPosition().getX(), player.getPosition().getY(), player
			.getPosition().getZ()) > 10)
		{
			return HandlerResult.FAILED;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id,
			3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId,
					id, 0, 1, 0), true);
				player.getInventory().removeFromBagByObjectId(itemObjId, 1);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}