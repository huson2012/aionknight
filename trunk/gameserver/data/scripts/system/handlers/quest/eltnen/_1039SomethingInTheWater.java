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
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;


/**
 * @author Sylar
 * 
 */
public class _1039SomethingInTheWater extends QuestHandler
{	

	private final static int	questId	= 1039;
	private final static int[]	mob_ids	= { 210946, 210947 };
	
	public _1039SomethingInTheWater()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setQuestItemIds(182201009).add(questId); //Empty Water Bottle
		qe.setNpcQuestData(203946).addOnTalkEvent(questId); //Asclepius
		qe.setNpcQuestData(203705).addOnTalkEvent(questId); //Jumentis
		qe.addQuestLvlUp(questId);
		for(int mob_id : mob_ids)
		qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
		
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}	
	
	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182201009 || qs == null) //Empty Water Bottle
			return HandlerResult.UNKNOWN;
		
		if (!ZoneService.getInstance().isInsideZone(player, ZoneName.MYSTIC_SPRING_OF_AGAIRON))
			return HandlerResult.FAILED;
		
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				player.getInventory().removeFromBagByObjectId(itemObjId, 1);
				ItemService.addItems(player, Collections.singletonList(new QuestItems(182201010, 1)));
				qs.setQuestVar(2);
				updateQuestStatus(env);
			}
		}, 3000);
		
		return HandlerResult.SUCCESS;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env) //Need to recheck because count bug
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		if(qs.getQuestVarById(0) == 4)
		{
			if(defaultQuestOnKillEvent(env, 210946, 0, 3, 1) || defaultQuestOnKillEvent(env, 210968, 0, 3, 1) ||
			   defaultQuestOnKillEvent(env, 210947, 0, 3, 2) || defaultQuestOnKillEvent(env, 210969, 0, 3, 2))
			{
				if(qs.getQuestVarById(1) == 3 && qs.getQuestVarById(2) == 3)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
				return true;
			}
		}
		return false;		
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
		return false;
		
		if(targetId == 203946) //Asclepius
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					qs.setQuestVar(1);
					ItemService.addItems(player, Collections.singletonList(new QuestItems(182201009, 1)));
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 10002)
				{
					qs.setQuestVar(4);
					updateQuestStatus(env);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}	
			
			else if(qs.getStatus() == QuestStatus.REWARD)
				{
					return defaultQuestEndDialog(env);
				}
		}
		else if(targetId == 203705) //Jumentis
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10001)
				{
					qs.setQuestVar(3); 
					updateQuestStatus(env);
					player.getInventory().removeFromBagByItemId(182201010, 1);
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
					
			}
		}	
		return false;
		}

}
