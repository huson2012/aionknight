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

package gameserver.utils.collections.cachemap;

import gameserver.configs.main.CacheConfig;

public class CacheMapFactory
{

	/**
	 * Returns new instance of either {@link WeakCacheMap} or {@link SoftCacheMap} depending on
	 * {@link CacheConfig#SOFT_CACHE_MAP} setting.
	 * 
	 * @param <K> - Type of keys
	 * @param <V> - Type of values
	 * 
	 * @param cacheName - The name for this cache map
	 * @param valueName - Mnemonic name for values stored in the cache
	 * @return CacheMap<K, V>
	 */
	public static <K, V> CacheMap<K, V> createCacheMap(String cacheName, String valueName)
	{
		if(CacheConfig.SOFT_CACHE_MAP)
			return createSoftCacheMap(cacheName, valueName);
		else
			return createWeakCacheMap(cacheName, valueName);
	}

	/**
	 * Creates and returns an instance of {@link SoftCacheMap}
	 * 
	 * @param <K> - Type of keys
	 * @param <V> - Type of values
	 * 
	 * @param cacheName - The name for this cache map
	 * @param valueName - Mnemonic name for values stored in the cache
	 * @return CacheMap<K, V>
	 */
	public static <K, V> CacheMap<K, V> createSoftCacheMap(String cacheName, String valueName)
	{
		return new SoftCacheMap<K,V>(cacheName, valueName);
	}
	
	/**
	 * Creates and returns an instance of {@link WeakCacheMap}
	 * 
	 * @param <K> - Type of keys
	 * @param <V> - Type of values
	 * 
	 * @param cacheName - The name for this cache map
	 * @param valueName - Mnemonic name for values stored in the cache
	 * @return CacheMap<K, V>
	 */
	public static <K, V> CacheMap<K, V> createWeakCacheMap(String cacheName, String valueName)
	{
		return new WeakCacheMap<K,V>(cacheName, valueName);
	}
}
