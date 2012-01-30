/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.dao;

import commons.database.dao.DAO;
import gameserver.model.templates.spawn.SpawnTemplate;
import java.util.Map;

public abstract class SpawnDAO implements DAO
{
	public enum SpawnType {
		SPAWNED(1), DESPAWNED(2), REMOVED(3), ALL(0);
		
		private int type;
		private SpawnType (int type)
		{
			this.type = type;
		}
		
		public int getType ()
		{
			return type;
		}
	}
	
	@Override
	public String getClassName()
	{
		return SpawnDAO.class.getName();
	}
	
	public abstract int addSpawn (int npcId, int adminObjectId, String group, boolean noRespawn, int mapId, float x, float y, float z, byte h, int objectId,int staticid);
	public abstract boolean unSpawnGroup (int adminObjectId, String group);
	public abstract boolean isSpawned (int adminObjectId, String group);
	public abstract int isInDB(int npcId, float x, float y);
	public abstract boolean updateHeading(int spawnId, int heading);
	public abstract boolean setSpawned (int spawnId, int objectId, boolean isSpawned);
	public abstract boolean setGroupSpawned (int adminObjectId, String group, boolean isSpawned);
	public abstract Map<Integer, SpawnTemplate> listSpawns (int adminObjectId, String group, SpawnType type);
	public abstract Map<String, Integer> listSpawnGroups (int adminObjectId);
	public abstract Map<Integer, SpawnTemplate> getAllSpawns();
	public abstract boolean deleteSpawn (int spawnId);
	public abstract boolean deleteSpawnGroup (int adminObjectId, String groupName);
	public abstract int getSpawnObjectId (int spawnId, boolean isSpawned);	
	public abstract SpawnTemplate getSpawnTemplate (int spawnId);
}