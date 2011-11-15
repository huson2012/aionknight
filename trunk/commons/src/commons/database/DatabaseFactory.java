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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import commons.configuration.ConfigurableProcessor;
import commons.utils.PropertiesUtils;

public class DatabaseFactory
{
	private static final Logger log	= Logger.getLogger(DatabaseFactory.class);
	private static DataSource dataSource;
	private static GenericObjectPool connectionPool;
	private static String databaseName;
	private static int databaseMajorVersion;
	private static int databaseMinorVersion;
	
	public synchronized static void init()
	{
		init("");
	}
	
	public synchronized static void init(String configFilePath)
	{
		if(dataSource != null)
		{
			return;
		}
		
		if(!configFilePath.equals(""))
		{
			try
			{
				Properties dbProps = PropertiesUtils.load(configFilePath);
				ConfigurableProcessor.process(DatabaseConfig.class, dbProps);
			}
			catch(IOException ex)
			{
				log.fatal("Cannot load database config", ex);
			}
		}

		try
		{
			DatabaseConfig.DATABASE_DRIVER.newInstance();
		}
		catch(Exception e)
		{
			log.fatal("Error obtaining DB driver", e);
			throw new Error("DB Driver doesnt exist!");
		}

		connectionPool = new GenericObjectPool();

		if(DatabaseConfig.DATABASE_CONNECTIONS_MIN > DatabaseConfig.DATABASE_CONNECTIONS_MAX)
		{
			log.error("Please check your database configuration. Minimum amount of connections is > maximum");
			DatabaseConfig.DATABASE_CONNECTIONS_MAX = DatabaseConfig.DATABASE_CONNECTIONS_MIN;
		}

		connectionPool.setMaxIdle(DatabaseConfig.DATABASE_CONNECTIONS_MIN);
		connectionPool.setMaxActive(DatabaseConfig.DATABASE_CONNECTIONS_MAX);

		try
		{
			dataSource = setupDataSource();
			Connection c = getConnection();
			DatabaseMetaData dmd = c.getMetaData();
			databaseName = dmd.getDatabaseProductName();
			databaseMajorVersion = dmd.getDatabaseMajorVersion();
			databaseMinorVersion = dmd.getDatabaseMinorVersion();
			c.close();
		}
		catch(Exception e)
		{
			log.fatal("Error with connection string: " + DatabaseConfig.DATABASE_URL, e);
			throw new Error("DatabaseFactory not initialized!");
		}

		log.info("Successfully connected to database");
	}

	private static DataSource setupDataSource() throws Exception
	{
		ConnectionFactory conFactory = new DriverManagerConnectionFactory(DatabaseConfig.DATABASE_URL,
			DatabaseConfig.DATABASE_USER, DatabaseConfig.DATABASE_PASSWORD);

		new PoolableConnectionFactoryAE(conFactory, connectionPool, null, 1, false, true);

		return new PoolingDataSource(connectionPool);
	}

	public static Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}

	public int getActiveConnections()
	{
		return connectionPool.getNumActive();
	}

	public int getIdleConnections()
	{
		return connectionPool.getNumIdle();
	}

	public static synchronized void shutdown()
	{
		try
		{
			connectionPool.close();
		}
		catch(Exception e)
		{
			log.warn("Failed to shutdown DatabaseFactory", e);
		}

		dataSource = null;
	}

	public static void close(Connection con)
	{
		if (con == null)
			return;
		
		try
		{
			con.close();
		}
		catch (SQLException e)
		{
			log.warn("DatabaseFactory: Failed to close database connection!", e);
		}
	}

	public static String getDatabaseName()
	{
		return databaseName;
	}

	public static int getDatabaseMajorVersion()
	{
		return databaseMajorVersion;
	}

	public static int getDatabaseMinorVersion()
	{
		return databaseMinorVersion;
	}

	private DatabaseFactory()
	{
	}
}