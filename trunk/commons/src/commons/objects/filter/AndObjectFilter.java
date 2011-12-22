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

package commons.objects.filter;

/**
 * This filter is used to combine a few ObjectFilters into one. Its acceptObject method returns true only if all
 * filters, that were passed through constructor return true
 * @param <T>
 */
public class AndObjectFilter<T> implements ObjectFilter<T>
{
	/** All filters that are used when running acceptObject() method */
	private ObjectFilter<? super T>[]	filters;

	/**
	 * Constructs new <tt>AndObjectFilter</tt> object, that uses given filters.
	 * 
	 * @param filters
	 */
	public AndObjectFilter(ObjectFilter<? super T>... filters)
	{
		this.filters = filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptObject(T object)
	{
		for(ObjectFilter<? super T> filter : filters)
		{
			if(filter != null && !filter.acceptObject(object))
				return false;
		}
		return true;
	}
}