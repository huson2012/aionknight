package ru.aionknight.gameserver.network.aion.clientpackets;


import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.network.aion.AionConnection;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_INGAMESHOP;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_INGAMESHOP_BALANCE;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_INGAMESHOP_ITEM;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_INGAMESHOP_ITEMS;
import ru.aionknight.gameserver.services.CashShopManager;
import ru.aionknight.gameserver.services.CashShopManager.ShopItem;

public class CM_INGAMESHOP extends AionClientPacket
{
	public CM_INGAMESHOP(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		AionConnection client = getConnection();
		int type = readC();
		switch(type)
		{
		
			case 0x01:
				int itemId = readD();//item id
				ShopItem item = CashShopManager.getInstance().getItem(itemId);
				if(item != null)
					client.sendPacket(new SM_INGAMESHOP_ITEM(item));
				break;

			case 0x02:
				@SuppressWarnings("unused")
				int vector = readD();//Load all categories ?
				client.sendPacket(new SM_INGAMESHOP(1, CashShopManager.getInstance().getCategories()));
				break;

			case 0x04:
				client.sendPacket(new SM_INGAMESHOP_ITEMS(CashShopManager.getInstance().getItems(1, 0), 1, 0, CashShopManager.getInstance().getItemsCount()));
				client.sendPacket(new SM_INGAMESHOP_ITEMS(CashShopManager.getInstance().getRankItems(), 0, 0, CashShopManager.getInstance().getItemsCount()));
				break;

			case 0x08:
				int catId = readD();//categoty id, 1-All
				int page = readD();//page
				client.sendPacket(new SM_INGAMESHOP_ITEMS(CashShopManager.getInstance().getItems(catId, page), catId, page, CashShopManager.getInstance().getItemsCount(catId)));
				client.sendPacket(new SM_INGAMESHOP_ITEMS(CashShopManager.getInstance().getRankItems(), 0, 0, CashShopManager.getInstance().getItemsCount()));
				break;

			case 0x10:
				client.sendPacket(new SM_INGAMESHOP_BALANCE());
			break;

			case 0x20:
				itemId = readD();
				int count = readD();
				CashShopManager.getInstance().buyItem(client.getActivePlayer(), itemId, count);
				client.sendPacket(new SM_INGAMESHOP_BALANCE());
				break;

			case 0x40:
				itemId = readD();
				count = readD();
				String receiver = readS();//receiver name
				String message = readS();//message
				CashShopManager.getInstance().giftItem(client.getActivePlayer(), itemId, count, receiver, message);
				client.sendPacket(new SM_INGAMESHOP_BALANCE());
				break;
		}
	}

	@Override
	protected void runImpl()
	{
		
	}
}
