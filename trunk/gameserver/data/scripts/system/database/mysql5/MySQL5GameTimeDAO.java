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
import gameserver.dao.GameTimeDAO;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL5GameTimeDAO extends GameTimeDAO
{
	private static Logger log = Logger.getLogger(MySQL5GameTimeDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int load()
	{
		Connection con = null;
		int result = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT `value` FROM `server_variables` WHERE `key`='time'");
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
				result = Integer.parseInt(resultSet.getString("value"));
			else
				result = 0;
		 				
			resultSet.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
			result = 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean store(int time)
	{
		boolean success = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("REPLACE INTO `server_variables` (`key`,`value`) VALUES (?,?)");
			ps.setString(1, "time");
			ps.setString(2, String.valueOf(time));
			success = ps.executeUpdate() > 0;
		}
		catch(SQLException e)
		{
			log.error("Error storing server time", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return success;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}