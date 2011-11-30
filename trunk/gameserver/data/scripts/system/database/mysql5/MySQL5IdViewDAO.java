/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package mysql5;

import commons.database.DatabaseFactory;
import gameserver.dao.IdViewDAO;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL5IdViewDAO extends IdViewDAO
{
	private static final Logger	log = Logger.getLogger(MySQL5IdViewDAO.class);
	public static final String SELECT_QUERY = "SELECT `players`.`id` AS `id` from `players` UNION SELECT `itemUniqueId` FROM `inventory` UNION SELECT `id` FROM `legions` UNION SELECT `mailUniqueId` FROM `mail`";
	
	@Override
	public int[] getUsedIDs()
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			con.setReadOnly(true);
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE,	ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for(int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("id");
			}
			statement.close();
			return ids;
		}
		catch(SQLException e)
		{
			log.error("Can't get list of id's from union query", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return new int[0];
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}