/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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