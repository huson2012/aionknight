package ru.aionknight.gameserver.dao;

import org.openaion.commons.database.dao.DAO;

import ru.aionknight.gameserver.model.gameobjects.player.Player;

/**
 * @author Mugen 
 */
public abstract class PlayerMotionDAO implements DAO
{
	/**
	* @param playerId
	*/
	public abstract void insertPlayerMotion(int playerId);

	/**
	* @param learnNinja
	* @param learnHober
	* @param player
	* @return
	*/
	public abstract void updatePlayerMotion(int learnNinja, int learnHober, Player player);

	/**
	* @param learnNinja
	* @param learnHober
	* @param waitingMotion
	* @param runningMotion
	* @param jumpingMotion
	* @param restMotion
	* @param player
	* @return
	*/
	public abstract boolean updatePlayerMotion(int learnNinja, int learnHober, int waitingMotion, int runningMotion, int jumpingMotion, int restMotion, Player player);

	/**
	* @param player
	* @return
	*/
	public abstract void loadPlayerMotion(Player player);

	/*
	* (non-Javadoc)
	* @see org.openaion.commons.database.dao.DAO#getClassName()
	*/
	@Override
	public final String getClassName()
	{
		return PlayerMotionDAO.class.getName();
	}
}
