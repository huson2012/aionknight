/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser Public License for more details.
 * You should have received a copy of the GNU Lesser Public License
 * along with Aion-Knight.If not, see <http://www.gnu.org/licenses/>.
 */
package quest.lower_udas_temple;


import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;


public class _30338GroupImprovedStaff extends QuestHandler
{
	private final static int	questId	= 30338;

	public _30338GroupImprovedStaff()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 799336)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == -1)
				{
					if(QuestService.collectItemCheck(env, false))
						return sendQuestDialog(env, 2716);	
				}
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
			{
				if(env.getDialogId() == -1)
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
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if(id == 186000107)
		{
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.DEBILKARIM_FORGE_300160000))
				return HandlerResult.FAILED;
			if(qs == null)
				return HandlerResult.UNKNOWN;
			if(qs.getQuestVarById(0) != 0)
				return HandlerResult.FAILED;
			if(player.getInventory().getItemCountByItemId(101500736) == 0 || player.getInventory().getItemCountByItemId(186000099) == 0
				|| player.getInventory().getItemCountByItemId(186000106) < 20 || player.getInventory().getItemCountByItemId(186000107) == 0)
				return HandlerResult.FAILED;
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					ItemService.addItems(player, Collections.singletonList(new QuestItems(182209736, 1)));
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					player.getInventory().removeFromBagByItemId(186000099, 1);
					player.getInventory().removeFromBagByItemId(186000106, 20);
					player.getInventory().removeFromBagByItemId(186000107, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.UNKNOWN;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799336).addOnQuestStart(questId);
		qe.setNpcQuestData(799336).addOnTalkEvent(questId);
		qe.setQuestItemIds(186000107).add(questId);
	}
}
