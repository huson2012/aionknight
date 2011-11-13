/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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