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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.CashShopManager.ShopItem;
import java.nio.ByteBuffer;

public class SM_INGAMESHOP_ITEMS extends AionServerPacket
{
	private final ShopItem[] items;
	private final int page;
	private final int total;
	private final int catId;
	
	public SM_INGAMESHOP_ITEMS(ShopItem[] items, int catId, int page, int total)
	{
		this.items = items;
		this.catId = catId;
		this.page = page;
		this.total = total;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, catId);//category Id
		writeD(buf, page);//page or ranking
		writeD(buf, total);//all items
		writeH(buf, items.length);//items on page

		for(ShopItem item : items)
			writeD(buf, item.id);
	}
}