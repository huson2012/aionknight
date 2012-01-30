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

import org.apache.log4j.Logger;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for {@link WeakCacheMap} and {@link SoftCacheMap}
 *
 * @param <K>
 * @param <V>
 */
abstract class AbstractCacheMap<K, V> implements CacheMap<K, V>
{
	private final Logger					log;

	protected final String					cacheName;
	protected final String					valueName;

	/** Map storing references to cached objects */
	protected final Map<K, Reference<V>>	cacheMap	= new HashMap<K, Reference<V>>();

	protected final ReferenceQueue<V>		refQueue	= new ReferenceQueue<V>();

	/**
	 * @param cacheName
	 * @param valueName
	 */
	AbstractCacheMap(String cacheName, String valueName, Logger log)
	{
		this.cacheName = "#CACHE  [" + cacheName + "]#  ";
		this.valueName = valueName;
		this.log = log;
	}

	/** {@inheritDoc} */
	@Override
	public void put(K key, V value)
	{
		cleanQueue();

		if(cacheMap.containsKey(key))
			throw new IllegalArgumentException("Key: " + key + " already exists in map");

		Reference<V> entry = newReference(key, value, refQueue);

		cacheMap.put(key, entry);

		if(log.isDebugEnabled())
			log.debug(cacheName + " : added " + valueName + " for key: " + key);
	}

	/** {@inheritDoc} */
	@Override
	public V get(K key)
	{
		cleanQueue();

		Reference<V> reference = cacheMap.get(key);

		if(reference == null)
			return null;

		V res = reference.get();

		if(res != null && log.isDebugEnabled())
			log.debug(cacheName + " : obtained " + valueName + " for key: " + key);

		return res;
	}

	@Override
	public boolean contains(K key)
	{
		cleanQueue();
		return cacheMap.containsKey(key);
	}

	protected abstract void cleanQueue();

	@Override
	
	public void remove(K key)
	{
		cacheMap.remove(key);
	}
	
	protected abstract Reference<V> newReference(K key, V value, ReferenceQueue<V> queue);
}
