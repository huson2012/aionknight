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

import gameserver.dataholders.DataManager;
import gameserver.dataholders.TradeListData;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.item.ItemQuality;
import gameserver.model.trade.TradeRepurchaseList;
import gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class RepurchaseService
{
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(TradeService.class);

	private static final TradeListData		tradeListData = DataManager.TRADE_LIST_DATA;

	/**
	 *
	 * @param Player
	 * @param TradeRepurchaseList
	 * @return true or false
	 */
	public static boolean performBuyFromRepurchase(Player player, TradeRepurchaseList tradeList)
	{
		Storage inventory = player.getInventory();
		Item kinahItem = inventory.getKinahItem();

		// 1. check kinah
		if(!tradeList.calculateBuyListPrice(player))
			return false;

		// 2. check free slots, need to check retail behaviour
		int freeSlots = inventory.getLimit() - inventory.getAllItems().size() + 1;
		if(freeSlots < tradeList.size())
			return false; // TODO message

		long tradeListPrice = player.getPrices().getKinahForSell(tradeList.getRequiredKinah(), player.getCommonData().getRace());

		List<Item> addedItems = new ArrayList<Item>();
		
		if(!inventory.decreaseKinah(tradeListPrice))
			return false;
		
		for(Item tradeItem : tradeList.getTradeItems())
		{
			if(tradeItem.getItemTemplate().getItemQuality() != ItemQuality.JUNK)
				ItemService.addRepurchaseItem(player, tradeItem);
		}		

		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(kinahItem));
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE(addedItems));
		// TODO message
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
