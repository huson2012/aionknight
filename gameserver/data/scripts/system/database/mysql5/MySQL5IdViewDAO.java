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
import gameserver.dao.IdViewDAO;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL5IdViewDAO extends IdViewDAO
{
	private static final Logger	log = Logger.getLogger(MySQL5IdViewDAO.class);
	public static final String SELECT_QUERY = "SELECT `players`.`id` AS `id` from `players` UNION SELECT `itemUniqueId` FROM `inventory` UNION SELECT `id` FROM `legions` UNION SELECT `mailUniqueId` FROM `mail`";
	
	@Override
	public int[] getUsedIDs()
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			con.setReadOnly(true);
			
			PreparedStatement statement = con.prepareStatement(SELECT_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE,	ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for(int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("id");
			}
			statement.close();
			return ids;
		}
		catch(SQLException e)
		{
			log.error("Can't get list of id's from union query", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return new int[0];
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}