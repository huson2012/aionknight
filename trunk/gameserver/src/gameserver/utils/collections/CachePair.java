/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.utils.collections;

/**
 * @author rolandas
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CachePair<K extends Comparable, V> implements Comparable<CachePair> 
{
	public CachePair(K key, V value) 
	{
		this.key = key;
		this.value = value;
	}

	public K key;
	public V value;

	public boolean equals(Object obj) 
	{
		if (obj instanceof CachePair) 
		{
			CachePair p = (CachePair)obj;
			return key.equals(p.key) && value.equals(p.value);
		}
		return false;
	}

	public int compareTo(CachePair p) 
	{
		int v = key.compareTo(p.key);
		if (v == 0 && p.value instanceof Comparable) 
			return ((Comparable)value).compareTo(p.value);
		return v;
	}

	@Override
	public int hashCode() 
	{
		return key.hashCode() ^ value.hashCode();
	}

	@Override
	public String toString() 
	{
		return key + ": " + value;
	}
}
