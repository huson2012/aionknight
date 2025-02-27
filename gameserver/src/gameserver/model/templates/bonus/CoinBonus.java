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
import gameserver.dataholders.DataManager;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ArmorType;
import gameserver.model.templates.item.ItemRace;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.item.WeaponType;
import gameserver.model.templates.quest.QuestItems;
import gameserver.services.ItemService;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoinBonus")
public class CoinBonus extends SimpleCheckItemBonus
{
	final static InventoryBonusType type = InventoryBonusType.COIN;

	/** (non-Javadoc)
	 * @see gameserver.itemengine.bonus.AbstractInventoryBonus#apply(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public boolean apply(Player player, Item item)
	{
		List<Integer> itemIds =
			DataManager.ITEM_DATA.getBonusItems(type, bonusLevel, bonusLevel + 1);
		if(itemIds.isEmpty())
			return true;
		List<Integer> finalIds = new ArrayList<Integer>();
		PlayerClass pcCurrent = player.getPlayerClass();
		PlayerClass pcStarting = null;
		boolean isStartClass = false;
		try 
		{
			pcStarting = PlayerClass.getStartingClassFor(pcCurrent);
		} 
		catch (IllegalArgumentException e)
		{ 
			pcStarting = pcCurrent;
			isStartClass = true;
		}

		// CHECK them if they are retail-like !!!
		// Although coin rewards are for warrior and mage classes only (%Quest_L_coin_[m|w]),
		// spending the coins on not needed stuff looks weird

		for (Integer itemId : itemIds) {
			ItemTemplate template = ItemService.getItemTemplate(itemId);

			ItemRace itemRace = template.getOriginRace();
			if(String.valueOf(itemRace) != String.valueOf(player.getCommonData().getRace()) &&
				itemRace != ItemRace.ALL)
				continue;

			boolean added = false;

			WeaponType weaponType = template.getWeaponType();
			if(weaponType != null)
			{
				switch (weaponType) {
					case BOOK_2H:
					case ORB_2H:
						if (pcStarting == PlayerClass.MAGE)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case BOW:
					case DAGGER_1H:
					case SWORD_1H:
						if (pcStarting == PlayerClass.WARRIOR || 
							pcStarting == PlayerClass.SCOUT)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case MACE_1H:
						if (pcStarting == PlayerClass.WARRIOR || 
							pcStarting == PlayerClass.PRIEST)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case SWORD_2H:
						if (pcStarting == PlayerClass.WARRIOR)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case POLEARM_2H:
						if (pcCurrent == PlayerClass.GLADIATOR || 
							isStartClass && pcStarting == PlayerClass.WARRIOR)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case STAFF_2H:
						if (pcStarting == PlayerClass.PRIEST)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
				}
				if (added)
					continue;
			}

			ArmorType armorType = template.getArmorType();
			if(armorType != null)
			{
				switch (template.getArmorType()) {
					case CLOTHES:
					case ROBE:
						if (pcStarting == PlayerClass.MAGE)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case CHAIN:
					case SHIELD:
						if (pcStarting == PlayerClass.WARRIOR || 
							pcStarting == PlayerClass.PRIEST)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case LEATHER:
						if (pcStarting == PlayerClass.SCOUT || 
							pcStarting == PlayerClass.PRIEST)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case PLATE:
						if (pcStarting == PlayerClass.WARRIOR)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
					case ARROW:
						if (pcStarting == PlayerClass.SCOUT)
						{
							added = true;
							finalIds.add(itemId);
						}
						break;
				}
				if (added)
					continue;
			}

			// the rest are for all classes
			if(weaponType == null && armorType == null)
				finalIds.add(itemId);
		}

		int itemId = finalIds.get(Rnd.get(finalIds.size()));
		return ItemService.addItems(player, Collections.singletonList(new QuestItems(itemId, 1)));
	}

	/** (non-Javadoc)
	 * @see gameserver.model.templates.bonus.AbstractInventoryBonus#getType()
	 */
	@Override
	public InventoryBonusType getType()
	{
		return type;
	}

}
