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

import commons.database.DB;
import commons.database.DatabaseFactory;
import commons.database.IUStH;
import gameserver.dao.PlayerSettingsDAO;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerSettings;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL5PlayerSettingsDAO extends PlayerSettingsDAO 
{
    private static final Logger log = Logger.getLogger(MySQL5PlayerSettingsDAO.class);


    @Override
    public void loadSettings(final Player player) 
	{
        final int playerId = player.getObjectId();
        final PlayerSettings playerSettings = new PlayerSettings();
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM player_settings WHERE player_id = ?");
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int type = resultSet.getInt("settings_type");
                switch (type) {
                    case 0:
                        playerSettings.setUiSettings(resultSet.getBytes("settings"));
                        break;
                    case 1:
                        playerSettings.setShortcuts(resultSet.getBytes("settings"));
                        break;
                    case 2:
                        playerSettings.setDisplay(resultSet.getInt("settings"));
                        break;
                    case 3:
                        playerSettings.setDeny(resultSet.getInt("settings"));
                        break;
                }
            }
            resultSet.close();
            statement.close();
        }
        catch (Exception e) {
            log.fatal("Could not restore PlayerSettings data for player " + playerId + " from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(con);
        }
        playerSettings.setPersistentState(PersistentState.UPDATED);
        player.setPlayerSettings(playerSettings);
    }

    @Override
    public void saveSettings(final Player player) 
	{
        final int playerId = player.getObjectId();

        PlayerSettings playerSettings = player.getPlayerSettings();
        if (playerSettings.getPersistentState() == PersistentState.UPDATED)
            return;

        final byte[] uiSettings = playerSettings.getUiSettings();
        final byte[] shortcuts = playerSettings.getShortcuts();
        final int display = playerSettings.getDisplay();
        final int deny = playerSettings.getDeny();

        if (uiSettings != null) {
            DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() 
			{
                @Override
                public void handleInsertUpdate(PreparedStatement stmt) throws SQLException 
				{
                    stmt.setInt(1, playerId);
                    stmt.setInt(2, 0);
                    stmt.setBytes(3, uiSettings);
                    stmt.execute();
                }
            });
        }

        if (shortcuts != null) {
            DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() 
			{
                @Override
                public void handleInsertUpdate(PreparedStatement stmt) throws SQLException 
				{
                    stmt.setInt(1, playerId);
                    stmt.setInt(2, 1);
                    stmt.setBytes(3, shortcuts);
                    stmt.execute();
                }
            });
        }

        DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() 
		{
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException 
			{
                stmt.setInt(1, playerId);
                stmt.setInt(2, 2);
                stmt.setInt(3, display);
                stmt.execute();
            }
        });

        DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() 
		{
            @Override
            public void handleInsertUpdate(PreparedStatement stmt) throws SQLException 
			{
                stmt.setInt(1, playerId);
                stmt.setInt(2, 3);
                stmt.setInt(3, deny);
                stmt.execute();
            }
        });

    }

	@Override
	public PlayerSettings lauchSettings(final int objId)
	{
	    final PlayerSettings playerSettings = new PlayerSettings();
	    Connection con = null;
	    try
	    {
	        con = DatabaseFactory.getConnection();
	        PreparedStatement statement = con.prepareStatement("SELECT * FROM player_settings WHERE player_id = ?");
	        statement.setInt(1, objId);
	        ResultSet resultSet = statement.executeQuery();
	        while (resultSet.next())
	        {
	            int type = resultSet.getInt("settings_type");
	            switch (type)
	            {
	                case 0:
	                    playerSettings.setUiSettings(resultSet.getBytes("settings"));
	                    break;
	                case 1:
	                    playerSettings.setShortcuts(resultSet.getBytes("settings"));
	                    break;
	                case 2:
	                    playerSettings.setDisplay(resultSet.getInt("settings"));
	                    break;
	                case 3:
	                    playerSettings.setDeny(resultSet.getInt("settings"));
	                    break;
	            }
	        }
	        resultSet.close();
	        statement.close();
	    }
	    catch (Exception e)
	    {
	        log.fatal("Could not restore PlayerSettings data for player " + objId + " from DB: " + e.getMessage(), e);
	    }
	    finally
	    {
	        DatabaseFactory.close(con);
	    }
	    return playerSettings;
	}

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}