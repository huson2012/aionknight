/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.services;

import commons.database.DatabaseFactory;
import commons.database.dao.DAOManager;
import gameserver.configs.main.CustomConfig;
import gameserver.configs.network.NetworkConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

public class CashShopManager
{
	private static final Logger log = Logger.getLogger(CashShopManager.class);
	private static CashShopManager instance;
	public HashMap<Integer, ShopCategory> categories = new HashMap<Integer, ShopCategory>();

	public class ShopCategory
	{
		public int id;
		public String name;
		public HashMap<Integer, ShopItem> items = new HashMap<Integer, ShopItem>();
		public Timer timer;

		public ShopCategory(int id)
		{
			this.id = id;
		}
	}

	public class ShopItem
	{
		public int id;
		public int itemId;
		public int count;
		public long price;
		public String name;
		public String desc = "";
		public int eyecatch;

		public ShopItem(int id)
		{
			this.id = id;
		}
	}

	public static CashShopManager getInstance()
	{
		if(instance == null)
			instance = new CashShopManager();
		return instance;
	}

	public static CashShopManager reload()
	{
		return instance = null;
	}

	public CashShopManager()
	{
		// TODO: Переместить DAO

		log.info("Loading ingame shop...");

		// Первая категория (id 1) выводит на экран все элементы. Любые дополнительные категории, 
		// должны быть id 2 и т.д.
		@SuppressWarnings("unused")
		int catId = 1;
		@SuppressWarnings("unused")
		int itId = 0;

		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM " + CustomConfig.AIONSHOP_DB + ".aionshop_categories");
			ResultSet catRs = stmt.executeQuery();
			while(catRs.next())
			{
				ShopCategory cat = new ShopCategory(catRs.getInt("categoryId"));
				cat.name = catRs.getString("categoryName");

				PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM " + CustomConfig.AIONSHOP_DB + ".aionshop_items WHERE itemCategory = ?");
				stmt2.setInt(1, cat.id);
				ResultSet rs2 = stmt2.executeQuery();

				while(rs2.next())
				{
					ShopItem item = new ShopItem(rs2.getInt("itemUniqueId"));
					item.itemId = rs2.getInt("itemTemplateId");
					item.count = rs2.getInt("itemCount");
					item.price = rs2.getInt("itemPrice");
					item.name = rs2.getString("itemName");
					item.desc = rs2.getString("itemDesc");
					item.eyecatch = rs2.getInt("itemEyecatch");
					cat.items.put(item.id, item);
				}

				rs2.close();
				stmt2.close();

				categories.put(cat.id, cat);
			}

			catRs.close();
			stmt.close();
			con.close();

		}
		catch(Exception e)
		{
			log.error("Cannot load ingame shop contents !", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		log.info("Loaded " + getAllItems().length + " items.");
	}

	public int getItemsCount()
	{
		return getAllItems().length;
	}

	public int getItemsCount(int catId)
	{
		return getItems(catId).length;
	}

	public ShopItem getItem(int id)
	{
		for(ShopCategory category : categories.values())
			if(category.items.containsKey(id))
				return category.items.get(id);
		return null;
	}

	public ShopItem[] getAllItems()
	{
		ArrayList<ShopItem> res = new ArrayList<ShopItem>();
		for(ShopCategory category : categories.values())
			res.addAll(category.items.values());
		return res.toArray(new ShopItem[res.size()]);
	}

	public ShopItem[] getItems(int catId)
	{
		if(catId == 1)
			return getAllItems();
		ShopCategory category = categories.get(catId);
		if(category == null)
			return new ShopItem[0];
		return category.items.values().toArray(new ShopItem[category.items.size()]);
	}

	public ShopItem[] getItems(int catId, int page)
	{
		ShopItem[] list = getItems(catId);
		ShopItem[] res = new ShopItem[9];
		int n = 0;
		for(int i = page * 9; i < (page + 1) * 9; i++)
		{
			if(i < list.length)
			{
				res[n] = list[i];
				n++;
			}
			else
				break;
		}
		if(n != 9)
			res = compact(res, n);
		return res;
	}

	private ShopItem[] compact(ShopItem[] items, int size)
	{
		ShopItem[] new_items = new ShopItem[size];
		System.arraycopy(items, 0, new_items, 0, size);
		return new_items;
	}

	public ShopCategory[] getCategories()
	{
		return categories.values().toArray(new ShopCategory[categories.size()]);
	}

	public void buyItem(Player player, int id, int count)
	{
		ShopItem item = getItem(id);

		if(item == null)
		{
			// Вы не смогли приобрести товар.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400105));
			return;
		}
		if(player.shopMoney < item.price)
		{
			// Вам не хватает денежных средств.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400108));
			return;
		}

		if(count > player.getInventory().getNumberOfFreeSlots())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
			return;
		}
		ItemService.addItem(player, item.itemId, item.count);

		decreaseAndUpdate(player, item.price);

		logPurchase(player, item.itemId);
	}

