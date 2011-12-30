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

package gameserver.services;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.instances.EmpyreanCrucible;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_ACADEMY_BOOTCAMP_STAGE;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.world.World;

import java.util.List;
import commons.utils.Rnd;

public class CrucibleChallengeService
{
	private SpawnLocation[] spawnCoords = { new SpawnLocation(330f, 360f, 1651f, 1676f, 95.5f), // 1 �����
			new SpawnLocation(1769f, 1797f, 780f, 811f, 469.5f), // 2 �����
			new SpawnLocation(1286f, 1332f, 1708f, 1753f, 316f), // 3 �����

	};

	private SpawnLocation[] readyRooms = { new SpawnLocation(379f, 380f, 349f, 351f, 96.8f), // 1 �����
			new SpawnLocation(1259f, 1261f, 827f, 829f, 358.6f), // 2, 3 �����
			new SpawnLocation(1815f, 1817f, 795f, 797f, 470.0f), // 4 �����
	};

	public SpawnLocation getSpawnLocation(int stage)
	{
		stage -= 1;

		if(stage < 0)
			stage = 0;

		if(spawnCoords[stage] != null)
			return spawnCoords[stage];
		else
			return new SpawnLocation(330f, 360f, 330f, 360f, 96f);
	}

	public SpawnLocation getReadyRoomLocation(int stage)
	{
		switch(stage)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				return readyRooms[0];

			case 5:
			case 6:
				return readyRooms[1];
			case 7:
				return readyRooms[2];
			case 8:
				return readyRooms[3];
			case 9:
				return readyRooms[4];
			case 10:
				return readyRooms[5];

			default:
				return readyRooms[0];
		}
	}

	private static class stageSpawnTask implements Runnable
	{
		private int instanceId;
		private List<StageSpawn> spawns;
		private int stage;
		private int round;
		private boolean finalSpawn;

		public stageSpawnTask(int instanceId, List<StageSpawn> spawns, int stage, int round, boolean finalSpawn)
		{
			this.instanceId = instanceId;
			this.spawns = spawns;
			this.stage = stage;
			this.round = round;
			this.finalSpawn = finalSpawn;
		}

		@Override
		public void run()
		{
			EmpyreanCrucible arena = (EmpyreanCrucible) World.getInstance().getWorldMap(300300000).getWorldMapInstanceById(instanceId);

			if(arena == null)
				return;

			arena.setStageRound(stage, round);

			arena.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					PacketSendUtility.sendPacket(player, new SM_ACADEMY_BOOTCAMP_STAGE(stage, round, false));
					return true;
				}
			}, true);

			SpawnLocation spawnLocation = CrucibleChallengeService.getInstance().getSpawnLocation(stage);
			//log.info("Spawning Bootcamp stage " + stage + " round " + round + " for group " + group.getGroupId() + " at LOC:" + spawnLocation.getMinCoordX().intValue() + " " + spawnLocation.getMinCoordY().intValue());
			for(StageSpawn stageSpawn : spawns)
			{
				for(int i = 0; i < stageSpawn.getCount(); i++)
				{
					float x = Rnd.get(spawnLocation.getMinCoordX().intValue(), spawnLocation.getMaxCoordX().intValue());
					float y = Rnd.get(spawnLocation.getMinCoordY().intValue(), spawnLocation.getMaxCoordY().intValue());
					float z = spawnLocation.getZ();
					byte h = (byte) Rnd.get(0, 120);
					//log.info("Spawning " + stageSpawn.getObjId() + " " + x + " " + y + " " + z + " instance " + group.getGroupLeader().getInstanceId());
					SpawnTemplate spawn;
					spawn = SpawnEngine.getInstance().addNewSpawn(300300000, arena.getInstanceId(), stageSpawn.getObjId(), x, y, z, h, 0, 0, true);
					SpawnEngine.getInstance().spawnObject(spawn, arena.getInstanceId());
				}
			}

			for(StageSpawn spawn : spawns)
			{
				arena.addSpawnedCount(spawn.getCount());
			}

			spawns.clear();
			spawns = null;

			if(finalSpawn)
				arena.setStageAllSpawned(true);
		}
	}

	private class StageSpawn
	{
		private int objId = 0;
		private int count = 0;

		public StageSpawn(int objId, int count)
		{
			this.objId = objId;
			this.count = count;
		}

		public int getObjId()
		{
			return objId;
		}

		public int getCount()
		{
			return count;
		}
	}

	private class SpawnLocation
	{
		private Float minCoordX;
		private Float maxCoordX;
		private Float minCoordY;
		private Float maxCoordY;
		private Float z;

		public SpawnLocation(float minX, float maxX, float minY, float maxY, float z)
		{
			this.minCoordX = minX;
			this.maxCoordX = maxX;
			this.minCoordY = minY;
			this.maxCoordY = maxY;
			this.z = z;
		}

		public Float getMinCoordX()
		{
			return minCoordX;
		}

		public Float getMaxCoordX()
		{
			return maxCoordX;
		}

		public Float getMinCoordY()
		{
			return minCoordY;
		}

		public Float getMaxCoordY()
		{
			return maxCoordY;
		}

		public Float getCenterCoordX()
		{
			return (minCoordX + maxCoordX) / 2f;
		}

		public Float getCenterCoordY()
		{
			return (minCoordY + maxCoordY) / 2f;
		}

		public Float getZ()
		{
			return z;
		}
	}

	public static boolean isCrucibleChallenge(int mapId)
	{
		return mapId == 300320000;
	}

	public static CrucibleChallengeService getInstance()
	{
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final CrucibleChallengeService instance = new CrucibleChallengeService();
	}
}