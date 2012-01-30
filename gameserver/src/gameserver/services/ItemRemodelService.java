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