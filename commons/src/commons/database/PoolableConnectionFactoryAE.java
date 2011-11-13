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