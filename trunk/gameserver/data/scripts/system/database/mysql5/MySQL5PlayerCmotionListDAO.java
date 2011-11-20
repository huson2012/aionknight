package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;
import gameserver.dao.PlayerCmotionListDAO;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;

/**
 * @author jjhun
 * 
 */
public class MySQL5PlayerCmotionListDAO extends PlayerCmotionListDAO 
{
    private static final String LOAD_CMOTION_QUERY   = "SELECT `cmotion_id`, `cmotion_active`, `cmotion_expires_time`, `cmotion_creation_time` FROM `player_cmotions` WHERE `player_id` = ?";
    private static final String INSERT_CMOTION_QUERY = "INSERT INTO `player_cmotions`(`player_id`, `cmotion_id`, `cmotion_active`, `cmotion_expires_time`, `cmotion_creation_time`) VALUES (?,?,?,?,?)";
    private static final String DELETE_CMOTION_QUERY = "DELETE FROM `player_cmotions` WHERE `player_id` = ? AND `cmotion_id` = ?";
    private static final String UPDATE_CMOTION_QUERY = "UPDATE `player_cmotions` SET `cmotion_active` = ? WHERE `player_id` = ? AND `cmotion_id` = ?";
    private static final String CHECK_CMOTION_QUERY  = "SELECT `cmotion_id` FROM `player_cmotions` WHERE `player_id`= ? AND `cmotion_id`= ?";
    private static final Logger log                  = Logger.getLogger(MySQL5PlayerCmotionListDAO.class);

    @Override
    public CmotionList loadCmotionList(int playerId) 
    {
        CmotionList cmotionList = new CmotionList();

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(LOAD_CMOTION_QUERY);
            stmt.setInt(1, playerId);
            ResultSet rset = stmt.executeQuery();
            while (rset.next()) 
            {
                int id = rset.getInt("cmotion_id");
                boolean isActive = rset.getBoolean("cmotion_active");
                long cmotion_expires_time = rset.getLong("cmotion_expires_time");
                long cmotion_creation_time = rset.getTimestamp("cmotion_creation_time").getTime();

                cmotionList.add(id, isActive, cmotion_expires_time, cmotion_creation_time);
            }
            rset.close();
            stmt.close();
        } 
        
        catch (Exception e)       
        {
            log.error(e);
        } 
        
        finally 
        {
            DatabaseFactory.close(con);
        }

        return cmotionList;
    }
    
	@Override
	public boolean AddCmotions(Player player)
	{
		int playerId = player.getObjectId();

		if(player.getCmotionList() != null)
		{
			for(Cmotion cmotion : player.getCmotionList().getCmotions())
			{
				Connection con = null;
				try
				{
					con = DatabaseFactory.getConnection();
					PreparedStatement stmt = con.prepareStatement(CHECK_CMOTION_QUERY);
					stmt.setInt(1, playerId);
					stmt.setInt(2, cmotion.getCmotionId());
					ResultSet rset = stmt.executeQuery();
					if (!rset.next())
					{
						Connection con2 = null;
						try
						{
							con2 = DatabaseFactory.getConnection();
							PreparedStatement stmt2 = con2.prepareStatement(INSERT_CMOTION_QUERY);
							stmt2.setInt(1, playerId);
							stmt2.setInt(2, cmotion.getCmotionId());
							stmt2.setBoolean(3, cmotion.getActive());
							stmt2.setLong(4, cmotion.getCmotionExpiresTime());
							stmt2.setTimestamp(5, new Timestamp(cmotion.getCmotionCreationTime()));
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
		}
		return true;
	}   

    @Override
    public void removeCmotion(int playerId, int cmotionId)
    {
        Connection con = null;
        try
        {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_CMOTION_QUERY);
            stmt.setInt(1, playerId);
            stmt.setInt(2, cmotionId);
            stmt.execute();
            stmt.close();
        } 
        
        catch (Exception e) 
        {
            log.error(e);
        }
        
        finally 
        {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public void updateCmotion(Player player, Cmotion cmotion) 
    {
        Connection con = null;
        try 
        {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(UPDATE_CMOTION_QUERY);
            stmt.setBoolean(1, cmotion.getActive());
            stmt.setInt(2, player.getObjectId());
            stmt.setInt(3, cmotion.getCmotionId());
            stmt.execute();
            stmt.close();
        } 
        
        catch (Exception e) 
        {
            log.error(e);
        } 
        
        finally 
        {
            DatabaseFactory.close(con);
        }
    }

    @Override
    public boolean storeCmotions(Player player) 
    {
        if (player.getCmotionList() != null) 
        {
            for (Cmotion cmotion : player.getCmotionList().getCmotions()) 
            {
                updateCmotion(player, cmotion);
            }
        }
        return true;
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }
}
