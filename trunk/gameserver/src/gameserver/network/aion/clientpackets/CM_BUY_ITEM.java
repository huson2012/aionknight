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

package gameserver.network.aion.clientpackets;

import gameserver.configs.main.GSConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.trade.TradeItem;
import gameserver.model.trade.TradeList;
import gameserver.model.trade.TradeListType;
import gameserver.model.trade.TradeRepurchaseList;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.services.PrivateStoreService;
import gameserver.services.PurchaseLimitService;
import gameserver.services.RepurchaseService;
import gameserver.services.TradeService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import org.apache.log4j.Logger;

public class CM_BUY_ITEM extends AionClientPacket
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(CM_BUY_ITEM.class);

	private int					sellerObjId;
	private int					unk1;
	private int					amount;
	private int					itemId;
	private int					count;
	@SuppressWarnings("unused")
	private int					unk2;
	private Player				player;
	private TradeList			tradeList;
	private TradeRepurchaseList	repurchaseList;

	public CM_BUY_ITEM(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		sellerObjId = readD();
		
		if(sellerObjId == 0)
			return;
		
		unk1 = readH();
		amount = readH();
		player = getConnection().getActivePlayer();

		tradeList = new TradeList();
		tradeList.setSellerObjId(sellerObjId);

		repurchaseList = new TradeRepurchaseList();
		tradeList.setSellerObjId(sellerObjId);

		for(int i = 0; i < amount; i++)
		{
			itemId = readD();
			count = readD();
			unk2 = readD();

			// prevent exploit packets
			if(count < 1 || itemId == 0)
				continue;

			if(unk1 == 13 || unk1 == 14 || unk1 == 15)
			{
				tradeList.addBuyItem(itemId, count);
			}
			else if(unk1 == 0 || unk1 == 1)
			{
				tradeList.addSellItem(itemId, count);
			}
			else if(unk1 == 2)
			{
				if(GSConfig.ENABLE_REPURCHASE)
					repurchaseList.addBuyItemRepurchase(itemId, player);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionObject obj = World.getInstance().findAionObject(sellerObjId);
		Player targetPlayer = null;

		try
		{
			player.setTrading(true);
			switch(unk1)
			{
				case 0:
					targetPlayer = (Player) obj;
					targetPlayer.setTrading(true);
					PrivateStoreService.sellStoreItem(targetPlayer, player, tradeList);
					break;
				case 1:
					TradeService.performSellToShop(player, tradeList);
					break;
				case 2:
					if(GSConfig.ENABLE_REPURCHASE)
						RepurchaseService.performBuyFromRepurchase(player, repurchaseList);
					break;
				case 13:
					if(PurchaseLimitService.getInstance().addCache(obj, player, tradeList))
					{
						Npc npc = (Npc) World.getInstance().findAionObject(sellerObjId);
						TradeListTemplate tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
						if(tlist.getType() == TradeListType.KINAH)
							TradeService.performBuyFromShop(player, tradeList);
						else
							log.info("[CHEAT]Player: "+player.getName()+" abusing CM_BUY_ITEM!");
					}
					else
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400353));
					break;
				case 14:
					Npc npc = (Npc) World.getInstance().findAionObject(sellerObjId);
					TradeListTemplate tlist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc.getNpcId());
					if(tlist.getType() == TradeListType.ABYSS)
						TradeService.performBuyFromAbyssShop(player, tradeList);
					break;
				case 15:
					Npc npc2 = (Npc) World.getInstance().findAionObject(sellerObjId);
					TradeListTemplate elist = DataManager.TRADE_LIST_DATA.getTradeListTemplate(npc2.getNpcId());
					if(elist.getType() == TradeListType.EXTRA)
						TradeService.performBuyWithExtraCurrency(player, tradeList);
					break;
				default:
					log.info(String.format("Unhandle shop action unk1: %d", unk1));
					break;
			}
		}
		finally
		{
			if(targetPlayer != null)
				targetPlayer.setTrading(false);
			player.setTrading(false);
		}

		VisibleObject visibleObject = null;
		if(obj instanceof VisibleObject)
			visibleObject = (VisibleObject) obj;

		for(TradeItem item : tradeList.getTradeItems())
		{
			ItemTemplate template = item.getItemTemplate();
			if(template != null && player != null)
				QuestEngine.getInstance().onItemSellBuyEvent(
					new QuestCookie(visibleObject, player, template.getItemQuestId(), 0), template.getTemplateId());
		}

	}
}
