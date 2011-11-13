package loginserver.dao;

import java.util.Map;
import loginserver.GameServerInfo;
import commons.database.dao.DAO;

public abstract class GameServersDAO implements DAO
{
	/**
	 * Returns all gameservers from database.
	 * @return all gameservers from database.
	 */
	public abstract Map<Byte, GameServerInfo> getAllGameServers();	
	public abstract void writeGameServerStatus(GameServerInfo gsi);

	/**
	 * Returns class name that will be uses as unique identifier for all DAO classes
	 * @return class name
	 */
	@Override
	public final String getClassName()
	{
		return GameServersDAO.class.getName();
	}
}