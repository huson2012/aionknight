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
import gameserver.dao.ItemCooldownsDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.items.ItemCooldown;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

public class MySQL5ItemCooldownsDAO extends ItemCooldownsDAO
{
	public static final String INSERT_QUERY = "INSERT INTO `item_cooldowns` (`player_id`, `delay_id`, `use_delay`, `reuse_time`) VALUES (?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `item_cooldowns` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `delay_id`, `use_delay`, `reuse_time` FROM `item_cooldowns` WHERE `player_id`=?";

	private static final Logger log = Logger.getLogger(MySQL5ItemCooldownsDAO.class);
	
	@Override
	public void loadItemCooldowns(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int delayId = rset.getInt("delay_id");
				int useDelay = rset.getInt("use_delay");
				long reuseTime = rset.getLong("reuse_time");
				
				if(reuseTime > System.currentTimeMillis())
					player.addItemCoolDown(delayId, reuseTime, useDelay);
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
		player.getEffectController().broadCastEffects();
	}

	@Override
	public void storeItemCooldowns(final Player player)
	{
		deleteItemCooldowns(player);
		Map<Integer, ItemCooldown> itemCoolDowns = player.getItemCoolDowns();
		
		if(itemCoolDowns == null)
			return;
		
		for(Map.Entry<Integer, ItemCooldown> entry : itemCoolDowns.entrySet())
		{
			final int delayId = entry.getKey();
			final long reuseTime = entry.getValue().getReuseTime();
			final int useDelay = entry.getValue().getUseDelay();
			
			if(reuseTime - System.currentTimeMillis() < 30000)
				continue;
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, delayId);
				stmt.setInt(3, useDelay);
				stmt.setLong(4, reuseTime);
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
	
	private void deleteItemCooldowns(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
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

	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
