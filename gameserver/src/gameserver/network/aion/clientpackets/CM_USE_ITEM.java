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

import gameserver.dataholders.DataManager;
import gameserver.itemengine.actions.AbstractItemAction;
import gameserver.itemengine.actions.ItemActions;
import gameserver.itemengine.actions.UnwrapAction;
import gameserver.model.DescriptionId;
import gameserver.model.Race;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.WrapperItem;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.item.LevelRestrict;
import gameserver.model.templates.item.LevelRestrictType;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.quest.HandlerResult;
import gameserver.quest.QuestEngine;
import gameserver.quest.model.QuestCookie;
import gameserver.restrictions.RestrictionsManager;
import gameserver.utils.PacketSendUtility;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public class CM_USE_ITEM extends AionClientPacket {

	public int uniqueItemId;
	public int type, targetItemId;

	private static final Logger log = Logger.getLogger(CM_USE_ITEM.class);

	public CM_USE_ITEM(int opcode) {
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		uniqueItemId = readD();
		type = readC();
		if (type == 2)
		{
			targetItemId = readD();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() 
	{
		Player player = getConnection().getActivePlayer();
		Item item = player.getInventory().getItemByObjId(uniqueItemId);
		
		if(item == null)
		{
			log.warn(String.format("CHECKPOINT: null item use action: %d %d", player.getObjectId(), uniqueItemId));
			return;
		}
		
		if(!RestrictionsManager.canUseItem(player))
			return;
		
		//check item race
		ItemTemplate template = item.getItemTemplate();
		switch(template.getRace())
		{
			case ASMODIANS:
				if(player.getCommonData().getRace() != Race.ASMODIANS)
					return;
				break;
			case ELYOS:
				if(player.getCommonData().getRace() != Race.ELYOS)
					return;
				break;
		}	
		
		//check class restrict
		if (!template.checkClassRestrict(player.getCommonData().getPlayerClass()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_INVALID_CLASS());
			return;
		}
			
		//check level restrict
		LevelRestrict restrict = template.getRectrict(player.getCommonData().getPlayerClass(), player.getLevel());
		LevelRestrictType restrictType = restrict.getType();
		if (restrictType == LevelRestrictType.LOW)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(
				restrict.getLevel(), new DescriptionId(Integer.parseInt(item.getName()))));
			return;
		}
		else if (restrictType == LevelRestrictType.HIGH)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_HIGH_LEVEL(
				restrict.getLevel(), new DescriptionId(Integer.parseInt(item.getName()))));
			return;
		}
			
		HandlerResult hResult = QuestEngine.getInstance().onItemUseEvent(new QuestCookie(null, player, template.getItemQuestId(), 0), item);
		if (hResult == HandlerResult.FAILED)
			return;
		
		//check use item multicast delay exploit cast (spam)
		if(player.isCasting())
		{
			//PacketSendUtility.sendMessage(this.getOwner(), "You must wait until cast time finished to use skill again.");
			return;
		}
		
		Item targetItem = player.getInventory().getItemByObjId(targetItemId);
		if(targetItem == null)
			targetItem = player.getEquipment().getEquippedItemByObjId(targetItemId);

		ItemActions itemActions = item.getItemTemplate().getActions();
		ArrayList<AbstractItemAction> actions = new ArrayList<AbstractItemAction>();
		
		ItemRace playerRace = player.getCommonData().getRace() == Race.ASMODIANS ?
			ItemRace.ASMODIANS : ItemRace.ELYOS;
		WrapperItem wrapper = DataManager.WRAPPED_ITEM_DATA.getItemWrapper(item.getItemId());
		if (wrapper != null && wrapper.hasAnyItems(playerRace))
		{
			if (itemActions == null)
				itemActions = new ItemActions();
			itemActions.getItemActions().add(new UnwrapAction());
		}
		
		if (itemActions == null)
			return;
		
		for (AbstractItemAction itemAction : itemActions.getItemActions())
		{
			// check if the item can be used before placing it on the cooldown list.
			if (itemAction.canAct(player, item, targetItem))
				actions.add(itemAction);
		}
		
		
		if(actions.isEmpty())
			return;
		
		// Store Item CD in server Player variable.
		// Prevents potion spamming, and relogging to use kisks/aether jelly/long CD items.
		if (player.isItemUseDisabled(item.getItemTemplate().getDelayId()))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANT_USE_UNTIL_DELAY_TIME);
			return;
		}
		
		int useDelay = item.getItemTemplate().getDelayTime();
		player.addItemCoolDown(item.getItemTemplate().getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);

		for (AbstractItemAction itemAction : actions)
		{
			itemAction.act(player, item, targetItem);
		}
	}
}
