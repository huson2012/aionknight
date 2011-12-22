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