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
