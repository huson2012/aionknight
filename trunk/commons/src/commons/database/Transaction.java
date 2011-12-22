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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import org.apache.log4j.Logger;

public class Transaction
{
	private static final Logger	log	= Logger.getLogger(Transaction.class);
	private Connection			connection;

	Transaction(Connection con) throws SQLException
	{
		this.connection = con;
		connection.setAutoCommit(false);
	}

	public void insertUpdate(String sql) throws SQLException
	{
		insertUpdate(sql, null);
	}

	public void insertUpdate(String sql, IUStH iusth) throws SQLException
	{
		PreparedStatement statement = connection.prepareStatement(sql);
		if(iusth != null)
		{
			iusth.handleInsertUpdate(statement);
		}
		else
		{
			statement.executeUpdate();
		}
	}

	public Savepoint setSavepoint(String name) throws SQLException
	{
		return connection.setSavepoint(name);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		connection.releaseSavepoint(savepoint);
	}

	public void commit() throws SQLException
	{
		commit(null);
	}

	public void commit(Savepoint rollBackToOnError) throws SQLException
	{

		try
		{
			connection.commit();
		}
		catch(SQLException e)
		{
			log.warn("Error while commiting transaction", e);

			try
			{
				if(rollBackToOnError != null)
				{
					connection.rollback(rollBackToOnError);
				}
				else
				{
					connection.rollback();
				}
			}
			catch(SQLException e1)
			{
				log.error("Can't rollback transaction", e1);
			}
		}

		connection.setAutoCommit(true);
		connection.close();
	}
}