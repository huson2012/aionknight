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

package commons.scripting.url;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import commons.scripting.ScriptClassLoader;

/**
 * This class represents URL Stream handler that accepts {@value #HANDLER_PROTOCOL} protocol
 */
public class VirtualClassURLStreamHandler extends URLStreamHandler
{
	/**
	 * Script Handler protocol for classes compiled from source
	 */
	public static final String		HANDLER_PROTOCOL	= "aescript://";

	/**
	 * Script class loader that loaded those classes
	 */
	private final ScriptClassLoader	cl;

	/**
	 * Creates new instance of url stream handler with given classloader
	 * 
	 * @param cl
	 *           ScriptClassLoaderImpl that was used to load compiled class
	 */
	public VirtualClassURLStreamHandler(ScriptClassLoader cl)
	{
		this.cl = cl;
	}

	/**
	 * Opens new URL connection for URL
	 * 
	 * @param u
	 *           url
	 * @return Opened connection
	 * @throws IOException
	 *            never thrown
	 */
	@Override
	protected URLConnection openConnection(URL u) throws IOException
	{
		return new VirtualClassURLConnection(u, cl);
	}
}
