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