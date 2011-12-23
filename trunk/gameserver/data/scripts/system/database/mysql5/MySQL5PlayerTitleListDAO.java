/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package mysql5;

import commons.database.DatabaseFactory;
import gameserver.dao.PlayerTitleListDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.player.TitleList;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class MySQL5PlayerTitleListDAO extends PlayerTitleListDAO
{
	private static final String LOAD_QUERY = "SELECT `title_id`, `title_expires_time`, `title_date` FROM `player_titles` WHERE `player_id`=?";
	private static final String INSERT_QUERY = "INSERT INTO `player_titles`(`player_id`,`title_id`, `title_expires_time`, `title_date`) VALUES (?,?,?,?)";
	private static final String CHECK_QUERY = "SELECT `title_id` FROM `player_titles` WHERE `player_id`=? AND `title_id`=?";
	private static final String DELETE_QUERY = "DELETE FROM `player_titles` WHERE `player_id` = ? AND `title_id` = ?";
	private static final Logger log = Logger.getLogger(MySQL5PlayerTitleListDAO.class);
	
	@Override
	public TitleList loadTitleList(final int playerId)
	{
		final TitleList tl = new TitleList ();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int id = rset.getInt("title_id");
				long title_date = rset.getTimestamp("title_date").getTime();
				long title_expires_time = rset.getLong("title_expires_time");

				tl.addTitle(id, title_date, title_expires_time);
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
		
		return tl;
	}

	@Override
	public boolean storeTitles(Player player)
	{
		final int playerId = player.getObjectId();
		
		for (final Title t : player.getTitleList().getTitles())
		{
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement(CHECK_QUERY);
				stmt.setInt(1, playerId);
				stmt.setInt(2, t.getTitleId());
				ResultSet rset = stmt.executeQuery();
				if (!rset.next())
				{
					Connection con2 = null;
					try
					{
						con2 = DatabaseFactory.getConnection();
						PreparedStatement stmt2 = con2.prepareStatement(INSERT_QUERY);
						stmt2.setInt(1, playerId);
						stmt2.setInt(2, t.getTitleId());
						stmt2.setLong(3, t.getTitleExpiresTime());
						stmt2.setTimestamp(4, new Timestamp(t.getTitleDate()));

						stmt2.execute();
					}
					catch(Exception e)
					{
						log.error(e);
					}
					finally
					{
						DatabaseFactory.close(con2);
					}
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
		return true;
	}

	@Override
	public void removeTitle(int playerId, int titleId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt2 = con.prepareStatement(DELETE_QUERY);
			stmt2.setInt(1, playerId);
			stmt2.setInt(2, titleId);
			stmt2.execute();
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
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}