	void decreaseAndUpdate(Player player, long value)
	{
		/**
		 * param value : кредиты провел
		 *
		 * Добавить свой код перед отправкой пакета обновления кредитов, если вам нужно конечно.
		 * PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
		 *
		 */
		decreaseCredits(player, value);
	}

	void increaseAndUpdate(Player player, long value)
	{
		/**
		 * param value : кредиты провел
		 *
		 * Добавить свой код перед отправкой пакета обновления кредитов, если вам нужно конечно.
		 * PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
		 *
		 */
		increaseCredits(player, value);
	}

	public void giftItem(Player player, int id, int count, String receiver, String message)
	{
		ShopItem item = getItem(id);

		if(!CustomConfig.AIONSHOP_GIFT_ENABLE)
		{
			PacketSendUtility.sendMessage(player, "Gift service is not enabled");
			return;
		}
		if(item == null)
		{
			// Вы не смогли приобрести товар.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400105));
			return;
		}
		if(player.shopMoney < item.price)
		{
			// Вам не хватает денежных средств.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400108));
			return;
		}

		PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(receiver);

		if(pcd == null || pcd.getRace() != player.getCommonData().getRace())
		{
			// Вы выбрали неверную цель, чтобы дарить подарок.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400106));
			return;
		}

		if(player.getName() == receiver)
		{
			// Вы не можете дарить подарки самому себе.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400116));
			return;
		}

		// Проверки прошли, перейдем к пункту отправки:
		decreaseAndUpdate(player, item.price);

		MailService.getInstance().sendSystemMail("CASH_ITEM_MAIL", "Gift from " + player.getName(), message, pcd.getPlayerObjId(), ItemService.newItem(item.itemId, item.count, "Purchased at Black Cloud Traders Shop", pcd.getPlayerObjId(), 0, 0), 0);

		// Подарок был успешно доставлен.
		PacketSendUtility.sendMessage(player, "Your gift has been delivered successfully");
		return;
	}

	public void logPurchase(Player player, int itemUniqueId)
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO " + CustomConfig.AIONSHOP_DB + ".aionshop_transactions(server_id,item_unique_id,buy_timestamp,player_id) VALUES(?,?,?,?)");
			stmt.setInt(1, NetworkConfig.GAMESERVER_ID);
			stmt.setInt(2, itemUniqueId);
			stmt.setLong(3, System.currentTimeMillis() / 1000);
			stmt.setLong(4, player.getObjectId());
			stmt.execute();
			con.close();
		}
		catch(Exception e)
		{
			log.error("Cannot log purchase ! " + player.getObjectId() + " - " + itemUniqueId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	public int getPlayerCredits(Player player)
	{
		int result = -1;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT credits FROM " + CustomConfig.AIONSHOP_DB + ".account_data WHERE name = ?");
			stmt.setString(1, player.getAcountName());
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				result = rs.getInt("credits");
			}
			stmt.close();
			con.close();
			log.info("Got player credits = " + result);
		}
		catch(Exception e)
		{
			log.error("Cannot get credits !", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		player.shopMoney = result;

		return result;
	}

	public ShopItem[] getRankItems()
	{
		Connection con = null;
		ArrayList<ShopItem> items = new ArrayList<ShopItem>();

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT i.*, COUNT(t.item_unique_id) as total FROM " + CustomConfig.AIONSHOP_DB + ".aionshop_items i, " + CustomConfig.AIONSHOP_DB + ".aionshop_transactions t WHERE t.item_unique_id = i.itemTemplateId GROUP BY t.item_unique_id ORDER BY total DESC LIMIT 6");
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
			{
				ShopItem item = new ShopItem(rs.getInt("itemUniqueId"));
				item.itemId = rs.getInt("itemTemplateId");
				item.count = rs.getInt("itemCount");
				item.price = rs.getInt("itemPrice");
				item.name = rs.getString("itemName");
				item.desc = rs.getString("itemDesc");
				item.eyecatch = rs.getInt("itemEyecatch");
				items.add(item);
			}
			stmt.close();
			con.close();
		}
		catch(Exception e)
		{
			log.error("Cannot get Rank Items !", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return items.toArray(new ShopItem[items.size()]);
	}

	public void decreaseCredits(Player player, long value)
	{
		player.shopMoney -= value;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE " + CustomConfig.AIONSHOP_DB + ".account_data SET credits = ? WHERE name = ?");
			stmt.setInt(1, (int) player.shopMoney);
			stmt.setString(2, player.getAcountName());
			stmt.execute();
			stmt.close();
			log.info("Decreased " + player.getName() + "'s credits by " + value);
		}
		catch(Exception e)
		{
			log.error("Cannot get credits !", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	public void increaseCredits(Player player, long value)
	{
		player.shopMoney += value;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE " + CustomConfig.AIONSHOP_DB + ".account_data SET credits = ? WHERE name = ?");
			stmt.setInt(1, (int) player.shopMoney);
			stmt.setString(2, player.getAcountName());
			stmt.execute();
			stmt.close();
			log.info("Increased " + player.getName() + "'s credits by " + value);
		}
		catch(Exception e)
		{
			log.error("Cannot set credits !", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
}