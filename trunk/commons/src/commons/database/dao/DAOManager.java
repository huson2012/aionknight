/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package commons.database.dao;

import static commons.database.DatabaseFactory.getDatabaseMajorVersion;
import static commons.database.DatabaseFactory.getDatabaseMinorVersion;
import static commons.database.DatabaseFactory.getDatabaseName;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import commons.database.DatabaseConfig;
import commons.scripting.scriptmanager.ScriptManager;

public class DAOManager
{
	private static final Logger				log		= Logger.getLogger(DAOManager.class);
	private static final Map<String, DAO>	daoMap	= new HashMap<String, DAO>();
	private static ScriptManager			scriptManager;
	public static void init()
	{
		try
		{
			scriptManager = new ScriptManager();
			scriptManager.setGlobalClassListener(new DAOLoader());
			scriptManager.load(DatabaseConfig.DATABASE_SCRIPTCONTEXT_DESCRIPTOR);
		}
		catch(Exception e)
		{
			throw new Error("Can't load database script context: " + DatabaseConfig.DATABASE_SCRIPTCONTEXT_DESCRIPTOR,
				e);
		}

		log.info("Loaded " + daoMap.size() + " DAO implementations.");
	}

	public static void shutdown()
	{
		scriptManager.shutdown();
		daoMap.clear();
		scriptManager = null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends DAO> T getDAO(Class<T> clazz) throws DAONotFoundException
	{

		DAO result = daoMap.get(clazz.getName());

		if(result == null)
		{
			String s = "DAO for class " + clazz.getName() + " not implemented";
			log.error(s);
			throw new DAONotFoundException(s);
		}

		return (T) result;
	}

	public static void registerDAO(Class<? extends DAO> daoClass) throws DAOAlreadyRegisteredException,
		IllegalAccessException, InstantiationException
	{
		DAO dao = daoClass.newInstance();

		if(!dao.supports(getDatabaseName(), getDatabaseMajorVersion(), getDatabaseMinorVersion()))
		{
			return;
		}

		synchronized(DAOManager.class)
		{
			DAO oldDao = daoMap.get(dao.getClassName());
			if(oldDao != null)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("DAO with className ").append(dao.getClassName()).append(" is used by ");
				sb.append(oldDao.getClass().getName()).append(". Can't override with ");
				sb.append(daoClass.getName()).append(".");
				String s = sb.toString();
				log.error(s);
				throw new DAOAlreadyRegisteredException(s);
			}
			else
			{
				daoMap.put(dao.getClassName(), dao);
			}
		}

		if(log.isDebugEnabled())
			log.debug("DAO " + dao.getClassName() + " was successfuly registered.");
	}

	public static void unregisterDAO(Class<? extends DAO> daoClass)
	{
		synchronized(DAOManager.class)
		{
			for(DAO dao : daoMap.values())
			{
				if(dao.getClass() == daoClass)
				{
					daoMap.remove(dao.getClassName());

					if(log.isDebugEnabled())
						log.debug("DAO " + dao.getClassName() + " was successfuly unregistered.");

					break;
				}
			}
		}
	}

	private DAOManager()
	{
	}
}