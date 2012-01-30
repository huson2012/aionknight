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

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Equipment;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import gameserver.restrictions.RestrictionsManager;
import gameserver.skill.model.Effect;
import gameserver.utils.PacketSendUtility;

public class CM_EQUIP_ITEM extends AionClientPacket
{
	public int	slotRead;
	public int	itemUniqueId;
	public int	action;
	
	public CM_EQUIP_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		action = readC(); // 0/1 = equip/unequip
		slotRead = readD();
		itemUniqueId = readD();
	}

	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		if(activePlayer == null)
			return;
		Equipment equipment = activePlayer.getEquipment();
		Item resultItem = null;

		if(!RestrictionsManager.canChangeEquip(activePlayer))
			return;

		switch(action)
		{
			case 0:
				resultItem = equipment.equipItem(itemUniqueId, slotRead);
				break;
			case 1:
				resultItem = equipment.unEquipItem(itemUniqueId, slotRead);
				break;
			case 2:
				if (activePlayer.getController().hasTask(TaskId.ITEM_USE) && !activePlayer.getController().getTask(TaskId.ITEM_USE).isDone())
				{
					PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.CANT_EQUIP_ITEM_IN_ACTION());
					return;
				}
				equipment.switchHands();
				break;
		}

		if(resultItem != null || action == 2)
		{
			PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(),
				equipment.getEquippedItemsWithoutStigma()), true);
		}

		if (!equipment.isShieldEquipped())
		{
			for (Effect effect : activePlayer.getEffectController().getNoShowEffects())
			{
			   if (effect.isStance())
				   activePlayer.getEffectController().removeNoshowEffect(effect.getSkillId());
			}
		}
		
	}
}
