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

import com.mysql.jdbc.exceptions.MySQLDataException;
import commons.database.DatabaseFactory;
import gameserver.dao.PlayerWorldBanDAO;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class MySQL5PlayerWorldBanDAO extends PlayerWorldBanDAO
{
	private static final Logger log = Logger.getLogger(MySQL5PlayerWorldBanDAO.class);
	
	@Override
	public void loadWorldBan(Player player)
	{
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM player_world_bans WHERE `player` = ?");
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
			{
				player.setBannedFromWorld(rs.getString("by"), rs.getString("reason"), rs.getLong("duration"), new Date(rs.getLong("date")));
			}
			rs.close();
			stmt.close();
		}
		catch (MySQLDataException mde) { }
		catch (Exception e)
		{
			log.error("cannot load world ban for player #"+player.getObjectId());
			e.printStackTrace();
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public boolean addWorldBan(int playerObjId, String by, long duration, Date date, String reason)
	{
		String query = "SELECT * FROM player_world_bans WHERE `player` = ?";
		Connection con = null;
		boolean result = false;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, playerObjId);
			ResultSet rset = stmt.executeQuery();
			if(!rset.next())
			{
				query = "INSERT INTO player_world_bans(`player`, `by`, `duration`, `date`, `reason`) VALUES (?,?,?,?,?)";
				stmt = con.prepareStatement(query);
				stmt.setInt(1, playerObjId);
				stmt.setString(2, by);
				stmt.setLong(3, duration);
				stmt.setLong(4, date.getTime());
				stmt.setString(5, reason);
				stmt.execute();
				stmt.close();
				result = true;
			}
			else
			{
				log.warn("player #"+playerObjId+" already banned");
				result = false;
			}
		}
		catch(MySQLDataException mde) { result = false; }
		catch (Exception e) 
		{
			log.error("cannot insert world ban for player #"+playerObjId);
			e.printStackTrace();
			result = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return result;
	}

	@Override
	public void removeWorldBan(int playerObjId)
	{
		String query = "DELETE FROM player_world_bans WHERE `player` = ?";
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, playerObjId);
			stmt.execute();
			stmt.close();
		}
		catch (MySQLDataException mde) { }
		catch (Exception e)
		{
			log.error("cannot delete world ban for player #"+playerObjId);
			e.printStackTrace();
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}