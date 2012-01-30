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

package gameserver.itemengine.actions;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DyeAction")

public class DyeAction extends AbstractItemAction
{
	@XmlAttribute(name = "color")
	protected String color;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if(targetItem == null)
		{ // no item selected.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ERROR);
			return false;
		}

		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		if (targetItem.getItemTemplate().isItemDyePermitted())
		{
			if (color.equals("no"))
			{
				targetItem.setItemColor(0);
			}
			else
			{
				int rgb = Integer.parseInt(color, 16);
				int bgra = 0xFF | ((rgb & 0xFF) << 24) | ((rgb & 0xFF00) << 8) | ((rgb & 0xFF0000) >>> 8);
				targetItem.setItemColor(bgra);
			}

			// item is equipped, so need broadcast packet
			if (player.getEquipment().getEquippedItemByObjId(targetItem.getObjectId()) != null)
			{
				PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedItemsWithoutStigma()), true);
				player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
			}
			
			// item is not equipped
			else
				player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);

			PacketSendUtility.sendPacket(player, new SM_UPDATE_ITEM(targetItem));
			player.getInventory().removeFromBagByObjectId(parentItem.getObjectId(), 1);
		}
	}
}