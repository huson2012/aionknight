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

/**
 * This interface represents a Map structure for cache usage. 
 */
public interface CacheMap<K, V>
{

	/**
	 * Adds a pair <key,value> to cache map.<br>
	 * <br>
	 * 
	 * <font color='red'><b>NOTICE:</b> </font> if there is already a value with given id in the map,
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value);

	/**
	 * Returns cached value correlated to given key.
	 * 
	 * @param key
	 * @return V
	 */
	public V get(K key);
	
	/**
	 * Checks whether this map contains a value related to given key.
	 * @param key
	 * @return true or false
	 */
	public boolean contains(K key);
	
	/**
	 * Removes an entry from the map, that has given key.
	 * @param key
	 */
	public void remove(K key);
}