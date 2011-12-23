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
import gameserver.dao.GameTimeDAO;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL5GameTimeDAO extends GameTimeDAO
{
	private static Logger log = Logger.getLogger(MySQL5GameTimeDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int load()
	{
		Connection con = null;
		int result = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT `value` FROM `server_variables` WHERE `key`='time'");
			ResultSet resultSet = stmt.executeQuery();
			if(resultSet.next())
				result = Integer.parseInt(resultSet.getString("value"));
			else
				result = 0;
		 				
			resultSet.close();
			stmt.close();
		}
		catch(SQLException e)
		{
			log.error(e);
			result = 0;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean store(int time)
	{
		boolean success = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement ps = con.prepareStatement("REPLACE INTO `server_variables` (`key`,`value`) VALUES (?,?)");
			ps.setString(1, "time");
			ps.setString(2, String.valueOf(time));
			success = ps.executeUpdate() > 0;
		}
		catch(SQLException e)
		{
			log.error("Error storing server time", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return success;
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