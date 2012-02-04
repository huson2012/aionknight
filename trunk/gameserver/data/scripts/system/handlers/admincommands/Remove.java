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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Remove extends AdminCommand
{
	public Remove()
	{
		super("remove");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_REMOVE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //remove <player name> <item id> <quantity>");
			return;
		}

		int itemId = 0;
		long itemCount = 1;
		Player target = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "Could not find an online player with that name.");
			return;
		}

		try
		{
			itemId = Integer.parseInt(params[1]);
			if ( params.length == 3 )
			{
				itemCount = Long.parseLong(params[2]);
			}
		}
		
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "Parameter needs to be an integer.");
			return;
		}

		Storage bag = target.getInventory();

		long itemsInBag = bag.getItemCountByItemId(itemId);
		if (itemsInBag == 0)
		{
			//Kinah cannot be removed from player's inventory using this command, bug?
			PacketSendUtility.sendMessage(admin, "Items with that id are not found in that player's inventory.");
			return;
		}

		Item item = bag.getFirstItemByItemId(itemId);
		if (itemsInBag <= itemCount)
		{
			if(bag.removeFromBag(item, true))
				PacketSendUtility.sendPacket(target,new SM_DELETE_ITEM(item.getObjectId()));
		}
		else
		{
			bag.removeFromBagByObjectId(item.getObjectId(), itemCount);
			PacketSendUtility.sendPacket(target,new SM_UPDATE_ITEM(item));
		}
		PacketSendUtility.sendMessage(admin, "Item(s) successfully removed from player " + target.getName());
		PacketSendUtility.sendMessage(target, "Admin " + admin.getName() + " removed an item from you");
	}
}