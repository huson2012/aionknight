package ru.aionknight.gameserver.itemengine.actions;

import org.openaion.commons.database.dao.DAOManager;

import ru.aionknight.gameserver.dao.PlayerMotionDAO;
import ru.aionknight.gameserver.model.DescriptionId;
import ru.aionknight.gameserver.model.gameobjects.Item;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.item.ItemTemplate;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_MOTION;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.services.ItemService;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.ThreadPoolManager;

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
