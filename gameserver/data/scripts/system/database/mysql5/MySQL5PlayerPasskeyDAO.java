/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package mysql5;

import commons.database.DatabaseFactory;
import gameserver.dao.PlayerPasskeyDAO;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL5PlayerPasskeyDAO extends PlayerPasskeyDAO
{
	private static final Logger log	= Logger.getLogger(MySQL5PlayerPasskeyDAO.class);

	public static final String INSERT_QUERY	= "INSERT INTO `player_passkey` (`account_id`, `passkey`) VALUES (?,?)";
	public static final String UPDATE_QUERY	= "UPDATE `player_passkey` SET `passkey`=? WHERE `account_id`=? AND `passkey`=?";
	public static final String UPDATE_FORCE_QUERY = "UPDATE `player_passkey` SET `passkey`=? WHERE `account_id`=?";
	public static final String CHECK_QUERY = "SELECT COUNT(*) cnt FROM `player_passkey` WHERE `account_id`=? AND `passkey`=?";
	public static final String EXIST_CHECK_QUERY = "SELECT COUNT(*) cnt FROM `player_passkey` WHERE `account_id`=?";

	/**
	 * (non-Javadoc)
	 * @see gameserver.dao.PlayerPasskeyDAO#insertPlayerPasskey(int, java.lang.String)
	 */
	@Override
	public void insertPlayerPasskey(int accountId, String passkey)
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);

			stmt.setInt(1, accountId);
			stmt.setString(2, passkey);

			stmt.execute();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error saving PlayerPasskey. accountId: " + accountId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/**
	* (non-Javadoc)
	* @see gameserver.dao.PlayerPasskeyDAO#updatePlayerPasskey(int, java.lang.String, java.lang.String)
	*/
	@Override
	public boolean updatePlayerPasskey(int accountId, String oldPasskey, String newPasskey)
	{
		boolean result = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);

			stmt.setString(1, newPasskey);
			stmt.setInt(2, accountId);
			stmt.setString(3, oldPasskey);

			if (stmt.executeUpdate() > 0)
				result = true;
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error updating PlayerPasskey. accountId: " + accountId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return result;
	}

	/**
	* (non-Javadoc)
	* @see gameserver.dao.PlayerPasskeyDAO#updateForcePlayerPasskey(int, java.lang.String)
	*/
	@Override
	public boolean updateForcePlayerPasskey(int accountId, String newPasskey)
	{
		boolean result = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_FORCE_QUERY);

			stmt.setString(1, newPasskey);
			stmt.setInt(2, accountId);

			if (stmt.executeUpdate() > 0)
				result = true;
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error updaing PlayerPasskey. accountId: " + accountId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return result;
	}

	/**
	 * (non-Javadoc)
	 * @see gameserver.dao.PlayerPasskeyDAO#checkPlayerPasskey(int, java.lang.String)
	 */
	@Override
	public boolean checkPlayerPasskey(int accountId, String passkey)
	{
		boolean passkeyChecked = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(CHECK_QUERY);

			stmt.setInt(1, accountId);
			stmt.setString(2, passkey);

			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				if (rset.getInt("cnt") == 1)
				passkeyChecked = true;
			}

			rset.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error loading PlayerPasskey. accountId: " + accountId, e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return passkeyChecked;
	}

	/**
	* (non-Javadoc)
	* @see gameserver.dao.PlayerPasskeyDAO#existCheckPlayerPasskey(int)
	*/
	@Override
	public boolean existCheckPlayerPasskey(int accountId)
	{
		boolean existPasskeyChecked = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(EXIST_CHECK_QUERY);

			stmt.setInt(1, accountId);

			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				if (rset.getInt("cnt") == 1)
					existPasskeyChecked = true;
			}

			rset.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error loading PlayerPasskey. accountId: " + accountId, e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return existPasskeyChecked;
	}

	/**
	 * (non-Javadoc)
	 * @see commons.database.dao.DAO#supports(java.lang.String, int, int)
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}