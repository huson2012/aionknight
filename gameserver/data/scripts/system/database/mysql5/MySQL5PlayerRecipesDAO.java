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
import gameserver.dao.PlayerRecipesDAO;
import gameserver.model.gameobjects.player.RecipeList;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class MySQL5PlayerRecipesDAO extends PlayerRecipesDAO
{
	private static final Logger log = Logger.getLogger(MySQL5PlayerRecipesDAO.class);
	
	private static final String SELECT_QUERY = "SELECT `recipe_id` FROM player_recipes WHERE `player_id`=?";
	private static final String ADD_QUERY = "INSERT INTO player_recipes (`player_id`, `recipe_id`) VALUES (?, ?)";
	private static final String DELETE_QUERY = "DELETE FROM player_recipes WHERE `player_id`=? AND `recipe_id`=?";
	
	@Override
	public RecipeList load(final int playerId)
	{
		final HashSet<Integer>		recipeList = new HashSet<Integer>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				recipeList.add(rs.getInt("recipe_id"));
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return new RecipeList(recipeList);
	}
	
	@Override
	public void addRecipe(final int playerId, final int recipeId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_QUERY);
			ps.setInt(1, playerId);
			ps.setInt(2, recipeId);
			ps.execute();
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

	public void deleteRecipe(final int playerId, final int recipeId)
	{

		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			
			stmt.setInt(1, playerId);
			stmt.setInt(2, recipeId);
			stmt.execute();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}