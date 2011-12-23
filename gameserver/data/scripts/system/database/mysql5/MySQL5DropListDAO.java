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