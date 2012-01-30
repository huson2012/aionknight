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

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PrivateStore;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.trade.TradePSItem;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.InventoryPacket;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

public class SM_PRIVATE_STORE extends InventoryPacket
{
	/** Private store Information **/
	private PrivateStore store;

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