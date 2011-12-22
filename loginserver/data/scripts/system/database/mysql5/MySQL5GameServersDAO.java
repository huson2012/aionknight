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

package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import loginserver.GameServerInfo;
import loginserver.dao.GameServersDAO;
import org.apache.log4j.Logger;
import commons.database.DB;
import commons.database.DatabaseFactory;
import commons.database.ReadStH;

/**
 * GameServers DAO implementation for MySQL5
 * 
 */
public class MySQL5GameServersDAO extends GameServersDAO
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Byte, GameServerInfo> getAllGameServers()
	{

		final Map<Byte, GameServerInfo> result = new HashMap<Byte, GameServerInfo>();
		DB.select("SELECT * FROM gameservers", new ReadStH() {
			@Override
			public void handleRead(ResultSet resultSet) throws SQLException
			{
				while (resultSet.next())
				{
					byte id = resultSet.getByte("id");
					String ipMask = resultSet.getString("mask");
					String password = resultSet.getString("password");
					GameServerInfo gsi = new GameServerInfo(id, ipMask, password);
					result.put(id, gsi);
				}
			}
		});
		return result;
	}
	
	@Override
	public void writeGameServerStatus(GameServerInfo gsi)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			String query = "UPDATE gameservers SET status = ? WHERE id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			
			stmt.setInt(1, gsi.isOnline() ? 1 : 0);
			stmt.setInt(2, gsi.getId());
			
			stmt.execute();
		}
		catch(Exception e)
		{
			Logger.getLogger(MySQL5GameServersDAO.class).error("Cannot write GS " + gsi.getId() + " status", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}