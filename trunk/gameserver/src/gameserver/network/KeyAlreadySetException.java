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

package gameserver.network;

@SuppressWarnings("serial")
public class KeyAlreadySetException extends RuntimeException
{
	/**
	 * Constructs an <code>KeyAlreadySetException</code> with no detail message.
	 */
	public KeyAlreadySetException()
	{
		super();
	}

	/**
	 * Constructs an <code>KeyAlreadySetException</code> with the specified detail message.
	 * 
	 * @param s
	 *           the detail message.
	 */
	public KeyAlreadySetException(String s)
	{
		super(s);
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *           exception description
	 * @param cause
	 *           reason of this exception
	 */
	public KeyAlreadySetException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates new error
	 * 
	 * @param cause
	 *           reason of this exception
	 */
	public KeyAlreadySetException(Throwable cause)
	{
		super(cause);
	}
}