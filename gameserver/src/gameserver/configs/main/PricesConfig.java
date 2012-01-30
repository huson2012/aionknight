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

public class PricesConfig
{
	@Property(key = "gameserver.prices.default.prices", defaultValue = "100")
	public static int DEFAULT_PRICES;
	
	@Property(key = "gameserver.prices.default.modifier", defaultValue = "100")
	public static int DEFAULT_MODIFIER;
	
	@Property(key = "gameserver.prices.default.taxes", defaultValue = "100")
	public static int DEFAULT_TAXES;
	
	@Property(key = "gameserver.prices.vendor.buymod", defaultValue = "100")
	public static int VENDOR_BUY_MODIFIER;
	
	@Property(key = "gameserver.prices.vendor.sellmod", defaultValue = "20")
	public static int VENDOR_SELL_MODIFIER;	
}