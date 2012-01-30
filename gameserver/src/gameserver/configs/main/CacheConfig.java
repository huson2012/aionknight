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
import gameserver.model.account.Account;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;

public class CacheConfig
{
	/**
	 * Says whether cache for such things like PlayerCommonData or Appereance etc is cached in {@link WeakCacheMap} or
	 * in {@link SoftCacheMap}
	 */
	@Property(key = "gameserver.cache.softcache", defaultValue = "false")
	public static boolean SOFT_CACHE_MAP = false;

	/**
	 * If true then whole {@link Player} objects are cached as long as there is memory for them
	 */
	@Property(key = "gameserver.cache.players", defaultValue = "false")
	public static boolean CACHE_PLAYERS	= false;

	/**
	 * If true then whole {@link PlayerCommonData} objects are cached as long as there is memory for them
	 */
	@Property(key = "gameserver.cache.pcd", defaultValue = "false")
	public static boolean CACHE_COMMONDATA = false;

	/**
	 * If true then whole {@link Account} objects are cached as long as there is memory for them
	 */
	@Property(key = "gameserver.cache.accounts", defaultValue = "false")
	public static boolean CACHE_ACCOUNTS = false;
}