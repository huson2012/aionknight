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

package gameserver.configs.main;

import commons.configuration.Property;

public class DropConfig
{
	@Property(key = "gameserver.drop.chance.formula", defaultValue = "0")
	public static int FORMULA_TYPE;

	@Property(key = "gameserver.drop.player.history", defaultValue = "20")
	public static int NPC_DROP_HISTORY_COUNT;

	@Property(key = "gameserver.drop.history.expire", defaultValue = "15")
	public static int NPC_DROP_EXPIRE_MINUTES;

	@Property(key = "gameserver.disable.drop.reduction", defaultValue = "false")
	public static boolean DISABLE_DROP_REDUCTION;

	@Property(key = "gameserver.world.drop.common", defaultValue = "0.01")
	public static float WORLD_DROP_CHANCE_COMMON;

	@Property(key = "gameserver.world.drop.rare", defaultValue = "0.005")
	public static float WORLD_DROP_CHANCE_RARE;

	@Property(key = "gameserver.world.drop.legendary", defaultValue = "0.003")
	public static float	WORLD_DROP_CHANCE_LEGENDARY;

	@Property(key = "gameserver.world.drop.unique", defaultValue = "0.003")
	public static float	WORLD_DROP_CHANCE_UNIQUE;

	@Property(key = "gameserver.dropquantity.restriction.enabled", defaultValue = "false")
	public static boolean DROPQUANTITY_RESTRICTION_ENABLED;

	@Property(key = "gameserver.dropquantity.restriction.blue", defaultValue = "3")
	public static int DROPQUANTITY_RESTRICTION_BLUE;

	@Property(key = "gameserver.dropquantity.restriction.gold", defaultValue = "2")
	public static int DROPQUANTITY_RESTRICTION_GOLD;

	@Property(key = "gameserver.dropquantity.restriction.orange", defaultValue = "1")
	public static int DROPQUANTITY_RESTRICTION_ORANGE;

	@Property(key = "gameserver.itemcategory.restriction.enabled", defaultValue = "false")
	public static boolean ITEMCATEGORY_RESTRICTION_ENABLED;
}