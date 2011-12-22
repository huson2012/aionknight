/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.spawn;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.model.templates.spawn.SpawnTime;
import gameserver.utils.gametime.DayTime;
import gameserver.utils.gametime.GameTimeManager;
import gameserver.world.World;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DayNightSpawnManager
{
	private static Logger log = Logger.getLogger(DayNightSpawnManager.class);
	private final List<SpawnTemplate> daySpawns;
	private final List<SpawnTemplate> nightSpawns;
	private final List<VisibleObject> spawnedObjects;
	private SpawnTime currentSpawnTime = null;

	public static final DayNightSpawnManager getInstance()
	{
		return SingletonHolder.instance;
	}

	private DayNightSpawnManager()
	{
		daySpawns = new ArrayList<SpawnTemplate>();
		nightSpawns = new ArrayList<SpawnTemplate>();
		spawnedObjects = new ArrayList<VisibleObject>();
		log.info("Day/Night spawns manager: initialized");
	}

	public void addSpawnTemplate(SpawnTemplate spawnTemplate)
	{
		if(spawnTemplate.getSpawnGroup().getSpawnTime() == SpawnTime.DAY)
			daySpawns.add(spawnTemplate);
		else
			nightSpawns.add(spawnTemplate);
	}

	private void spawnNpcs(List<SpawnTemplate> spawns)
	{
		for(SpawnTemplate spawnTemplate : spawns)
		{
			Set<Integer> instanceIds = World.getInstance().getWorldMap(spawnTemplate.getWorldId()).getInstanceIds();
			for(Integer instanceId : instanceIds)
			{
				VisibleObject object = SpawnEngine.getInstance().spawnObject(spawnTemplate, instanceId);
				if(object != null)
					spawnedObjects.add(object);
			}
		}
	}

	private void deleteObjects()
	{
		for(VisibleObject object : spawnedObjects)
			object.getController().delete();
		spawnedObjects.clear();
	}

	public void notifyChangeMode()
	{
		deleteObjects();
		DayTime dayTime = GameTimeManager.getGameTime().getDayTime();
		if(dayTime == DayTime.NIGHT && (currentSpawnTime == null || currentSpawnTime == SpawnTime.DAY))
		{
			spawnNpcs(nightSpawns);
			currentSpawnTime = SpawnTime.NIGHT;
			log.info("Day/Night spawn manager: " + spawnedObjects.size() + " night spawned.");

		}
		else if(dayTime != DayTime.NIGHT && (currentSpawnTime == null || currentSpawnTime == SpawnTime.NIGHT))
		{
			spawnNpcs(daySpawns);
			currentSpawnTime = SpawnTime.DAY;
			log.info("Day/Night spawn manager: " + spawnedObjects.size() + " day spawned.");
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DayNightSpawnManager	instance = new DayNightSpawnManager();
	}
}