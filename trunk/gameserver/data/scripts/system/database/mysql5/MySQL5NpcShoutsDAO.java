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
import gameserver.dao.NpcShoutsDAO;
import gameserver.model.NpcShout;
import gameserver.model.ShoutEventType;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MySQL5NpcShoutsDAO extends NpcShoutsDAO
{

	private static final Logger log = Logger.getLogger(MySQL5NpcShoutsDAO.class);
	
	@Override
	public FastMap<Integer, List<NpcShout>> getShouts()
	{
		final FastMap<Integer, List<NpcShout>> shouts = new FastMap<Integer, List<NpcShout>>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT message_id, npc_id, event, param FROM npc_shouts");
			ResultSet arg0 = stmt.executeQuery();
			while(arg0.next())
			{
				int npcId = arg0.getInt("npc_id");
				int messageId = arg0.getInt("message_id");
				ShoutEventType event = ShoutEventType.valueOf(arg0.getString("event"));
				String param = arg0.getString("param");
				
				List<NpcShout> npcShouts = null;
				if (shouts.containsKey(npcId))
					npcShouts = shouts.get(npcId);
				else
					npcShouts = new ArrayList<NpcShout>();
				npcShouts.add(new NpcShout(messageId, event, param));
				shouts.put(npcId, npcShouts);
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
		return shouts;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}