/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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

package gameserver.configs.main;

import commons.configuration.Property;

public class DropConfig
{
	@Property(key = "gameserver.drop.chance.formula", defaultValue = "0")
	public static int		FORMULA_TYPE;

	@Property(key = "gameserver.drop.player.history", defaultValue = "20")
	public static int		NPC_DROP_HISTORY_COUNT;

	@Property(key = "gameserver.drop.history.expire", defaultValue = "15")
	public static int		NPC_DROP_EXPIRE_MINUTES;

	@Property(key = "gameserver.disable.drop.reduction", defaultValue = "false")
	public static boolean		DISABLE_DROP_REDUCTION;

	@Property(key = "gameserver.world.drop.common", defaultValue = "0.01")
	public static float		WORLD_DROP_CHANCE_COMMON;

	@Property(key = "gameserver.world.drop.rare", defaultValue = "0.005")
	public static float		WORLD_DROP_CHANCE_RARE;

	@Property(key = "gameserver.world.drop.legendary", defaultValue = "0.003")
	public static float		WORLD_DROP_CHANCE_LEGENDARY;

	@Property(key = "gameserver.world.drop.unique", defaultValue = "0.003")
	public static float		WORLD_DROP_CHANCE_UNIQUE;

	@Property(key = "gameserver.dropquantity.restriction.enabled", defaultValue = "false")
	public static boolean	DROPQUANTITY_RESTRICTION_ENABLED;

	@Property(key = "gameserver.dropquantity.restriction.blue", defaultValue = "3")
	public static int		DROPQUANTITY_RESTRICTION_BLUE;

	@Property(key = "gameserver.dropquantity.restriction.gold", defaultValue = "2")
	public static int		DROPQUANTITY_RESTRICTION_GOLD;

	@Property(key = "gameserver.dropquantity.restriction.orange", defaultValue = "1")
	public static int		DROPQUANTITY_RESTRICTION_ORANGE;

	@Property(key = "gameserver.itemcategory.restriction.enabled", defaultValue = "false")
	public static boolean	ITEMCATEGORY_RESTRICTION_ENABLED;
}