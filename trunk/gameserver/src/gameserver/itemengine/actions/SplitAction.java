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

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SplitAction")
public class SplitAction extends AbstractItemAction
{

	@Override
	public void act(final Player player, final Item parentItem, Item targetItem)
	{
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
			parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		player.getController().addNewTask(TaskId.ITEM_USE,
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem
					.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 1, 0));

				Storage inventory = player.getInventory();
				inventory.removeFromBagByObjectId(parentItem.getObjectId(), 1);
				int decomposedId = parentItem.getItemId() - 1;
				switch (parentItem.getItemId())
				{
					case 152000112:
					case 152000328:
						decomposedId -= 2;
						break;
					case 152000213:
					case 152000327:
						decomposedId -= 3;
						break;
					case 152000326:
						decomposedId -= 4;
				}
				ItemService.addItem(player, decomposedId, 3);
			}
		}, 5000));
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		int itemId = parentItem.getItemTemplate().getTemplateId();
        return !(itemId >= 152000329 && itemId <= 152000331);
    }
}