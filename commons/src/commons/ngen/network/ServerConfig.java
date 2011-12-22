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

package commons.ngen.network;

public class ServerConfig
{
	public String name;
	public String hostname;
	public int port;
	public ConnectionFactory factory;
	public int readThreads;
	public int writeThreads;
	public boolean	enableWorkers;
	public int workerThreads;	
	public int bufferCount;	
	public int readTries;	
	public int writeTries;	
	public boolean debugEnabled;	
	public ServerConfig (String name, String hostname, int port, ConnectionFactory factory, int readThreads, int writeThreads, boolean enableWorkers, int workerThreads, int bufferCount, int readTries, int writeTries, boolean debugEnabled)
	{
		this.name = name;
		this.hostname = hostname;
		this.port = port;
		this.factory = factory;
		this.readThreads = readThreads;
		this.writeThreads = writeThreads;
		this.enableWorkers = enableWorkers;
		this.workerThreads = workerThreads;
		this.bufferCount = bufferCount;
		this.readTries = readTries;
		this.writeTries = writeTries;
		this.debugEnabled = debugEnabled;
	}
}