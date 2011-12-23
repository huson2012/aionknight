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
import commons.database.dao.DAOManager;
import gameserver.dao.FriendListDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Friend;
import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQL5FriendListDAO extends FriendListDAO
{
	private static final Logger log = Logger.getLogger(MySQL5FriendListDAO.class);
	public static final String LOAD_QUERY = "SELECT * FROM `friends` WHERE `player`=?";
	public static final String ADD_QUERY = "INSERT IGNORE INTO `friends` (`player`,`friend`) VALUES (?, ?)";
	public static final String DEL_QUERY = "DELETE FROM friends WHERE player = ? AND friend = ?";

	/** (non-Javadoc)
	 * @see gameserver.dao.FriendListDAO#load(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public FriendList load(final Player player)
	{
		final List<Friend> friends = new ArrayList<Friend>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			PlayerDAO dao = DAOManager.getDAO(PlayerDAO.class);
			while (rset.next()) 
			{
				int objId = rset.getInt("friend");
					
				PlayerCommonData pcd = dao.loadPlayerCommonData(objId);
				if (pcd != null)
				{
					Friend friend = new Friend(pcd);
					friends.add(friend);
				}
			}
		}
		catch (Exception e)
		{
			log.fatal("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return new FriendList(player,friends);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addFriends(final Player player, final Player friend)
	{	
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(ADD_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, friend.getObjectId());
			stmt.addBatch();
			
			stmt.setInt(1, friend.getObjectId());
			stmt.setInt(2, player.getObjectId());
			stmt.addBatch();
			stmt.executeBatch();
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
	
	@Override
	public boolean delFriends(final int playerOid, final int friendOid) 
	{	
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DEL_QUERY);
			stmt.setInt(1, playerOid);
			stmt.setInt(2, friendOid);
			stmt.execute();
			stmt.close();
			
			PreparedStatement stmt2 = con.prepareStatement(DEL_QUERY);
			stmt2.setInt(1, friendOid);
			stmt2.setInt(2, playerOid);
			stmt2.execute();
			stmt2.close();
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
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}