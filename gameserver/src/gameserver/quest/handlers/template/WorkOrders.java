/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.quest.handlers.template;

import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.bonus.BonusTemplate;
import gameserver.model.templates.quest.CollectItem;
import gameserver.model.templates.quest.CollectItems;
import gameserver.model.templates.quest.QuestItems;
import gameserver.model.templates.quest.QuestWorkItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.handlers.models.WorkOrdersData;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import java.util.List;

public class WorkOrders extends QuestHandler
{
	private final WorkOrdersData workOrdersData;
	/**
	 * @param questId
	 */
	public WorkOrders(WorkOrdersData workOrdersData)
	{
		super(workOrdersData.getId());
		this.workOrdersData = workOrdersData;
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(workOrdersData.getStartNpcId()).addOnQuestStart(workOrdersData.getId());
		qe.setNpcQuestData(workOrdersData.getStartNpcId()).addOnTalkEvent(workOrdersData.getId());
		qe.addOnQuestAbort(workOrdersData.getId());
		qe.addOnQuestFinish(workOrdersData.getId());
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(env.getTargetId() != workOrdersData.getStartNpcId())
			return false;
		QuestState qs = player.getQuestStateList().getQuestState(workOrdersData.getId());
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)
		{
			switch(env.getDialogId())
			{
				case 26:
					return sendQuestDialog(env, 4);
				case 1002:
					if(player.getInventory().isFull())
					{
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
					else if (QuestService.startQuest(env, QuestStatus.START))
					{
						if (ItemService.addItems(player, workOrdersData.getGiveComponent()))
						{
							player.getRecipeList().addRecipe(player, DataManager.RECIPE_DATA.getRecipeTemplateById(workOrdersData.getRecipeId()));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						}
						return true;
					}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START)
		{
			if (env.getDialogId() == 26)
				return sendQuestDialog(env, 5);
			else if (env.getDialogId() == 18)
			{
				int questId = env.getQuestId();
				QuestWorkItems qwi = DataManager.QUEST_DATA.getQuestById(questId).getQuestWorkItems();

				if(player.getInventory().isFull())
				{
					boolean failed = true;
					if(qwi != null)
					{
						long count = 0;
						for(QuestItems qi : qwi.getQuestWorkItem())
						{
							count = player.getInventory().getItemCountByItemId(qi.getItemId());
							if(qi.getCount() <= count)
							{
								failed = false; // we can remove all and free a slot
								break;
							}
						}
					}
					if(failed)
					{
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				}
				if (QuestService.collectItemCheck(env, false))
				{
					QuestTemplate	template = DataManager.QUEST_DATA.getQuestById(questId);
					CollectItems collectItems = template.getCollectItems();
					
					// remove crafted items only when the work order is removed from the dialog;
					// otherwise, leave them for bonus exchange
					int playerSkillPoints = player.getSkillList().getSkillLevel(template.getCombineSkill());
					int craftSkillPoints = template.getCombineSkillPoint();
					if(craftSkillPoints == 1)
						craftSkillPoints = 0;
					long removeCount = 0;

					for (CollectItem collectItem : collectItems.getCollectItem())
					{
						if(!CustomConfig.WORK_ORDER_BONUS || (craftSkillPoints <= playerSkillPoints / 10 * 10 - 4 * 10))
							removeCount = player.getInventory().getItemCountByItemId(collectItem.getItemId());
						else
							removeCount = collectItem.getCount();
						if(!player.getInventory().removeFromBagByItemId(collectItem.getItemId(), removeCount))
							return false;
					}
					
					//remove all worker list item if finished.
					if(qwi != null)
					{
						long count = 0;
						for(QuestItems qi : qwi.getQuestWorkItem())
						{
							if(qi != null)
							{	
								count = player.getInventory().getItemCountByItemId(qi.getItemId());
								if(count > 0)
									player.getInventory().removeFromBagByItemId(qi.getItemId(), count);	
							}
						}
					}
					// always apply bonus, don't check items, unless the bonus depends on the count 
					// of the crafted products
					BonusTemplate bonusTemplate = DataManager.BONUS_DATA.getBonusInfoByQuestId(questId);
					if(bonusTemplate != null)
					{
						List<AbstractInventoryBonus> bi = bonusTemplate.getItemBonuses();
                        for (AbstractInventoryBonus aib : bi)
                        {
                            aib.apply(player, null);
                        }
					}
					qs.setStatus(QuestStatus.COMPLETE);
					abortQuest(env);
					qs.setCompliteCount(qs.getCompleteCount() + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean onQuestFinishEvent(QuestCookie env)
	{
		return true;
	}

	public boolean onQuestAbortEvent(QuestCookie env)
	{
		abortQuest(env);
		return true;
	}
	
	private void abortQuest(QuestCookie env)
	{
		env.getPlayer().getRecipeList().deleteRecipe(env.getPlayer(), workOrdersData.getRecipeId());
	}
}
