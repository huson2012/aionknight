package gameserver.dao;

import commons.database.dao.DAO;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.Player;



/**
 * @author jjhun
 * 
 */
public abstract class PlayerCmotionListDAO implements DAO
{
	@Override
	public final String getClassName()
	{
		 return PlayerCmotionListDAO.class.getName();
	}

	public abstract CmotionList loadCmotionList(int playerId);
	
	public abstract boolean AddCmotions(Player player);
		
	public abstract void removeCmotion(int playerId, int cmotionId);
	
	public abstract void updateCmotion(Player player, Cmotion cmotion);
	
	public abstract boolean storeCmotions(Player player);
	
	public abstract boolean supports(String databaseName, int majorVersion, int minorVersion);
}
