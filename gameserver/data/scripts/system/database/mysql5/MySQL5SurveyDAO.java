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
import gameserver.dao.SurveyDAO;
import gameserver.model.gameobjects.Survey;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQL5SurveyDAO extends SurveyDAO
{
	private static final Logger log = Logger.getLogger(MySQL5InventoryDAO.class);

	public static final String DELETE_QUERY = "DELETE FROM `surveys` WHERE `survey_id`=?";
	public static final String SELECT_QUERY = "SELECT * FROM `surveys` WHERE `player_id`=? ORDER BY `survey_id` DESC";
	public static final String SELECT_SURVEY_QUERY = "SELECT * FROM `surveys` WHERE `survey_id`=? AND `player_id`=?";
	public static final String INSERT_QUERY = "INSERT INTO `surveys` (`player_id`, `title`, `message`, `select_text`, `itemId`, `itemCount`) VALUES (?,?,?,?,?,?)";

	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}

	@Override
	public boolean deleteSurvey(int survey_id)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, survey_id);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Error delete survey_id: " + survey_id, e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public List<Survey> loadSurveys(int playerId)
	{
		final List<Survey> surveys = new ArrayList<Survey>();

		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int survey_id = rset.getInt("survey_id");
				int player_id = rset.getInt("player_id");
				String title= rset.getString("title");
				String message = rset.getString("message");
				String select_text = rset.getString("select_text");
				int itemId = rset.getInt("itemId");
				int itemCount = rset.getInt("itemCount");
				long itemExistTime = rset.getLong("itemExistTime");
				int itemTradeTime = rset.getInt("itemTradeTime");

				Survey survey = new Survey(survey_id, player_id, title, message, select_text, itemId, itemCount, itemExistTime, itemTradeTime);
				surveys.add(survey);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore Survey data for player: " + playerId + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return surveys;
	}

	@Override
	public Survey loadSurvey(int player_id, int survey_id)
	{
		Survey survey = null;

		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_SURVEY_QUERY);
			stmt.setInt(1, survey_id);
			stmt.setInt(2, player_id);

			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				String title= rset.getString("title");
				String message = rset.getString("message");
				String select_text = rset.getString("select_text");
				int itemId = rset.getInt("itemId");
				int itemCount = rset.getInt("itemCount");
				long itemExistTime = rset.getLong("itemExistTime");
				int itemTradeTime = rset.getInt("itemTradeTime");

				survey = new Survey(survey_id, player_id, title, message, select_text, itemId, itemCount, itemExistTime, itemTradeTime);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore Survey data for player: " + player_id + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return survey;
	}
	
	@Override
	public void addSurvey(int playerId, String title, String message, String selectText, int itemId, int itemCount)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setString(2, title);
			stmt.setString(3, message);
			stmt.setString(4, selectText);
			stmt.setInt(5, itemId);
			stmt.setInt(6, itemCount);
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
}