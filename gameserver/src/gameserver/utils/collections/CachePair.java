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

package gameserver.utils.collections;

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
