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

import commons.database.DB;
import commons.database.DatabaseFactory;
import commons.database.IUStH;
import commons.database.dao.DAOManager;
import gameserver.dao.BlockListDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.BlockList;
import gameserver.model.gameobjects.player.BlockedPlayer;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * MySQL5 DAO for editing the block list
 */
public class MySQL5BlockListDAO extends BlockListDAO
{
	public static final String LOAD_QUERY = "SELECT blocked_player, reason FROM blocks WHERE player=?";
	public static final String ADD_QUERY = "INSERT INTO blocks (player, blocked_player, reason) VALUES (?, ?, ?)";
	public static final String DEL_QUERY = "DELETE FROM blocks WHERE player=? AND blocked_player=?";
	public static final String SET_REASON_QUERY = "UPDATE blocks SET reason=? WHERE player=? AND blocked_player=?";
	private static Logger log = Logger.getLogger(MySQL5BlockListDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addBlockedUser(final int playerObjId,final int objIdToBlock,final String reason)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(ADD_QUERY);
			stmt.setInt(1, playerObjId);
			stmt.setInt(2,objIdToBlock);
			stmt.setString(3, reason);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delBlockedUser(final int playerObjId,final int objIdToDelete)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DEL_QUERY);
			stmt.setInt(1, playerObjId);
			stmt.setInt(2,objIdToDelete);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BlockList load(final Player player)
	{
		final Map<Integer, BlockedPlayer> list = new HashMap<Integer, BlockedPlayer>();
		Connection con = null;
		
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet resultSet = stmt.executeQuery();
			PlayerDAO playerDao = DAOManager.getDAO(PlayerDAO.class);	
			while (resultSet.next())
		 	{
				int blockedOid = resultSet.getInt("blocked_player");
				PlayerCommonData pcd = playerDao.loadPlayerCommonData(blockedOid);
				if (pcd == null)
				{
					log.error("Attempt to load block list for " + player.getName() + " tried to load a player which does not exist: " + blockedOid);
				}
				else
				{
					list.put(blockedOid, new BlockedPlayer(pcd, resultSet.getString("reason")));
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
		
		return new BlockList(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setReason(final int playerObjId, final int blockedPlayerObjId, final String reason)
	{
		return DB.insertUpdate(SET_REASON_QUERY, new IUStH(){
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setString(1, reason);
				stmt.setInt(2, playerObjId);
				stmt.setInt(3, blockedPlayerObjId);
				stmt.execute();
				
			}
		});
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