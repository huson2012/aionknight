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
import gameserver.dao.DropListDAO;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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