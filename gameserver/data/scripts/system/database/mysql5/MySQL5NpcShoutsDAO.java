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