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
import gameserver.dao.PlayerLifeStatsDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQL5PlayerLifeStatsDAO extends PlayerLifeStatsDAO
{

	/** Logger */
	private static final Logger log = Logger.getLogger(MySQL5PlayerLifeStatsDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `player_life_stats` (`player_id`, `hp`, `mp`, `fp`) VALUES (?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `player_life_stats` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `hp`, `mp`, `fp` FROM `player_life_stats` WHERE `player_id`=?";
	public static final String UPDATE_QUERY = "UPDATE player_life_stats set `hp`=?, `mp`=?, `fp`=? WHERE `player_id`=?";

	/** (non-Javadoc)
	 * @see gameserver.dao.PlayerLifeStatsDAO#loadPlayerLifeStat(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void loadPlayerLifeStat(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			if(rset.next())
			{
				PlayerLifeStats lifeStats = player.getLifeStats();
				lifeStats.setCurrentHp(rset.getInt("hp"));
				lifeStats.setCurrentMp(rset.getInt("mp"));
				lifeStats.setCurrentFp(rset.getInt("fp"));
			}
			else
				insertPlayerLifeStat(player);
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore PlayerLifeStat data for playerObjId: " + player.getObjectId() + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/** (non-Javadoc)
	 * @see gameserver.dao.PlayerLifeStatsDAO#storePlayerLifeStat(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void insertPlayerLifeStat(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, player.getLifeStats().getCurrentHp());
			stmt.setInt(3, player.getLifeStats().getCurrentMp());
			stmt.setInt(4, player.getLifeStats().getCurrentFp());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not store PlayerLifeStat data for player " + player.getObjectId() + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}


	/** (non-Javadoc)
	 * @see gameserver.dao.PlayerLifeStatsDAO#deletePlayerLifeStat(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void deletePlayerLifeStat(final int playerId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not delete PlayerLifeStat data for player " + playerId + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/** (non-Javadoc)
	 * @see gameserver.dao.PlayerLifeStatsDAO#updatePlayerLifeStat(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void updatePlayerLifeStat(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, player.getLifeStats().getCurrentHp());
			stmt.setInt(2, player.getLifeStats().getCurrentMp());
			stmt.setInt(3, player.getLifeStats().getCurrentFp());
			stmt.setInt(4, player.getObjectId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not update PlayerLifeStat data for player " + player.getObjectId() + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/** (non-Javadoc)
	 * @see commons.database.dao.DAO#supports(java.lang.String, int, int)
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}