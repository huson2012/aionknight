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