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
 * This class represents ServerCfg for configuring NioServer
 * @see commons.network.ConnectionFactory
 * @see commons.network.AConnection
 */
public class ServerCfg
{
	/**
	 * Host Name on wich we will listen for connections.
	 */
	public final String	hostName;
	/**
	 * Port number on wich we will listen for connections.
	 */
	public final int port;
	/**
	 * Connection Name only for logging purposes.
	 */
	public final String	connectionName;
	/**
	 * <code>ConnectionFactory</code> that will create <code>AConection</code> object<br>
	 * representing new socket connection.
	 * 
	 * @see commons.network.ConnectionFactory
	 * @see commons.network.AConnection
	 */
	public final ConnectionFactory	factory;

	/**
	 * Constructor
	 * 
	 * @param hostName
	 *           - Host Name on witch we will listen for connections.
	 * @param port
	 *           - Port number on witch we will listen for connections.
	 * @param connectionName
	 *           - only for logging purposes.
	 * @param factory
	 *           <code>ConnectionFactory</code> that will create <code>AConection</code> object
	 */
	public ServerCfg(String hostName, int port, String connectionName, ConnectionFactory factory)
	{
		this.hostName = hostName;
		this.port = port;
		this.connectionName = connectionName;
		this.factory = factory;
	}
}