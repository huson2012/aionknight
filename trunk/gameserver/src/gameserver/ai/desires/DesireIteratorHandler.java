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

package gameserver.ai.desires;

import java.util.Iterator;

/**
 * This class is designed to be a helper class for desires.
 * Direct access to desire list is not allowed, however this interface can be used for iteration.
 *
 * @see gameserver.ai.desires.DesireIteratorFilter
 * @see gameserver.ai.desires.DesireQueue#iterateDesires(DesireIteratorHandler, DesireIteratorFilter[])
 */
public interface DesireIteratorHandler
{
	/**
	 * This method is called each time for every desire that is in the queue.<br>
	 * Remove of desire must be handeled by <b>iterator.remove();</b><br>
	 * <br> {@link java.util.ConcurrentModificationException} will be thrown by
	 * {@link gameserver.ai.desires.DesireQueue#iterateDesires(DesireIteratorHandler, DesireIteratorFilter[])}
	 * if any of the following methods will be called from here:
	 * <ul>
	 * <li>{@link gameserver.ai.desires.DesireQueue#addDesire(Desire)}</li>
	 * <li>{@link gameserver.ai.desires.DesireQueue#poll()}</li>
	 * <li>{@link gameserver.ai.desires.DesireQueue#removeDesire(Desire)}</li>
	 * </ul>
	 * <p/>
	 * However {@link gameserver.ai.desires.DesireQueue#clear()} can be called.
	 * 
	 * @param desire
	 *           current element of iteration
	 * @param iterator
	 *           iterator object
	 */
	public void next(Desire desire, Iterator<Desire> iterator);
}
