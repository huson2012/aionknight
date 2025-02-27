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

package gameserver.model.trade;

import gameserver.dataholders.DataManager;
import gameserver.dataholders.TradeListData;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.ItemService;
import gameserver.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeList
{
	private int sellerObjId;
	private List<TradeItem> tradeItems = new ArrayList<TradeItem>();
	private long requiredKinah;
	private int currencyId;
	private int requiredAp;
	private Map<Integer, Integer> requiredItems	= new HashMap<Integer, Integer>();
	private static final TradeListData tradeListData = DataManager.TRADE_LIST_DATA;

	/**
	 * @param itemId
	 * @param count
	 */
	public void addBuyItem(int itemId, long count)
	{
		ItemTemplate itemTemplate = ItemService.getItemTemplate(itemId);
		if(itemTemplate != null)
		{
			TradeItem tradeItem = new TradeItem(itemId, count);
			tradeItem.setItemTemplate(itemTemplate);
			tradeItems.add(tradeItem);
		}
	}

	/**
	 * @param itemId
	 * @param count
	 */
	public void addPSItem(int itemId, long count)
	{
		ItemTemplate itemTemplate = ItemService.getItemTemplate(itemId);
		if(itemTemplate != null)
		{
			TradeItem tradeItem = new TradeItem(itemId, count);
			tradeItems.add(tradeItem);
		}
	}

	/**
	 * @param itemObjId
	 * @param count
	 */
	public void addSellItem(int itemObjId, long count)
	{
		TradeItem tradeItem = new TradeItem(itemObjId, count);
		tradeItems.add(tradeItem);
	}

	/**
	 * @return price TradeList sum price
	 */
	public boolean calculateBuyListPrice(Player player)
	{
		long availableKinah = player.getInventory().getKinahItem().getItemCount();
		long priceCheck = 0;
		long priceWithRates = 0;
		requiredKinah = 0;

		Npc npc = (Npc) World.getInstance().findAionObject(this.sellerObjId);
		TradeListTemplate tradeListTemplate = tradeListData.getTradeListTemplate(npc.getObjectTemplate()
			.getTemplateId());

		for(TradeItem tradeItem : tradeItems)
		{
			priceWithRates = Math.round((tradeItem.getItemTemplate().getPrice() + (tradeItem.getItemTemplate().getPrice() * tradeListTemplate.getBuyRate()))
				* tradeListTemplate.getSellRate());
			priceCheck = player.getPrices().getKinahForBuy(priceWithRates, player.getCommonData().getRace());
			if(priceCheck <= 0)
				priceCheck = 1;
			requiredKinah += priceCheck * tradeItem.getCount();
		}

		return availableKinah >= requiredKinah;
	}

	/**
	 * @return true or false
	 */
	public boolean calculateAbyssBuyListPrice(Player player)
	{
		int ap = player.getAbyssRank().getAp();

		this.requiredAp = 0;
		this.requiredItems.clear();
		for(TradeItem tradeItem : tradeItems)
		{
			requiredAp += tradeItem.getItemTemplate().getAbyssPoints() * tradeItem.getCount();
			int itemId = tradeItem.getItemTemplate().getAbyssItem();

			Integer alreadyAddedCount = requiredItems.get(itemId);
			if(alreadyAddedCount == null)
				requiredItems.put(itemId, tradeItem.getItemTemplate().getAbyssItemCount());
			else
				requiredItems.put(itemId, alreadyAddedCount + tradeItem.getItemTemplate().getAbyssItemCount());
		}

		if(ap < requiredAp || requiredAp < 0)
			return false;

		for(Integer itemId : requiredItems.keySet())
		{
			long count = player.getInventory().getItemCountByItemId(itemId);
			if(count < requiredItems.get(itemId))
				return false;
		}

		return true;
	}

	/**
	 * @return true or false
	 */
	public boolean calculateExtraCurrencyBuyListPrice(Player player)
	{
		this.requiredItems.clear();
		this.currencyId = 0;

		for(TradeItem tradeItem : tradeItems)
		{
			if(currencyId == 0)
				currencyId = tradeItem.getItemTemplate().getExtraCurrencyItem();
			else if(tradeItem.getItemTemplate().getExtraCurrencyItem() != currencyId)
				continue;

			Integer alreadyAddedCount = requiredItems.get(currencyId);
			if(alreadyAddedCount == null)
				requiredItems.put(currencyId, (int)(tradeItem.getItemTemplate().getExtraCurrencyItemCount() * tradeItem.getCount()));
			else
				requiredItems.put(currencyId, alreadyAddedCount
					+ (int)(tradeItem.getItemTemplate().getExtraCurrencyItemCount() * tradeItem.getCount()));
		}

		for(Integer itemId : requiredItems.keySet())
		{
			long count = player.getInventory().getItemCountByItemId(itemId);
			if(count < requiredItems.get(itemId))
				return false;
		}

		return true;
	}

	/**
	 * @return the tradeItems
	 */
	public List<TradeItem> getTradeItems()
	{
		return tradeItems;
	}

	public int size()
	{
		return tradeItems.size();
	}

	/**
	 * @return the npcId
	 */
	public int getSellerObjId()
	{
		return sellerObjId;
	}

	/**
	 * @param sellerObjId
	 *           the npcId to set
	 */
	public void setSellerObjId(int npcObjId)
	{
		this.sellerObjId = npcObjId;
	}

	/**
	 * @return the requiredAp
	 */
	public int getRequiredAp()
	{
		return requiredAp;
	}

	/**
	 * @return the requiredKinah
	 */
	public long getRequiredKinah()
	{
		return requiredKinah;
	}

	/**
	 * @return the currencyId
	 */
	public int getCurrencyId()
	{
		return currencyId;
	}

	/**
	 * @return the requiredItems
	 */
	public Map<Integer, Integer> getRequiredItems()
	{
		return requiredItems;
	}
}