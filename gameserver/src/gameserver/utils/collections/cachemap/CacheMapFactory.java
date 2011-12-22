/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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