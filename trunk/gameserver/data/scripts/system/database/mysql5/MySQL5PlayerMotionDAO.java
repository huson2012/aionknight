package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;

import ru.aionknight.gameserver.dao.PlayerMotionDAO;
import ru.aionknight.gameserver.model.gameobjects.player.Player;


/**
 * @author Mugen
 */
public class MySQL5PlayerMotionDAO extends PlayerMotionDAO
{
	private static final Logger log			= Logger.getLogger(MySQL5PlayerMotionDAO.class);

	public static final String  INSERT_QUERY	= "INSERT INTO `player_motion` (`player_id`, `learn_ninja`, `learn_hober`, `waiting_motion`, `running_motion`, `jumping_motion`, `rest_motion`) VALUES (?,0,0,0,0,0,0)";
	public static final String  UPDATE_QUERY	= "UPDATE `player_motion` SET `learn_ninja`=?,  `learn_hober`=?,  `waiting_motion`=?,  `running_motion`=?,  `jumping_motion`=?,  `rest_motion`=? WHERE `player_id`=?";
	public static final String  SELECT_QUERY	= "SELECT * FROM `player_motion` WHERE `player_id`=?";

	/*
	 * (non-Javadoc)
	 * @see org.openaion.gameserver.dao.PlayerMotionDAO#insertPlayerMotion(int)
	 */
	@Override
	public void insertPlayerMotion(int playerId)
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);

			stmt.setInt(1, playerId);

			stmt.execute();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error saving PlayerMotion. playerId: " + playerId, e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/*
	* (non-Javadoc)
	* @see org.openaion.gameserver.dao.PlayerMotionDAO#updatePlayerMotion(int, int, int)
	*/
	@Override
	public void updatePlayerMotion(int learnNinja, int learnHober, Player player)
	{
		updatePlayerMotion(learnNinja, learnHober, 0, 0, 0, 0, player);
	}
	/*
	* (non-Javadoc)
	* @see org.openaion.gameserver.dao.PlayerMotionDAO#updatePlayerMotion(int, int, int, int, int, int, int)
	*/
	@Override
	public boolean updatePlayerMotion(int learnNinja, int learnHober, int waitingMotion, int runningMotion, int jumpingMotion, int restMotion, Player player)
	{
		boolean result = false;
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);

			stmt.setInt(1, learnNinja);
			stmt.setInt(2, learnHober);
			stmt.setInt(3, waitingMotion);
			stmt.setInt(4, runningMotion);
			stmt.setInt(5, jumpingMotion);
			stmt.setInt(6, restMotion);
			stmt.setInt(7, player.getObjectId());

			if (stmt.executeUpdate() > 0)
				result = true;
			stmt.close();
		}
		catch (SQLException e)
		{
			log.fatal("Error updating PlayerMotion. playerId: " + player.getObjectId(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

	    return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.openaion.gameserver.dao.PlayerMotionDAO#selectPlayerMotion(int)
	 */
	@Override
	public void loadPlayerMotion(Player player)
	{
		Connection con = null;

		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);

			stmt.setInt(1, player.getObjectId());

			ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				int		learnNinja			= rset.getInt("learn_ninja");
				int		learnHober			= rset.getInt("learn_hober");
				int		waitingMotion		= rset.getInt("waiting_motion");
				int		runningMotion		= rset.getInt("running_motion");
				int		jumpingMotion		= rset.getInt("jumping_motion");
				int		restMotion			= rset.getInt("rest_motion");

				player.setMotion(learnNinja, learnHober, waitingMotion, runningMotion, jumpingMotion, restMotion);
			}

			rset.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.openaion.commons.database.dao.DAO#supports(java.lang.String, int, int)
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
