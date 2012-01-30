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
import java.lang.ref.WeakReference;

/**
 * This class is a simple map implementation for cache usage.<br>
 * <br>
 * Values from the map will be removed after the first garbage collector run if there isn't any strong reference to the
 * value object.
 */
class WeakCacheMap<K, V> extends AbstractCacheMap<K, V> implements CacheMap<K, V>
{
	private static final Logger	log	= Logger.getLogger(WeakCacheMap.class);

	/**
	 * This class is a {@link WeakReference} with additional responsibility of holding key object
	 */
	private class Entry extends WeakReference<V>
	{
		private K	key;

		Entry(K key, V referent, ReferenceQueue<? super V> q)
		{
			super(referent, q);
			this.key = key;
		}

		K getKey()
		{
			return key;
		}
	}

	WeakCacheMap(String cacheName, String valueName)
	{
		super(cacheName, valueName, log);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void cleanQueue()
	{
		Entry en = null;
		while((en = (Entry) refQueue.poll()) != null)
		{
			K key = en.getKey();
			if(log.isDebugEnabled())
				log.debug(cacheName + " : cleaned up " + valueName + " for key: " + key);
			cacheMap.remove(key);
		}
	}

	@Override
	protected Reference<V> newReference(K key, V value, ReferenceQueue<V> vReferenceQueue)
	{
		return new Entry(key, value, vReferenceQueue);
	}
}
