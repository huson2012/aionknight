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

package gameserver.model.templates.bonus;

import commons.utils.Rnd;
import gameserver.model.Race;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Storage;
import gameserver.model.items.ItemId;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.Collections;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedalBonus")
public class MedalBonus extends SimpleCheckItemBonus
{

	static final InventoryBonusType type = InventoryBonusType.MEDAL;
	
	@Override
	public boolean canApply(Player player, int itemId, int questId)
	{
		if(!super.canApply(player, itemId, questId))
			return false;

		Storage storage = player.getInventory();
		if(storage.getItemCountByItemId(checkItem) < count)
			return false;
		else if(storage.isFull())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.MSG_FULL_INVENTORY);
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean apply(Player player, Item item)
	{
		int questId = player.getQuestCookie().getQuestId();
		int itemId;
		int count = 1;
		if (Rnd.get(1000) / 10f < 41.8)
		{
			if (Rnd.get(1000) / 10f < 48.4)
			{
				count = 2;
				itemId = checkItem == ItemId.SILVER_MEDAL.value() ? ItemId.SILVER_MEDAL.value()
																  : ItemId.GOLDEN_MEDAL.value();
			}
			else
				itemId = checkItem == ItemId.SILVER_MEDAL.value() ? ItemId.GOLDEN_MEDAL.value()
																  : ItemId.PLATINUM_MEDAL.value();
		}
		else if ((questId == 1717 || questId == 2717) && Rnd.get(100) < 4)
		{
			if (player.getCommonData().getRace() == Race.ASMODIANS)
				itemId = 182205668; // Rusted Spear
			else
				itemId = 182202156; // Quartz of Virtue
		}
		else
			itemId = ItemId.RUSTED_MEDAL.value();

		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, count)));
	}

	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
