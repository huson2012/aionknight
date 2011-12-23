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
import gameserver.dao.PlayerMacrossesDAO;
import gameserver.model.gameobjects.player.MacroList;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MySQL5PlayerMacrossesDAO extends PlayerMacrossesDAO
{
	private static Logger log = Logger.getLogger(MySQL5PlayerMacrossesDAO.class);
	
	public static final String INSERT_QUERY = "INSERT INTO `player_macrosses` (`player_id`, `order`, `macro`) VALUES (?,?,?)";
	public static final String	UPDATE_QUERY = "UPDATE `player_macrosses` SET `macro`=? WHERE `player_id`=? AND `order`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_macrosses` WHERE `player_id`=? AND `order`=?";
	public static final String SELECT_QUERY = "SELECT `order`, `macro` FROM `player_macrosses` WHERE `player_id`=?";

	/**
	 * Add a macro information into database
	 *
	 * @param playerId player object id
	 * @param macro    macro contents.
	 */
	@Override
	public void addMacro(final int playerId, final int macroPosition, final String macro)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, macroPosition);
			stmt.setString(3, macro);
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void updateMacro(final int playerId, final int macroPosition, final String macro)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, macro);
			stmt.setInt(2, playerId);
			stmt.setInt(3, macroPosition);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("[DAO: MySQL5PlayerMacrossesDAO] updating macro " + playerId + " "+ macroPosition);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void deleteMacro(final int playerId, final int macroPosition)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, macroPosition);
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/** {@inheritDoc} */
	@Override
	public MacroList restoreMacrosses(final int playerId)
	{
		final Map<Integer, String> macrosses = new HashMap<Integer, String>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			log.debug("[DAO: MySQL5PlayerMacrossesDAO] loading macroses for playerId: "+playerId);
			while(rset.next())
			{
				int order = rset.getInt("order");
				String text = rset.getString("macro");
				macrosses.put(order, text);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore MacroList data for player " + playerId + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return new MacroList(macrosses);
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}