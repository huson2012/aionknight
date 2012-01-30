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

package gameserver.network.aion.clientpackets;

import gameserver.itemengine.actions.EnchantItemAction;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemCategory;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class CM_MANASTONE extends AionClientPacket
{
	
	private int npcObjId;
	private int slotNum;
	
	private int actionType;
	private int targetFusedSlot;
	private int stoneUniqueId;
	private int targetItemUniqueId;
	private int supplementUniqueId;
	private ItemCategory actionCategory;
	
	/**
	 * @param opcode
	 */
	public CM_MANASTONE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		actionType = readC();
		targetFusedSlot = readC();
		//This is specifing which part of the weapon to socket with fused weapons, 
		//1 is primary weapon 2 is fused weapon. System needs to be rewritten accordingly. 
		targetItemUniqueId = readD();
		switch(actionType)
		{
			case 1:
			case 2:
				stoneUniqueId = readD();
				supplementUniqueId = readD();
				break;
			case 3:
				slotNum = readC();
				readC();
				readH();
				npcObjId = readD();
				break;
		}
	}

	@Override
	protected void runImpl()
	{
		AionObject npc = World.getInstance().findAionObject(npcObjId);
		Player player = getConnection().getActivePlayer();
		
		switch(actionType)
		{
			case 1: //enchant stone
			case 2: //add manastone
				EnchantItemAction action = new EnchantItemAction();
				Item manastone = player.getInventory().getItemByObjId(stoneUniqueId);
				Item targetItem = player.getInventory().getItemByObjId(targetItemUniqueId);
				if(targetItem == null)
				{
					targetItem = player.getEquipment().getEquippedItemByObjId(targetItemUniqueId);
				}
				
				if(actionType == 1)
					actionCategory = ItemCategory.ENCHANTSTONE;
				else
					actionCategory = ItemCategory.MAGICSTONE;
				
				if(manastone != null && manastone.getItemTemplate().getItemCategory() != actionCategory)
					return;
				
				if(manastone != null && targetItem != null && action.canAct(player, manastone, targetItem, targetFusedSlot))
				{
					Item supplement = player.getInventory().getItemByObjId(supplementUniqueId);
					action.act(player, manastone, targetItem, supplement, targetFusedSlot);
				}
				break;
			case 3: // remove manastone
				long price = player.getPrices().getPriceForService(500, player.getCommonData().getRace());
				if (player.getInventory().getKinahItem().getItemCount() < price)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(price));
					return;
				}
				if(npc != null)
				{
					if(!player.getInventory().decreaseKinah(price))
						return;
					if(targetFusedSlot == 1)
						ItemService.removeManastone(player, targetItemUniqueId, slotNum);
					else 
						ItemService.removeFusionstone(player, targetItemUniqueId, slotNum);
				}
		}
	}
}