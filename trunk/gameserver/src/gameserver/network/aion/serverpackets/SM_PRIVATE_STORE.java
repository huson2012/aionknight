/**
 * This file is part of Aion-Knight <www.Aion-Knight.com>.
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
package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PrivateStore;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.trade.TradePSItem;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;


/**
 * @author Simple
 */
public class SM_PRIVATE_STORE extends InventoryPacket
{
	/** Private store Information **/
	private PrivateStore	store;

	public SM_PRIVATE_STORE(PrivateStore store)
	{
		this.store = store;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(store != null)
		{
			Player storePlayer = store.getOwner();
			LinkedHashMap<Integer, TradePSItem> soldItems = store.getSoldItems();
			
			writeD(buf, storePlayer.getObjectId());
			writeH(buf, soldItems.size());
			for(Integer itemObjId : soldItems.keySet())
			{
				Item item = storePlayer.getInventory().getItemByObjId(itemObjId);
				TradePSItem tradeItem = store.getTradeItemById(itemObjId);
				long price = tradeItem.getPrice();
				writeD(buf, itemObjId);
				writeD(buf, item.getItemTemplate().getTemplateId());
				writeH(buf, (int) tradeItem.getCount());
				writeD(buf, (int) price);

				ItemTemplate itemTemplate = item.getItemTemplate();

				if (itemTemplate.isWeapon())
				{
					writeWeaponInfo(buf, item);
				}
				else if (itemTemplate.isArmor())
				{
					writeArmorInfo(buf, item);
				}
				else
				{
					writeGeneralItemInfo(buf, item);
				}
			}
		}
	}
}