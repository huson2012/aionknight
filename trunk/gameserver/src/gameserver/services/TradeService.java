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

import gameserver.configs.main.GSConfig;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.GoodsListData;
import gameserver.dataholders.TradeListData;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.AbyssRank;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.TradeListTemplate.TradeTab;
import gameserver.model.templates.goods.GoodsList;
import gameserver.model.trade.TradeItem;
import gameserver.model.trade.TradeList;
import gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

import java.util.*;

public class TradeService
{
	private static final Logger	log	= Logger.getLogger(TradeService.class);
	private static final TradeListData tradeListData = DataManager.TRADE_LIST_DATA;
	private static final GoodsListData goodsListData = DataManager.GOODSLIST_DATA;

	/**
	 * @param player
	 * @param tradeList
	 * @return true or false
	 */
	public static boolean performBuyFromShop(Player player, TradeList tradeList)
	{

		if(!validateBuyItems(tradeList))
		{
			PacketSendUtility.sendMessage(player, "Some items are not allowed to be sold by this npc.");
			return false;
		}

		Storage inventory = player.getInventory();
		Item kinahItem = inventory.getKinahItem();

		// 1. check kinah
		if(!tradeList.calculateBuyListPrice(player))
			return false;

		// 2. check free slots, need to check retail behaviour
		int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
		if(freeSlots < tradeList.size())
			return false; // TODO message

		long tradeListPrice = tradeList.getRequiredKinah();
		
		if(!inventory.decreaseKinah(tradeListPrice))
			return false;

		List<Item> addedItems = new ArrayList<Item>();
		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
			if(count != 0)
			{
				log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player
					.getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
				return false;
			}
		}

		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(kinahItem));
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
		// TODO message
		return true;
	}

	/**
	 * Probably later merge with regular buy
	 * 
	 * @param player
	 * @param tradeList
	 * @return true or false
	 */
	public static boolean performBuyFromAbyssShop(Player player, TradeList tradeList)
	{

		if(!validateBuyItems(tradeList))
		{
			PacketSendUtility.sendMessage(player, "Some items are not allowed to be selled from this npc");
			return false;
		}

		Storage inventory = player.getInventory();
		int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
		AbyssRank rank = player.getAbyssRank();

		// 1. check required items and ap
		if(!tradeList.calculateAbyssBuyListPrice(player))
			return false;
		
        if (tradeList.getRequiredAp() < 0)
        {
                log.warn("[AUDIT] Player: " + player.getName() + " possible client hack. tradeList.getRequiredAp() < 0");
                return false;
        }

		// 2. check free slots, need to check retail behaviour
		if(freeSlots < tradeList.size())
			return false; // TODO message

		List<Item> addedItems = new ArrayList<Item>();
		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
			if(count != 0)
			{
				log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player
					.getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
				player.getCommonData().setAp(rank.getAp()-tradeList.getRequiredAp());
				return false;
			}
		}
		player.getCommonData().setAp(rank.getAp()-tradeList.getRequiredAp());
		Map<Integer, Integer> requiredItems = tradeList.getRequiredItems();
		for(Map.Entry<Integer, Integer> entry : requiredItems.entrySet())
		{
			if(!player.getInventory().removeFromBagByItemId(entry.getKey(), entry.getValue()))
				return false;
		}

		PacketSendUtility.sendPacket(player, new SM_ABYSS_RANK(rank));
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
		// TODO message
		return true;
	}
	
	/**
	 * Probably later merge with regular buy
	 * 
	 * @param player
	 * @param tradeList
	 * @return true or false
	 */
	public static boolean performBuyWithExtraCurrency(Player player, TradeList tradeList)
	{

		if(!validateBuyItems(tradeList))
		{
			PacketSendUtility.sendMessage(player, "Some items are not allowed to be selled from this npc");
			return false;
		}

		Storage inventory = player.getInventory();
		int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;

		// 1. check required items
		if(!tradeList.calculateExtraCurrencyBuyListPrice(player))
			return false;

		// 2. check free slots, need to check retail behaviour
		if(freeSlots < tradeList.size())
			return false; // TODO message

		List<Item> addedItems = new ArrayList<Item>();
		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
			if(count != 0)
			{
				log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player
					.getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
				return false;
			}
		}
		Map<Integer, Integer> requiredItems = tradeList.getRequiredItems();
		for(Map.Entry<Integer, Integer> entry : requiredItems.entrySet())
		{
			if(!player.getInventory().removeFromBagByItemId(entry.getKey(), entry.getValue()))
				return false;
		}

		for (Item item : player.getInventory().getAllItemsByItemId(tradeList.getCurrencyId()))
		{
			PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(item));
		}
		
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
		// TODO message
		return true;
	}

	/**
	 * @param tradeList
	 */
	private static boolean validateBuyItems(TradeList tradeList)
	{
		Npc npc = (Npc) World.getInstance().findAionObject(tradeList.getSellerObjId());
		TradeListTemplate tradeListTemplate = tradeListData.getTradeListTemplate(npc.getObjectTemplate()
			.getTemplateId());

		Set<Integer> allowedItems = new HashSet<Integer>();
		for(TradeTab tradeTab : tradeListTemplate.getTradeTablist())
		{
			GoodsList goodsList = goodsListData.getGoodsListById(tradeTab.getId());
			if(goodsList != null && goodsList.getItemIdList() != null)
			{
				allowedItems.addAll(goodsList.getItemIdList());
			}
		}

		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			if(!allowedItems.contains(tradeItem.getItemId()))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param player
	 * @param tradeList
	 * @return true or false
	 */
	public static boolean performSellToShop(Player player, TradeList tradeList)
	{
		Storage inventory = player.getInventory();

		long kinahReward = 0;
		for(TradeItem tradeItem : tradeList.getTradeItems())
		{
			Item item = inventory.getItemByObjId(tradeItem.getItemId());
			// 1) don't allow to sell fake items;
			if(item == null)
				return false;
			
			tradeItem.setItemTemplate(item.getItemTemplate());
			
			// 2) don't allow to sell non-sellable items
			if(!item.getItemTemplate().isSellable())
			{
				log.warn("[AUDIT] Selling exploit, tried to sell unsellable item: " + player.getName());
				return false;
			}
			
			if(item.getItemCount() - tradeItem.getCount() == 0)
			{
				boolean removeResult = inventory.removeFromBag(item, true);
				if(!removeResult)
					return false;
				
				kinahReward += item.getItemTemplate().getPrice() * item.getItemCount();
				
				// TODO check retail packet here
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(item.getObjectId()));

				if(GSConfig.ENABLE_REPURCHASE)
					player.getRepurchase().addItem(item.getObjectId(), item);
			}
			else if(item.getItemCount() - tradeItem.getCount() > 0)
			{
				if(inventory.decreaseItemCount(item, tradeItem.getCount()) > 0)
				{
					// TODO check retail packet here
					kinahReward += item.getItemTemplate().getPrice() * tradeItem.getCount();
					PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(item));
				}
				else
					return false;
			}
			else
				return false;
		}

		Item kinahItem = inventory.getKinahItem();
		kinahReward = player.getPrices().getKinahForSell(kinahReward, player.getCommonData().getRace());
		inventory.increaseKinah(kinahReward);
		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(kinahItem));

		return true;
	}
	
	/**
	 * @return the tradeListData
	 */
	public static TradeListData getTradeListData()
	{
		return tradeListData;
	}	
}
