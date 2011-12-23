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
import gameserver.dao.PlayerPunishmentsDAO;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySQL5PlayerPunishmentsDAO extends PlayerPunishmentsDAO
{
	public static final String	SELECT_QUERY	= "SELECT `player_id`, `punishment_status`, `punishment_timer` FROM `player_punishments` WHERE `player_id`=?";
	public static final String	UPDATE_QUERY	= "UPDATE `player_punishments` SET `punishment_status`=?, `punishment_timer`=? WHERE `player_id`=?";
	public static final String	REPLACE_QUERY	= "REPLACE INTO `player_punishments` VALUES (?,?,?)";
	public static final String	DELETE_QUERY	= "DELETE FROM `player_punishments` WHERE `player_id`=?";

	private static final Logger log = Logger.getLogger(MySQL5PlayerPunishmentsDAO.class);
	
	@Override
	public void loadPlayerPunishments(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				player.setPrisonTimer(rs.getLong("punishment_timer"));

				if(player.isInPrison())
					player.setPrisonTimer(rs.getLong("punishment_timer"));
				else
					player.setPrisonTimer(0);
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
	public void storePlayerPunishments(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);
			ps.setInt(1, player.isInPrison() ? 1 : 0);
			ps.setLong(2, player.getPrisonTimer());
			ps.setInt(3, player.getObjectId());
			ps.execute();
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
	public void punishPlayer(final Player player, final int mode)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(REPLACE_QUERY);
			ps.setInt(1, player.getObjectId());
			ps.setInt(2, mode);
			ps.setLong(3, player.getPrisonTimer());
			ps.execute();
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
	public void unpunishPlayer(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_QUERY);
			ps.setInt(1, player.getObjectId());
			ps.execute();
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
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}