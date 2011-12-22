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

package commons.database;

import java.io.File;
import commons.configuration.Property;

public class DatabaseConfig
{
	@Property(key = "database.url", defaultValue = "jdbc:mysql://localhost:3306/ak_server_ls")
	public static String DATABASE_URL;

	@Property(key = "database.driver", defaultValue = "com.mysql.jdbc.Driver")
	public static Class<?> DATABASE_DRIVER;

	@Property(key = "database.user", defaultValue = "root")
	public static String DATABASE_USER;

	@Property(key = "database.password", defaultValue = "root")
	public static String DATABASE_PASSWORD;

	@Property(key = "database.connections.min", defaultValue = "2")
	public static int DATABASE_CONNECTIONS_MIN;

	@Property(key = "database.connections.max", defaultValue = "10")
	public static int DATABASE_CONNECTIONS_MAX;

	@Property(key = "database.scriptcontext.descriptor", defaultValue = "./data/scripts/system/database/database.xml")
	public static File DATABASE_SCRIPTCONTEXT_DESCRIPTOR;
}