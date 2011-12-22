/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
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

package commons.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorIterator<V> implements Iterator<V>
{
	private Iterator<? extends Iterable<V>>	firstLevelIterator;
	private Iterator<V>						secondLevelIterator;

	public IteratorIterator(Iterable<? extends Iterable<V>> itit)
	{
		this.firstLevelIterator = itit.iterator();
	}

	@Override
	public boolean hasNext()
	{
		if(secondLevelIterator != null && secondLevelIterator.hasNext())
			return true;

		while(firstLevelIterator.hasNext())
		{
			Iterable<V> iterable = firstLevelIterator.next();

			if(iterable != null)
			{
				secondLevelIterator = iterable.iterator();

				if(secondLevelIterator.hasNext())
					return true;
			}
		}
		return false;
	}

	@Override
	public V next()
	{
		if(secondLevelIterator == null || !secondLevelIterator.hasNext())
			throw new NoSuchElementException();
		return secondLevelIterator.next();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("This operation is not supported.");
	}
}