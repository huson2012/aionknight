/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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