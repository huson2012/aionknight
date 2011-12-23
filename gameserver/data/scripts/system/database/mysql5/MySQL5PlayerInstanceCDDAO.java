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