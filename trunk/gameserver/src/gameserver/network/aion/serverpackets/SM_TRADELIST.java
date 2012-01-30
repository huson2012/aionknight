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

package gameserver.network.aion.serverpackets;

import gameserver.configs.main.GSConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.PurchaseLimit;
import gameserver.model.templates.TradeListTemplate;
import gameserver.model.templates.TradeListTemplate.TradeTab;
import gameserver.model.templates.goods.GoodsList;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.PurchaseLimitService;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SM_TRADELIST extends AionServerPacket
{
	private int targetObjectId;
	private int npcTemplateId;
	private TradeListTemplate tlist;
	private int buyPriceModifier;
	private int finalPriceModifier;
		
	public SM_TRADELIST(Npc npc, TradeListTemplate tlist, int buyPriceModifier)
	{
		if(tlist == null)
			return;
		this.targetObjectId = npc.getObjectId();
		this.npcTemplateId = npc.getNpcId();
		this.tlist = tlist;
		this.buyPriceModifier = buyPriceModifier;
		finalPriceModifier = Math.round((buyPriceModifier + (tlist.getBuyRate() * buyPriceModifier)) * tlist.getSellRate());
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if ((tlist != null)&&(tlist.getNpcId()!=0)&&(tlist.getCount()!=0))
		{
			writeD(buf, targetObjectId);
			switch (tlist.getType()) {
				case ABYSS:
					writeC(buf, 2);
					break;
				case COUPON:
					writeC(buf, 3);
					break;
				case EXTRA:
					writeC(buf, 4);
					break;
				default:
					writeC(buf, 1);
			}
			
			writeD(buf, finalPriceModifier); // Vendor Buy Price Modifier
			writeH(buf, tlist.getCount());

			List<GoodsList> list = new ArrayList<GoodsList>();
			boolean isLimited = false;
			int countItems = 0;

			for(TradeTab tradeTabl : tlist.getTradeTablist())
			{
				writeD(buf, tradeTabl.getId());

				if(GSConfig.ENABLE_PURCHASE_LIMIT)
				{
					GoodsList goodsListAdd = DataManager.GOODSLIST_DATA.getGoodsListById(tradeTabl.getId());

					if(goodsListAdd != null && goodsListAdd.isLimited())
					{
						isLimited = true;
						countItems += goodsListAdd.getItemsList().size();

						list.add(goodsListAdd);
					}
				}
			}

			if(isLimited)
			{
				PurchaseLimit purchaseLimit = con.getActivePlayer().getPurchaseLimit();

				writeH(buf, countItems);

				for(GoodsList goodsList : list)
				{
					if(goodsList != null && goodsList.getItemsList() != null && purchaseLimit != null)
					{
						for(GoodsList.Item item : goodsList.getItemsList())
						{
							writeD(buf, item.getId());
							writeH(buf, purchaseLimit.getItemLimitCount(item.getId()));
							writeH(buf, (item.getSelllimit() - PurchaseLimitService.getInstance().getCountItem(item.getId())));
						}
					}
				}
			}
		}
		else if(tlist == null)
		{
			Logger.getLogger(SM_TRADELIST.class).warn("Empty TradeListTemplate for NpcId: " + npcTemplateId);
			writeD(buf, targetObjectId);
			writeC(buf, 1);
			writeD(buf, buyPriceModifier);
			writeH(buf, 0);
		}
	}
}