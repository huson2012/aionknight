/**
 * This file is part of Aion-Knight <aionu-unique.com>.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;

import gameserver.dao.DropListDAO;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;


/**

 * 
 */
public class MySQL5DropListDAO extends DropListDAO
{    
	private static final Logger	log				= Logger.getLogger(MySQL5DropListDAO.class);
    @Override
    public DropList load() 
    {
        final DropList dropList = new DropList();
        Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM `droplist`");
			
			ResultSet resultSet = stmt.executeQuery();
					
			while (resultSet.next())
		 	{
				int mobId = resultSet.getInt("mobId");
                int itemId = resultSet.getInt("itemId");
                int min = resultSet.getInt("min");
                int max = resultSet.getInt("max");
                float chance = resultSet.getFloat("chance");
                
                if(chance > 0)
                {
                    DropTemplate dropTemplate = new DropTemplate(mobId, itemId, min, max, chance);
                    dropList.addDropTemplate(mobId, dropTemplate);
                }
		 	}
			
			resultSet.close();
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
        
        return dropList;
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
