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

import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.templates.item.ArmorType;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;

public class ItemRemodelService
{
	/**
	 * @param player
	 * @param keepItemObjId
	 * @param extractItemObjId
	 */
	public static void remodelItem (Player player, int keepItemObjId, int extractItemObjId)
	{
		Storage inventory = player.getInventory();
		Item keepItem = inventory.getItemByObjId(keepItemObjId);
		Item extractItem = inventory.getItemByObjId(extractItemObjId);
		
		long remodelCost = player.getPrices().getPriceForService(1000, player.getCommonData().getRace());
		
		if(keepItem == null || extractItem == null)
		{ // NPE check.
			return;
		}
		
		// Check Player Level
		if (player.getLevel() < 20)
		{
			
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_PC_LEVEL_LIMIT);
			return;
		}
		
		// Check Kinah
		if (player.getInventory().getKinahItem().getItemCount() < remodelCost)
		{
			PacketSendUtility.sendPacket(player,
				SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_NOT_ENOUGH_GOLD(new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		}
		
		// Check for using "Pattern Reshaper" (168100000)
		if (extractItem.getItemTemplate().getTemplateId() == 168100000)
		{
			if (keepItem.getItemTemplate() == keepItem.getItemSkinTemplate())
			{
				PacketSendUtility.sendMessage(player, "That item does not have a remodeled skin to remove.");
				return;
			}
			// Remove Money
			if(!player.getInventory().decreaseKinah(remodelCost))
				return;
			
			// Remove Pattern Reshaper
			player.getInventory().decreaseItemCount(extractItem, 1);
			
			// Revert item to ORIGINAL SKIN
			keepItem.setItemSkinTemplate(keepItem.getItemTemplate());
			
			// Remove dye color if item can not be dyed.
			if (!keepItem.getItemTemplate().isItemDyePermitted())
				keepItem.setItemColor(0);
			
			// Notify Player
			PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(keepItem));
			PacketSendUtility.sendPacket(player,
				SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_SUCCEED(new DescriptionId(keepItem.getItemTemplate().getNameId())));
			
			return;
		}
		// Check that types match.
		if(keepItem.getItemTemplate().getWeaponType() != extractItem.getItemSkinTemplate().getWeaponType()
			|| (extractItem.getItemSkinTemplate().getArmorType() != ArmorType.CLOTHES
				&& keepItem.getItemTemplate().getArmorType() != extractItem.getItemSkinTemplate().getArmorType())
			|| keepItem.getItemTemplate().getArmorType() == ArmorType.CLOTHES
			|| keepItem.getItemTemplate().getItemSlot() != extractItem.getItemSkinTemplate().getItemSlot())
		{
			PacketSendUtility.sendPacket(player,
				SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_NOT_COMPATIBLE(
					new DescriptionId(keepItem.getItemTemplate().getNameId()),
					new DescriptionId(extractItem.getItemSkinTemplate().getNameId())
			));
			return;
		}

		//check if item is remodel able
		if (!keepItem.getItemTemplate().isChangeSkinPermitted())
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300478,
				new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		}
		
		// -- SUCCESS --
		
		// Remove Money
		if(!player.getInventory().decreaseKinah(remodelCost))
			return;
		
		// Remove Item
		player.getInventory().decreaseItemCount(extractItem, 1);
		
		// REMODEL ITEM
		keepItem.setItemSkinTemplate(extractItem.getItemSkinTemplate());
		
		// Transfer Dye
		keepItem.setItemColor(extractItem.getItemColor());
		
		// Notify Player
		PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(keepItem));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300483, new DescriptionId(keepItem.getItemTemplate().getNameId())));
	}
}