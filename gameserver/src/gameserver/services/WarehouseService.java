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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.player.StorageType;
import gameserver.model.templates.WarehouseExpandTemplate;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_WAREHOUSE_INFO;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;

import java.util.List;

public class WarehouseService
{
	private static final Logger	log			= Logger.getLogger(WarehouseService.class);

	private static final int	MIN_EXPAND	= 0;
	private static final int	MAX_EXPAND	= 10;

	/**
	 * Shows Question window and expands on positive response
	 * 
	 * @param player
	 * @param npc
	 */
	public static void expandWarehouse(final Player player, Npc npc)
	{
		final WarehouseExpandTemplate expandTemplate = DataManager.WAREHOUSEEXPANDER_DATA.getWarehouseExpandListTemplate(npc
			.getNpcId());

		if(expandTemplate == null)
		{
			log.error("Warehouse Expand Template could not be found for Npc ID: " + npc.getObjectId());
			return;
		}

		if(npcCanExpandLevel(expandTemplate, player.getWarehouseSize() + 1)
			&& validateNewSize(player.getWarehouseSize() + 1))

			if(validateNewSize(player.getWarehouseSize() + 1))
			{
				/**
				 * Check if our player can pay the warehouse expand price
				 */
				final int price = getPriceByLevel(expandTemplate, player.getWarehouseSize() + 1);
				RequestResponseHandler responseHandler = new RequestResponseHandler(npc){
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						if(player.getInventory().getKinahItem().getItemCount() < price)
						{
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300831));
							return;
						}
						if(player.getInventory().decreaseKinah(price))
							expand(responder);
						
					}

					@Override
					public void denyRequest(Creature requester, Player responder)
					{
						// nothing to do
					}
				};

				boolean result = player.getResponseRequester().putRequest(900686, responseHandler);
				if(result)
				{
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(900686, 0, String.valueOf(price)));
				}
			}
			else
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300430));
	}

	/**
	 * 
	 * @param player
	 */
	public static void expand(Player player)
	{
		if (!validateNewSize(player.getWarehouseSize() + 1))
			return;
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300433, "8")); // 8 Slots added
		player.setWarehouseSize(player.getWarehouseSize() + 1);
		
		sendWarehouseInfo(player, false);
	}

	/**
	 * Checks if new player cube is not max
	 * 
	 * @param level
	 * @return true or false
	 */
	private static boolean validateNewSize(int level)
	{
		// check min and max level
		if(level < MIN_EXPAND || level > MAX_EXPAND)
			return false;
		return true;
	}

	/**
	 * Checks if npc can expand level
	 * 
	 * @param clist
	 * @param level
	 * @return true or false
	 */
	private static boolean npcCanExpandLevel(WarehouseExpandTemplate clist, int level)
	{
		// check if level exists in template
		if(!clist.contains(level))
			return false;
		return true;
	}

	/**
	 * The guy who created cube template should blame himself :) One day I will rewrite them
	 * 
	 * @param template
	 * @param level
	 * @return
	 */
	private static int getPriceByLevel(WarehouseExpandTemplate clist, int level)
	{
		return clist.get(level).getPrice();
	}
	
	
	/**
	 * Sends correctly warehouse packets
	 * 
	 * @param player
	 */
	public static void sendWarehouseInfo(Player player, boolean sendAccountWh)
	{		
		List<Item> items = player.getStorage(StorageType.REGULAR_WAREHOUSE.getId()).getStorageItems();
		
		int whSize = player.getWarehouseSize();
		int itemsSize = items.size();

		/**
		 * Regular warehouse
		 */
		boolean firstPacket = true;
		if(itemsSize != 0)
		{
			int index = 0;
			
			while(index + 10 < itemsSize)
			{
				PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(items.subList(index, index + 10),
					StorageType.REGULAR_WAREHOUSE.getId(), whSize, firstPacket));
				index += 10;
				firstPacket = false;
			}
			PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(items.subList(index, itemsSize),
				StorageType.REGULAR_WAREHOUSE.getId(), whSize, firstPacket));
		}

		PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(null, StorageType.REGULAR_WAREHOUSE
			.getId(), whSize, false)); 
		
		if(sendAccountWh)
		{
			/**
			 * Account warehouse
			 */
			PacketSendUtility
				.sendPacket(player, new SM_WAREHOUSE_INFO(player.getStorage(
					StorageType.ACCOUNT_WAREHOUSE.getId()).getAllItems(),
					StorageType.ACCOUNT_WAREHOUSE.getId(), 0, true));
		}

		PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(null, StorageType.ACCOUNT_WAREHOUSE
			.getId(), 0, false));
	}
}