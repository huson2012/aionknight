/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
 *
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова)
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package commons.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public final class DB
{
	protected static final Logger	log	= Logger.getLogger(DB.class);
	private DB()
	{

	}

	public static boolean select(String query, ReadStH reader)
	{
		return select(query, reader, null);
	}

	public static boolean select(String query, ReadStH reader, String errMsg)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rset;

		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(query);
			if(reader instanceof ParamReadStH)
				((ParamReadStH) reader).setParams(stmt);
			rset = stmt.executeQuery();
			reader.handleRead(rset);
		}
		catch(Exception e)
		{
			if(errMsg == null)
				log.warn("Error executing select query " + e, e);
			else
				log.warn(errMsg + " " + e, e);
			return false;
		}
		finally
		{
			try
			{
				if(con != null)
					con.close();
				if(stmt != null)
					stmt.close();
			}
			catch(Exception e)
			{
				log.warn("Failed to close DB connection " + e, e);
			}
		}
		return true;
	}

	public static boolean call(String query, ReadStH reader)
	{
		return call(query, reader, null);
	}

	public static boolean call(String query, ReadStH reader, String errMsg)
	{
		Connection con = null;
		CallableStatement stmt = null;
		ResultSet rset;

		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareCall(query);
			if(reader instanceof CallReadStH)
				((CallReadStH) reader).setParams(stmt);
			rset = stmt.executeQuery();
			reader.handleRead(rset);
		}
		catch(Exception e)
		{
			if(errMsg == null)
				log.warn("Error calling stored procedure " + e, e);
			else
				log.warn(errMsg + " " + e, e);
			return false;
		}
		finally
		{
			try
			{
				if(con != null)
					con.close();
				if(stmt != null)
					stmt.close();
			}
			catch(Exception e)
			{
				log.warn("Failed to close DB connection " + e, e);
			}
		}
		return true;
	}

	public static boolean insertUpdate(String query)
	{
		return insertUpdate(query, null, null);
	}

	public static boolean insertUpdate(String query, String errMsg)
	{
		return insertUpdate(query, null, errMsg);
	}

	public static boolean insertUpdate(String query, IUStH batch)
	{
		return insertUpdate(query, batch, null);
	}

	public static boolean insertUpdate(String query, IUStH batch, String errMsg)
	{
		Connection con = null;
		PreparedStatement stmt = null;

		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(query);
			if(batch != null)
				batch.handleInsertUpdate(stmt);
			else
				stmt.executeUpdate();

		}
		catch(Exception e)
		{
			if(errMsg == null)
				log.warn("Failed to execute IU query " + e, e);
			else
				log.warn(errMsg + " " + e, e);

			return false;
		}
		finally
		{
			try
			{
				if(con != null)
					con.close();
				if(stmt != null)
					stmt.close();
			}
			catch(Exception e)
			{
				log.warn("Failed to close DB connection " + e, e);
			}
		}
		return true;
	}

	public static Transaction beginTransaction() throws SQLException
	{
		Connection con = DatabaseFactory.getConnection();
		return new Transaction(con);
	}

	public static PreparedStatement prepareStatement(String sql)
	{
		return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	}

	public static PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
	{
		Connection c = null;
		PreparedStatement ps = null;
		try
		{
			c = DatabaseFactory.getConnection();
			ps = c.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}
		catch(Exception e)
		{
			log.error("Can't create PreparedStatement for querry: " + sql, e);
			if(c != null)
			{
				try
				{
					c.close();
				}
				catch(SQLException e1)
				{
					log.error("Can't close connection after exception", e1);
				}
			}
		}

		return ps;
	}

	public static int executeUpdate(PreparedStatement statement)
	{
		try
		{
			return statement.executeUpdate();
		}
		catch(Exception e)
		{
			log.error("Can't execute update for PreparedStatement", e);
		}

		return -1;
	}

	public static void executeUpdateAndClose(PreparedStatement statement)
	{
		executeUpdate(statement);
		close(statement);
	}

	public static ResultSet executeQuerry(PreparedStatement statement)
	{
		ResultSet rs = null;
		try
		{
			rs = statement.executeQuery();
		}
		catch(Exception e)
		{
			log.error("Error while executing querry", e);
		}
		return rs;
	}

	public static void close(PreparedStatement statement)
	{

		try
		{
			if(statement.isClosed())
			{
				log.warn("Attempt to close PreparedStatement that is closes already", new Exception());
				return;
			}

			Connection c = statement.getConnection();
			statement.close();
			c.close();
		}
		catch(Exception e)
		{
			log.error("Error while closing PreparedStatement", e);
		}
	}
}