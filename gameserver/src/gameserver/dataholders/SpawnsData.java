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

package gameserver.dataholders;

import gameserver.model.templates.spawn.SpawnGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.DayNightSpawnManager;
import gameserver.spawn.SpawnEngine;
import gnu.trove.TIntObjectHashMap;
import org.apache.log4j.Logger;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "spawns")
@XmlAccessorType(XmlAccessType.FIELD)
public class SpawnsData
{
	@XmlElement(name = "spawn")
	protected List<SpawnGroup> spawnGroups;
	private static Logger log = Logger.getLogger(SpawnEngine.class);
	@XmlTransient
	private TIntObjectHashMap<ArrayList<SpawnGroup>> spawnsByMapId = new TIntObjectHashMap<ArrayList<SpawnGroup>>();
	@XmlTransient
	private TIntObjectHashMap<ArrayList<SpawnGroup>> spawnsByNpcID = new TIntObjectHashMap<ArrayList<SpawnGroup>>();
	@XmlTransient
	private TIntObjectHashMap<ArrayList<SpawnGroup>> spawnsByMapIdNew = new TIntObjectHashMap<ArrayList<SpawnGroup>>();
	@XmlTransient
	private int counter = 0;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		spawnsByMapId.clear();
		spawnsByMapIdNew.clear();
		spawnsByNpcID.clear();
		
		for(SpawnGroup spawnGroup : spawnGroups)
		{
			for(SpawnTemplate template : spawnGroup.getObjects())
			{
				template.setSpawnGroup(spawnGroup);
				if (spawnGroup.getSpawnTime() != null) 
				{ 
					DayNightSpawnManager.getInstance().addSpawnTemplate(template); 
				} 
			}
			if (spawnGroup.getSpawnTime() != null)
				continue;
				
			addNewSpawnGroup(spawnGroup, spawnGroup.getMapid(), spawnGroup.getNpcid(), false);

			counter += spawnGroup.getObjects().size();
		}
	}

	public SpawnTemplate getFirstSpawnByNpcId(int npcId)
	{
		List<SpawnGroup> spawnGroups =  spawnsByNpcID.get(npcId);
		if(spawnGroups == null)
			return null;

		for(SpawnGroup spawnGroup : spawnGroups)
		{
			if(spawnGroup.getObjects() != null)
			{
				return spawnGroup.getObjects().get(0);
			}
		}
		return null;
	}
	
	public List<SpawnGroup> getSpawnsForWorld(int worldId)
	{
		return spawnsByMapId.get(worldId);
	}
	
	public List<SpawnGroup> getNewSpawnsForWorld(int worldId)
	{
		return spawnsByMapIdNew.get(worldId);
	}

	public int size()
	{
		return counter;
	}

	public void addNewSpawnGroup(SpawnGroup spawnGroup, int worldId, int npcId, boolean isNew)
	{
		ArrayList<SpawnGroup> mapSpawnGroups = spawnsByMapId.get(worldId);
		if(mapSpawnGroups == null)
		{
			mapSpawnGroups = new ArrayList<SpawnGroup>();
			spawnsByMapId.put(worldId, mapSpawnGroups);
		}
		mapSpawnGroups.add(spawnGroup);

		ArrayList<SpawnGroup> npcIdSpawnGroups = spawnsByNpcID.get(npcId);
		if(npcIdSpawnGroups == null)
		{
			npcIdSpawnGroups = new ArrayList<SpawnGroup>();
			spawnsByNpcID.put(npcId, npcIdSpawnGroups);
		}
		npcIdSpawnGroups.add(spawnGroup);

		if(isNew)
		{
			ArrayList<SpawnGroup> mapNewSpawnGroups = spawnsByMapIdNew.get(worldId);
			if(mapNewSpawnGroups == null)
			{
				mapNewSpawnGroups = new ArrayList<SpawnGroup>();
				spawnsByMapIdNew.put(worldId, mapNewSpawnGroups);
			}
			mapNewSpawnGroups.add(spawnGroup);
		}
	}

	public void removeSpawn(SpawnTemplate spawn)
	{
		if(spawn == null)
		{
			log.error("Tried to remove an object that doesn't exist");
		}
		else
		{
		
			if(spawn.getSpawnGroup().size() > 1)
			{
				spawn.getSpawnGroup().getObjects().remove(spawn);
				return;
			}
		
			List<SpawnGroup> worldSpawns = spawnsByMapId.get(spawn.getWorldId());
			if(worldSpawns != null)
			{
				worldSpawns.remove(spawn.getSpawnGroup());
			}
		
			List<SpawnGroup> worldNewSpawns = spawnsByMapIdNew.get(spawn.getWorldId());
			if(worldNewSpawns != null)
			{
				worldNewSpawns.remove(spawn.getSpawnGroup());
			}
		
			List<SpawnGroup> spawnsByNpc = spawnsByNpcID.get(spawn.getSpawnGroup().getNpcid());
			if(spawnsByNpc != null)
			{
				spawnsByNpc.remove(spawn.getSpawnGroup());
			}
		}
	}
		public List<SpawnGroup> getSpawnGroups()
	{
		if(spawnGroups == null)
			spawnGroups = new ArrayList<SpawnGroup>();
		return spawnGroups;
	}
		public void setSpawns(List<SpawnGroup> spawns)
	{
		this.spawnGroups = spawns;
		afterUnmarshal(null, null);
	}
}