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
import java.lang.ref.SoftReference;

/**
 * This class is a simple map implementation for cache usage.<br>
 * <br>
 * Value may be stored in map really long, but it for sure will be removed if there is low memory (and of course there
 * isn't any strong reference to value object)
 */
class SoftCacheMap<K, V> extends AbstractCacheMap<K, V> implements CacheMap<K, V>
{
	private static final Logger	log	= Logger.getLogger(SoftCacheMap.class);

	/**
	 * This class is a {@link SoftReference} with additional responsibility of holding key object
	 */
	private class SoftEntry extends SoftReference<V>
	{
		private K	key;

		SoftEntry(K key, V referent, ReferenceQueue<? super V> q)
		{
			super(referent, q);
			this.key = key;
		}

		K getKey()
		{
			return key;
		}
	}

	SoftCacheMap(String cacheName, String valueName)
	{
		super(cacheName, valueName, log);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void cleanQueue()
	{
		SoftEntry en = null;
		while((en = (SoftEntry) refQueue.poll()) != null)
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
		return new SoftEntry(key, value, vReferenceQueue);
	}
}