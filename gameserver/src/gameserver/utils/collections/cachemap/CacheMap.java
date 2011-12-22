/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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