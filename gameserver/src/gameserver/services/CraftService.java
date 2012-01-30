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

package gameserver.services;

import commons.utils.Rnd;
import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.recipe.ComboProduct;
import gameserver.model.templates.recipe.Component;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.model.SkillTemplate;
import gameserver.skill.task.CraftingTask;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CraftService 
{
	private static final Logger log = Logger.getLogger(CraftService.class);

	/**
	 * @param player
	 * @param recipetemplate
	 */
	public static ItemTemplate finishCrafting(Player player, RecipeTemplate recipetemplate, int itemId)
	{
		boolean critical = false;
		boolean getReward = false;
		int critItemId = 0;
		
		if(recipetemplate.getComboProduct().isEmpty())
			getReward = true;
		else
			critical = Rnd.get(100) <= CustomConfig.CRITICAL_CRAFTING_SUCCESS;
		
		if(critical)
		{
			boolean getNext = false;
			
			for(ComboProduct comboProduct: recipetemplate.getComboProduct())
			{
				if(itemId == recipetemplate.getProductid() && critItemId == 0)
				{
					critItemId = comboProduct.getItemid();
					break;
				}
				else if(itemId == comboProduct.getItemid())
					getNext = true;
				else if(getNext)
				{
					critItemId = comboProduct.getItemid();
					break;
				}
			}
			
			if(critItemId == 0)
				getReward = true;
		}
		else
			getReward = true;
		
		if(critical && !getReward)
			return ItemService.getItemTemplate(critItemId);
		else
		{
			int skillId = recipetemplate.getSkillid();
			
			if(recipetemplate.getMaxProductionCount() == 1)
				player.getRecipeList().deleteRecipe(player, recipetemplate.getId());
			
			if(skillId == 40009)
			{
				ItemService.addItem(player, itemId, recipetemplate.getQuantity(), player.getName(), 0, 0);
				PacketSendUtility.sendPacket(player,SM_SYSTEM_MESSAGE.STR_COMBINE_SUCCESS(new DescriptionId(ItemService.getItemTemplate(itemId).getNameId())));
			}
			else if(itemId != 0 && skillId != 40009)
			{
				int skillPoint = recipetemplate.getSkillpoint();
				SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
				
				ItemService.addItem(player, itemId, recipetemplate.getQuantity(), player.getName(), 0, 0);
				
				if(skillPoint + 40 > player.getSkillList().getSkillLevel(skillId))
				{
					int xpReward = (int)((0.0144*skillPoint*skillPoint+3.5*skillPoint+270)*player.getRates().getCraftingLvlRate());
					if(player.getXpBoost() > 0)
						xpReward = xpReward * ((player.getXpBoost() / 100) + 1);
	
					if(player.getSkillList().addSkillXp(player, skillId, xpReward))
					{
						player.getCommonData().addExp((int)(xpReward*player.getRates().getCraftingXPRate()/player.getRates().getCraftingLvlRate()));
						PacketSendUtility.sendPacket(player ,SM_SYSTEM_MESSAGE.STR_CRAFT_SUCCESS_GETEXP);
					}
					else
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(skillTemplate.getNameId())));
				}
				else
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(skillTemplate.getNameId())));
			}
			player.setCraftingTask(null);
			return null;
		}
	}

	/**
	 * @param player
	 * @param recipeId
	 * @param targetObjId
	 */
	public static void startCrafting(Player player, int recipeId, int targetObjId)
	{
		if(player.getCraftingTask() != null && player.getCraftingTask().isInProgress())
			return;

		RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);

		if(recipeTemplate == null)
		{
			log.warn(String.format("recipeTemplate with id %d not found", recipeId));
			return;
		}
		int skillId = recipeTemplate.getSkillid();
		AionObject target = World.getInstance().findAionObject(targetObjId);
		
		if((skillId != 40009) && (target == null || !(target instanceof StaticObject)))
		{
			log.info("[AUDIT] Player " + player.getName() + " tried to craft incorrect target.");
			return;
		}
		if((skillId != 40009) && !MathUtil.isIn3dRange(player, (StaticObject)target, 10))
		{
			log.info("[AUDIT] Player " + player.getName() + " sending fake packet CM_CRAFT.");
			return;
		}
		
		if(recipeTemplate.getDp() != null && (player.getCommonData().getDp() < recipeTemplate.getDp()))
		{
			log.info("[AUDIT] Player " + player.getName() + " modded her/his client.");
			return;
		}
		
		if(player.getInventory().isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.COMBINE_INVENTORY_IS_FULL);
			return;
		}
		
		for(Component component : recipeTemplate.getComponent())
		{
			if(player.getInventory().getItemCountByItemId(component.getItemid()) < component.getQuantity())
			{
				log.info("[AUDIT] Player " + player.getName() + " modded her/his client.");
				return;
			}
		}
		
		ItemTemplate itemTemplate = ItemService.getItemTemplate(recipeTemplate.getProductid());
		
		if(itemTemplate == null)
		{
			log.warn(String.format("itemTemplate with id %d not found", recipeTemplate.getProductid()));
			return;
		}
	
		if(recipeTemplate.getDp() != null)
			player.getCommonData().addDp(-recipeTemplate.getDp());
	
		for(Component component : recipeTemplate.getComponent())
		{
			if(!player.getInventory().removeFromBagByItemId(component.getItemid(), component.getQuantity()))
				return;
		}
			player.setCraftingTask(new CraftingTask(player, (StaticObject)target, recipeTemplate, itemTemplate));
			player.getCraftingTask().start();
	}
}
