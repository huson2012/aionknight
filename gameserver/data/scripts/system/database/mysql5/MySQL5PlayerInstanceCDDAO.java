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
import gameserver.dao.PlayerInstanceCDDAO;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

public class MySQL5PlayerInstanceCDDAO extends PlayerInstanceCDDAO
{
	private static final Logger	log					= Logger.getLogger(MySQL5PlayerInstanceCDDAO.class);
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadCooldowns(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT `instanceMapId`, `CDEnd`, `instanceId`, `groupId` FROM `player_instancecd` WHERE `playerId` = ?");
			ps.setInt(1, player.getObjectId());
			ResultSet rset = ps.executeQuery();
			while(rset.next())
			{
				int instanceMapId = rset.getInt("instanceMapId");
				Timestamp CDEnd = rset.getTimestamp("CDEnd");
				int instanceId = rset.getInt("instanceId");
				int groupId = rset.getInt("groupId");
				
				Calendar currentTime = Calendar.getInstance();
				Calendar CDEndTime = Calendar.getInstance();
				CDEndTime.setTimeInMillis(CDEnd.getTime());
				
				if(CDEndTime.after(currentTime))
					player.addInstanceCD(instanceMapId, CDEnd, instanceId, groupId);
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
	public void storeCooldowns(final Player player)
	{
		deleteCooldowns(player);
		
		if(player.getInstanceCDs().isEmpty())
			return;
		
		for(int i : player.getInstanceCDs().keys())
		{
			int instanceMapId = i;
			Timestamp CDEnd = player.getInstanceCD(i).getCDEndTime();
			int instanceId = player.getInstanceCD(i).getInstanceId();
			int groupId = player.getInstanceCD(i).getGroupId();
			
			if(CDEnd.getTime() - System.currentTimeMillis() <= 0)
				continue;
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement("INSERT INTO `player_instancecd` VALUES (?, ?, ?, ?, ?)");
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, instanceMapId);
				stmt.setTimestamp(3, CDEnd);
				stmt.setInt(4, instanceId);
				stmt.setInt(5, groupId);
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
	
	private void deleteCooldowns(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM `player_instancecd` WHERE `playerId` = ?");
			stmt.setInt(1, player.getObjectId());
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}