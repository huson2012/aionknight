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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusTemplate")
public class BonusTemplate
{
	@XmlElements({
		@XmlElement(name = "boss", type = BossBonus.class),
		@XmlElement(name = "coin", type = CoinBonus.class),
		@XmlElement(name = "enchant", type = EnchantBonus.class),
		@XmlElement(name = "food", type = FoodBonus.class),
		@XmlElement(name = "fortress", type = FortressBonus.class),
		@XmlElement(name = "goods", type = GoodsBonus.class),
		@XmlElement(name = "island", type = IslandBonus.class),
		@XmlElement(name = "magical", type = MagicalBonus.class),
		@XmlElement(name = "manastone", type = ManastoneBonus.class),
		@XmlElement(name = "master_recipe", type = MasterRecipeBonus.class),
		@XmlElement(name = "material", type = MaterialBonus.class),
		@XmlElement(name = "medal", type = MedalBonus.class),
		@XmlElement(name = "medicine", type = MedicineBonus.class),
		@XmlElement(name = "movie", type = CutSceneBonus.class),
		@XmlElement(name = "recipe", type = RecipeBonus.class),
		@XmlElement(name = "redeem", type = RedeemBonus.class),
		@XmlElement(name = "task", type = WorkOrderBonus.class),
		@XmlElement(name = "lunar", type = LunarEventBonus.class)
	})
	protected List<AbstractInventoryBonus> itemBonuses;

	@XmlAttribute()
	private int questId;
	
	
	/**
	 * Gets the value of the itemBonuses property.
	 * 
	 */
	public List<AbstractInventoryBonus> getItemBonuses() {
		if (itemBonuses == null) {
			itemBonuses = new ArrayList<AbstractInventoryBonus>();
		}
		return this.itemBonuses;
	}
	
	public int getQuestId() {
		return questId;
	}
}
