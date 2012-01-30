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

public class SM_INGAMESHOP_ITEM extends AionServerPacket
{
	private final ShopItem item;
	
	public SM_INGAMESHOP_ITEM(ShopItem item)
	{
		this.item = item;
	}

	@Override
	public void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, item.id);
		writeQ(buf, item.price);
		writeH(buf, 0); //unk
		writeD(buf, item.itemId);
		writeD(buf, item.count);
		writeD(buf, 0);
		writeD(buf, 0);//if greater than 0, shows a stack of boxes
		writeD(buf, 0);//if greater than 0, shows a stack of boxes
		writeC(buf, item.eyecatch);//0- regular, 1-new, 2-hot
		writeD(buf, 0);
		writeD(buf, 0);
		writeH(buf, 0);
		writeS(buf, item.name);
		writeS(buf, item.desc);
	}
}