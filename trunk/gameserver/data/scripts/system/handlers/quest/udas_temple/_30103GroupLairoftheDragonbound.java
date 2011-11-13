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

package quest.udas_temple;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

public class _30103GroupLairoftheDragonbound extends QuestHandler
{
	private final static int	questId	= 30103;

	public _30103GroupLairoftheDragonbound()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799333).addOnQuestStart(questId);
		qe.setNpcQuestData(799333).addOnTalkEvent(questId);
		qe.setQuestItemIds(182209189).add(questId);
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (id != 182209189 || qs == null)
			return HandlerResult.UNKNOWN;

		final int var = qs.getQuestVarById(0);
		
		if (var == 0)
		{
			if(!ZoneService.getInstance().isInsideZone(player, ZoneName.SUBTERRANEA_TEMPLE_ENTRANCE_220070000))
				return HandlerResult.FAILED;
			
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					player.getInventory().removeFromBagByItemId(182209189, 1);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		
		}
		return HandlerResult.FAILED;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		@SuppressWarnings("unused")
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 799333)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
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
				return defaultQuestEndDialog(env);
		}
		return false;
	}
}