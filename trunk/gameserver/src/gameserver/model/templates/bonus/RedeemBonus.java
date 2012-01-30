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
@XmlType(name = "RedeemBonus")
public class RedeemBonus extends SimpleCheckItemBonus
{
	static final InventoryBonusType type = InventoryBonusType.REDEEM;

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
		if(itemId == ItemId.ANGELS_EYE.value() || itemId == ItemId.DEMONS_EYE.value())
		{
			int kinahCount = (int)(1000 * Math.pow(5, (bonusLevel % 10) - 1));
			return player.getInventory().getKinahCount() >= kinahCount;
		}
		return true;
	}

	@Override
	public boolean apply(Player player, Item item)
	{
		if(checkItem == ItemId.ANGELS_EYE.value() || checkItem == ItemId.DEMONS_EYE.value())
		{
			int magic = bonusLevel % 10;
			int kinahCount = (int)(1000 * Math.pow(5, magic - 1));
			if(!player.getInventory().decreaseKinah(kinahCount))
				return false;
			
			double rndCoin = 0;
			// Give 1 broken coin or 1-6 Platinum Coins;
			// Here's an approximation according to data from
			// http://www.aionopedia.info/index.php/Angels_Eye
			if(magic == 1) // 1000 kinah (almost linear decrease)
			{
				rndCoin = getTriangularRnd(0, 0.76, 10);
				if(rndCoin >= 6.7)
					rndCoin = 0;
			}
			else if(magic == 2) // 5000 kinah (max at 2 coins, with 0 probability for 6 coins)
			{
				rndCoin = getTriangularRnd(0, 2, 6);
				if(rndCoin < 1.7 && rndCoin > 1)
					rndCoin = 2;
				else if(rndCoin > 5.5)
					rndCoin = 5;
			} 
			else if(magic == 3) // 25000 kinah (max at 4 coins, with 0 probability up to 2 coins)
			{
				rndCoin = getTriangularRnd(2.5, 4.7, 7);
				if (rndCoin < 3.2 || rndCoin >= 6.5)
					rndCoin = 4;
			}
			int coinCount = (int)rndCoin;
			int rewardId = ItemId.BROKEN_COIN.value();
			if(coinCount > 0)
			{
				rewardId = player.getCommonData().getRace() == Race.ELYOS ? 
					ItemId.PLATINUM_ELYOS.value() : ItemId.PLATINUM_ASMODIANS.value();
			}
			else
				coinCount = 1;
			return ItemService.addItems(player, Collections.singletonList(new QuestItems(rewardId, coinCount)));
		}
		else
		{
			int rewardId = player.getCommonData().getRace() == Race.ELYOS ? 
				ItemId.ANGELS_EYE.value() : ItemId.DEMONS_EYE.value();
				return ItemService.addItems(player, Collections.singletonList(new QuestItems(rewardId, Rnd.get(1, 4))));
		}
	}

	/**
	 * @param left - left bound
	 * @param right - right bound
	 * @param mode - variance mode
	 * @return returns a pseudo-random variate from a triangular distribution with a range [a,b]
	 */
	double getTriangularRnd(double left, double mode, double right) {
		double sample, point;
		point = (mode - left) / (right - left);
		sample = Rnd.get();

		if (sample <= point)
			return (Math.sqrt(sample * (right - left) * (mode - left)) + left);
		else
			return (right - Math.sqrt((1.0 - sample) * (right - left) * (right - mode)));
	}

	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
