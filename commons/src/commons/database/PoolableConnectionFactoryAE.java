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

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.ObjectPool;

public class PoolableConnectionFactoryAE extends PoolableConnectionFactory
{
	private final int	validationTimeout;

	public PoolableConnectionFactoryAE(ConnectionFactory connFactory, ObjectPool pool,
		KeyedObjectPoolFactory stmtPoolFactory, int validationTimeout, boolean defaultReadOnly,
		boolean defaultAutoCommit)
	{
		super(connFactory, pool, stmtPoolFactory, null, defaultReadOnly, defaultAutoCommit);
		this.validationTimeout = validationTimeout;
	}

	@Override
	public void validateConnection(Connection conn) throws SQLException
	{
		if(conn.isClosed())
			throw new SQLException("validateConnection: connection closed");
		if(validationTimeout >= 0 && !conn.isValid(validationTimeout))
			throw new SQLException("validateConnection: connection invalid");
	}
}