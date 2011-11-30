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
