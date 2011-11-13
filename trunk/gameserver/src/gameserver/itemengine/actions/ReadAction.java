/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.itemengine.actions;

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerMotionDAO;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_MOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class ReadAction extends AbstractItemAction
{

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if(parentItem == null)
			return false;
		
		ItemTemplate template = parentItem.getItemTemplate();
		int questId = template.getItemQuestId();
		ItemActions actions = template.getActions();
		
		if(questId == 0 || actions == null)
			return false;
		
		for(AbstractItemAction action : actions.getItemActions())
			if(action instanceof ReadAction)
				return true;

		return false;
	}

	@Override
	public void act(final Player player, Item parentItem, Item targetItem)
	{
		final int itemObjId = parentItem.getObjectId();
		final int id = parentItem.getItemTemplate().getTemplateId();
		
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 50, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
			}
		}, 50);
		
		if (parentItem.getItemId() == 188508001 || parentItem.getItemId() == 188508002)
		{
			PacketSendUtility.sendPacket(player,SM_SYSTEM_MESSAGE.STR_MSG_GET_CASH_CUSTOMIZE_MOTION(new DescriptionId(ItemService.getItemTemplate(parentItem.getItemId()).getNameId())));
			Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());
			if (player.getInventory().removeFromBag(item, true))
				PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));
			if (player.getLearnNinja() != 1 && player.getLearnHober() != 1)
				DAOManager.getDAO(PlayerMotionDAO.class).insertPlayerMotion(player.getObjectId());
			if (parentItem.getItemId() == 188508001)
			{
				DAOManager.getDAO(PlayerMotionDAO.class).updatePlayerMotion(1, player.getLearnHober(), player);
				player.setLearnNinja(1);
			}
			else if (parentItem.getItemId() == 188508002)
			{
				DAOManager.getDAO(PlayerMotionDAO.class).updatePlayerMotion(player.getLearnNinja(), 1, player);
				player.setLearnHober(1);
			}

			PacketSendUtility.sendPacket(player, new SM_MOTION(player, true));
		}
	}
}