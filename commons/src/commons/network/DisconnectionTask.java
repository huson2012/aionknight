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

package commons.network;

/**
 * Disconnection Task that will be execute on <code>DisconnectionThreadPool</code>
 * @see commons.network.DisconnectionThreadPool
 */
public class DisconnectionTask implements Runnable
{
	/**
	 * Connection that onDisconnect() method will be executed by <code>DisconnectionThreadPool</code>
	 * 
	 * @see commons.network.DisconnectionThreadPool
	 */
	private AConnection	connection;

	/**
	 * Construct <code>DisconnectionTask</code>
	 * 
	 * @param connection
	 */
	public DisconnectionTask(AConnection connection)
	{
		this.connection = connection;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		connection.onDisconnect();
	}
}