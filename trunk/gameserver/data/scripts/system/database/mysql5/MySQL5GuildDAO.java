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
import gameserver.dao.GuildDAO;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Guild;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.*;

public class MySQL5GuildDAO extends GuildDAO
{
	public static final String SELECT_QUERY = "SELECT `guild_id`, `last_quest`, `complete_time`, `current_quest` FROM `guilds` WHERE `player_id`=?";
	public static final String INSERT_QUERY = "INSERT INTO `guilds` (`player_id`, `guild_id`, `last_quest`, `complete_time`, `current_quest`) VALUES(?,?,?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE guilds SET guild_id=?, last_quest=?, complete_time=?, current_quest=? WHERE player_id=?";
	private static final Logger log = Logger.getLogger(MySQL5GuildDAO.class);
	
	@Override
	public void loadGuild(final Player player)
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
				int guildId = rset.getInt("guild_id");
				int lastQuest = rset.getInt("last_quest");
				Timestamp completeTime = rset.getTimestamp("complete_time");
				int currentQuest = rset.getInt("current_quest");
				Guild guild = new Guild(guildId, lastQuest, completeTime, currentQuest);
				guild.setPersistentState(PersistentState.UPDATED);
				player.setGuild(guild);
			}
			else
			{
				Guild guild = new Guild(0, 0, null, 0);
				guild.setPersistentState(PersistentState.NEW);
				player.setGuild(guild);
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
	}

	@Override
	public void storeGuild(Player player)
	{
		Guild guild = player.getGuild();
		switch(guild.getPersistentState())
		{
			case NEW:
				addGuild(player.getObjectId(), guild);
				break;
			case UPDATE_REQUIRED:
				updateGuild(player.getObjectId(), guild);
				break;
		}
		guild.setPersistentState(PersistentState.UPDATED);
	}

	/**
	 * @param objectId
	 * @param event
	 * @return
	 */
	private void addGuild(final int objectId, final Guild guild)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, guild.getGuildId());
			stmt.setInt(3, guild.getLastQuest());
			stmt.setTimestamp(4, guild.getCompleteTime());
			stmt.setInt(5, guild.getCurrentQuest());
			stmt.execute();
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

	/**
	 * @param objectId
	 * @param rank
	 * @return
	 */
	private void updateGuild(final int objectId, final Guild guild)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, guild.getGuildId());
			stmt.setInt(2, guild.getLastQuest());
			stmt.setTimestamp(3, guild.getCompleteTime());
			stmt.setInt(4, guild.getCurrentQuest());
			stmt.setInt(5, objectId);
			stmt.execute();
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
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}