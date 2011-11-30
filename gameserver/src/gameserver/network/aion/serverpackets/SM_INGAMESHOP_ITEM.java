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
		//writeD(buf, 0); //unk
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
