package org.openaion.gameserver.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openaion.commons.database.DatabaseFactory;
import org.openaion.gameserver.controllers.SummonController.UnsummonType;
import org.openaion.gameserver.model.EmotionType;
import org.openaion.gameserver.model.Race;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.gameobjects.Monster;
import org.openaion.gameserver.model.gameobjects.Summon;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.group.PlayerGroup;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.network.aion.serverpackets.SM_EMOTION;
import org.openaion.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import org.openaion.gameserver.network.aion.serverpackets.SM_STAGE_STEP_STATUS;
import org.openaion.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import org.openaion.gameserver.utils.PacketSendUtility;
import org.openaion.gameserver.utils.ThreadPoolManager;


/**
 * @author Sarev0k, Abbat
 *
 */
public class EmpyreanCrucibleService
{
	List<VisibleObject> monsterSpawns = new ArrayList<VisibleObject>();
	int currentStage = 1;
	int currentRound = 1;
	int currentRoundNpcCount = 0;
	int pointsReward = 0;
	private PlayerGroup registeredGroup = null;
	static int mapId = 300300000;
	private Map<Player,Integer> registeredPlayers = new HashMap<Player,Integer>();//key = player, value = player state (0 = active, 1 = ready room, 2 = excluded)
	int stage;
	int param;
	int round;
	int type;
	int itemId;
	private final static int[] code = {35464, 36464, 37464, 38464, 8392, 43856, 13784, 49248, 19176, 54640};
	/**
	 ** Array with reward points
	 */
	private final static int[] rewardPoints = {1615, 1000, 722, 1000, 1665, 1285, 4800, 7200, 9665, 2500};
	/**
	 ** Array with type for SM_STAGE_STEP_STATUS
	 */
	private final static int[] typeArray = {1, 1, 1, 1, 3, 4, 6, 7, 9, 10};
	/**
	 ** Array with coordinates when player die
	 */
	private final static float[] dieX = {381.192f, 381.192f, 381.192f, 381.192f, 1261.0006f, 1594.374f, 1816.27f, 1777.57f, 1357.69f, 1751.23f};
	private final static float[] dieY = {348.942f, 348.942f, 348.942f, 348.942f, 831.7126f, 150.303366f, 795.125f, 1726.25f, 1747.89f, 1256.68f};
	private final static float[] dieZ = {96.7476f, 96.7476f, 96.7476f, 96.7476f, 358.60562f, 128.69188f, 470.034f, 304f, 319.336f, 394.238f};
	private final static int[] dieH = {59, 59, 59, 59, 100, 103, 58, 29, 64, 29};
	/**
	 ** Array with coordinates when player change stage or return to stage
	 */
	private final static float[] chsX = {345.25f, 345.25f, 345.25f, 345.25f, 1256.8428f, 1638.0f, 1794.908f, 1775.9249f, 1303.6643f, 1766.1079f};
	private final static float[] chsY = {349.40f, 349.40f, 349.40f, 349.40f, 801.94794f, 134.0f, 811.9936f, 1760.295f, 1732.8148f, 1289.0673f};
	private final static float[] chsZ = {96.09f, 96.09f, 96.09f, 96.09f, 358.60562f, 126.0f, 469.3501f, 303.69543f, 316.09506f, 394.23755f};

	public class MonsterSpawnNode {
		int npcId = 0;
		int flag = 0; //0 = normal, 1 = do not add to kill_list
		float x = 0;
		float y = 0;
		float z = 0;
		byte h = 0;
		MonsterSpawnNode(int npcId, int flag, float x, float y, float z, byte h){
			this.npcId = npcId;
			this.flag = flag;
			this.x = x;
			this.y = y;
			this.z = z;
			this.h = h;
		}
	}


	public void startEvent(PlayerGroup group){
		this.registeredGroup = group;
		
		sendScore2Group();
		for(Player player : registeredGroup.getMembers())
			{
			registeredPlayers.put(player, 0);
			player.setInEmpyrean(true);
			PacketSendUtility.sendPacket(player, new SM_STAGE_STEP_STATUS(2, 35464, 1));
			}
		sendWaves(); 
	}

	//stage -> round
	private void sendWaves()
	{
		//List<MonsterSpawnNode> lstMonster = new ArrayList<MonsterSpawnNode>();
		final int tmpStage = currentStage;
		final int tmpRound = currentRound;
		

		type = typeArray[currentStage-1];
	
		sendMsg2group(2, tmpStage, tmpRound, type);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendWave(getNpcsFromDB(mapId, tmpStage, tmpRound, registeredGroup.getGroupLeader().getCommonData().getRace()));
			}
		}, 5000);
	}
	
	//stage -> complete
	private void sendStageComplete(int stage)
	{
		//List<MonsterSpawnNode> lstMonster = new ArrayList<MonsterSpawnNode>();
		final int tmpStage = currentStage;
		
		for(Player player : registeredGroup.getMembers())
			 {
			//PacketSendUtility.sendPacket(player, new SM_STAGE_STEP_STATUS(2, stage*1000, 0));
			PacketSendUtility.sendPacket(player, new SM_STAGE_STEP_STATUS(2, stage*1000+105, 0));
			}

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendWave(getNpcsFromDB(mapId, tmpStage-1, 10, registeredGroup.getGroupLeader().getCommonData().getRace()));
			}
		}, 2000);
	}

	//stage -> start
	private void sendStageStart()
	{
		//List<MonsterSpawnNode> lstMonster = new ArrayList<MonsterSpawnNode>();
		final int tmpStage = currentStage;
		
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendWave(getNpcsFromDB(mapId, tmpStage, 0, registeredGroup.getGroupLeader().getCommonData().getRace()));
			}
		}, 2000);
	}

	/**
	 * @param i
	 * @param j
	 * @param k
	 */
	private void sendMsg2group(int param, int stage, int round, int type)
	{
		for(Player player : registeredGroup.getMembers()) {
			if(round == 6)
				PacketSendUtility.sendPacket(player, new SM_STAGE_STEP_STATUS(2, code[stage-1]+6, type)); 
			else
				PacketSendUtility.sendPacket(player, new SM_STAGE_STEP_STATUS(2, code[stage-1]+round, type)); 
		}
	}
	private void sendWave(List<MonsterSpawnNode> spawns)
	{  		
		monsterSpawns.clear();
		if(spawns == null){
			//TODO: print error
			return;
		}
		for(MonsterSpawnNode node : spawns)
		{
			//TODO: special rounds
			if(node.flag < 2){
				VisibleObject ob = InstanceService.addNewSpawn(300300000, registeredGroup.getGroupLeader().getInstanceId(), node.npcId, node.x, node.y, node.z, node.h, true);
				if(node.flag == 0)
					monsterSpawns.add(ob);
			}
		}
	}


	/**
	 * @param owner
	 * @param group
	 */
	public void onGroupReward(Monster creature, PlayerGroup group)
	{
		//System.out.println("PRE:\nStage: "+currentStage+"\nRound: "+currentRound);
		//monsterSpawns.remove((VisibleObject)creature);
		sendScore2Group(creature);
		currentRoundNpcCount++;
		if(monsterSpawns.size() > currentRoundNpcCount){
			//TODO: nothing?
			return;
		}
		currentRound++;
		currentRoundNpcCount = 0;
		if(currentRound>5)
		{ 			
			currentRound=1; 
			currentStage++;
			sendStageComplete(currentStage-1);
		return;
		}

		sendWaves();
	//	System.out.println("POS:\nStage: "+currentStage+"\nRound: "+currentRound);
	}
	/**
	 * 
	 */
	private void sendScore2Group(Monster monster)
	{
		int instanceTime = (int) ((registeredGroup.getInstanceStartTime() + 14400000) - System.currentTimeMillis());
		int pointsReward = rewardPoints[currentStage-1];
		int grandTotal = registeredGroup.getGroupInstancePoints() + pointsReward;
		registeredGroup.setGroupInstancePoints(grandTotal);
		for(Player player : registeredGroup.getMembers())
			if(player.getWorldId() == mapId){
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(mapId, instanceTime, registeredGroup, grandTotal, 0, false));

				//for now, we send string since score packet is wrong
				//PacketSendUtility.sendMessage(player, "Points: "+grandTotal);
			}
	}
	private void sendScore2Group()
	{
		int instanceTime = (int) ((registeredGroup.getInstanceStartTime() + 14400000) - System.currentTimeMillis());
		int grandTotal = registeredGroup.getGroupInstancePoints();
		for(Player player : registeredGroup.getMembers()){
			if(player.getWorldId() == mapId){
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(mapId, instanceTime, registeredGroup, grandTotal, 0, false));
				
				//heal all member after sending score
				if(!player.getLifeStats().isAlreadyDead()){
				player.getLifeStats().increaseHp(TYPE.HP, player.getLifeStats().getMaxHp() + 1);
				player.getLifeStats().increaseMp(TYPE.MP, player.getLifeStats().getMaxMp() + 1);
				}
			}
		}
	}
	/**
	 * @param monster
	 * @param groupLeader
	 * @return
	 */

	/**
	 * 
	 */
	public void doReward()
	{	
		int instanceTime = (int) ((registeredGroup.getInstanceStartTime() + 14400000) - System.currentTimeMillis());
		int grandTotal = registeredGroup.getGroupInstancePoints();
		int rewardCount = grandTotal / 68; 
		for(Player player : registeredGroup.getMembers()){
			if(player.getWorldId() == mapId){
				ItemService.addItem(player, 186000130, rewardCount);
					if (player.getCommonData().getRace().getRaceId() == 0)
						itemId = 186000124;
					else itemId = 186000125;
				player.getInventory().removeFromBagByItemId(itemId, player.getInventory().getItemCountByItemId(itemId));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(mapId, instanceTime, registeredGroup, grandTotal, rewardCount, true));
			}
		}
	}

	public void groupUnreg()
	{
		registeredGroup.setEmpyreanCrucible(null);
		registeredGroup = null;
	}

	public static boolean isInEmpyreanCrucible(int worldId)
	{
		if(worldId == mapId)
			return true;

		return false;
	}
	/**
	 * @param player
	 * @param lastAttacker
	 */
	//TODO: atm player is sent to special place, but there is more stuff to take care.
	public void onDie(final Player player, Creature lastAttacker)
	{
		player.getEffectController().removeAllEffects();
		player.getController().cancelCurrentSkill();

		//set player state to trace its steps (for reward)
		//hash map put = get and replace if key already there :)
		registeredPlayers.put(player, 1);

		Summon summon = player.getSummon();
		if(summon != null)
			summon.getController().release(UnsummonType.UNSPECIFIED);

		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DIE);
		player.getObserveController().notifyDeath(player);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				player.getReviveController().skillRevive(false);
				// add switch teleport for ready room here..
				TeleportService.teleportTo(player, mapId, player.getInstanceId(), dieX[currentStage-1], dieY[currentStage-1], dieZ[currentStage-1], dieH[currentStage-1]);

				//check if all players are dead
				checkInstanceEnd();
			}
		}, 5000);
	}

	public void returnStage(final Player player)
	{
			if(player.getWorldId() == mapId){
				TeleportService.teleportTo(player, 300300000, player.getInstanceId(), chsX[currentStage-1], chsY[currentStage-1], chsZ[currentStage-1], 0);
			}			
	}

	/**
	 * used to see if all players are dead and doReward can be called
	 */
	private void checkInstanceEnd()
	{
		// TODO Auto-generated method stub
		for(java.util.Map.Entry<Player, Integer> entrySet : registeredPlayers.entrySet()){
			if(entrySet.getValue() < 1)
				if(entrySet.getKey().isOnline() && entrySet.getKey().getWorldId() == mapId)//only consider player as active if player is still online and not in ready room
				return;																	//TODO: When we implement chests, need to change this because they can return!!
		}
		doReward();
	}	

	/**
	 * @param player
	 */
	public void changeStage()
	{
		//TODO: this switch can be removed.. for now i leave it, maybe can be usefull later - Sarev0k
		//this event is called when currentStage is already incremented
		
		for(Player player : registeredGroup.getMembers()){
			if(player.getLifeStats().isAlreadyDead()){
				player.getReviveController().skillRevive(false);
			}
			if(player.getWorldId() == mapId){
				if(currentStage > 4)
				{TeleportService.teleportTo(player, 300300000, player.getInstanceId(), chsX[currentStage-1], chsY[currentStage-1], chsZ[currentStage-1], 0);}

				PacketSendUtility.sendPacket(player, new SM_STAGE_STEP_STATUS(2, code[currentStage-1], typeArray[currentStage-1]));
			}
		}
			sendStageStart();
 	}
	
		public void changeStart()
	{
			sendWaves();
	}
	
	/**
	 * @param instance
	 */
	private List<MonsterSpawnNode> getNpcsFromDB(int worldId, int stage, int round, Race playerRace)
	{
		Connection con = null;
		List<MonsterSpawnNode> tmpList = new ArrayList<EmpyreanCrucibleService.MonsterSpawnNode>();
		boolean result = false;
		final String LIST_SPAWNS_QUERY = "SELECT * FROM `special_spawns` WHERE `world_id` = ? AND `stage` = ? AND `round` = ? AND (`race` = ? OR `race` = 'ALL')";

		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LIST_SPAWNS_QUERY);
			
			stmt.setInt(1, mapId);
			stmt.setInt(2, stage);
			stmt.setInt(3, round);
			stmt.setString(4, playerRace.toString());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				tmpList.add( new MonsterSpawnNode(rs.getInt("npc_id"), rs.getInt("flag"), rs.getFloat("x"), rs.getFloat("y"), rs.getFloat("z"), rs.getByte("h")));
			}
			stmt.close();
			con.close();
			result = true;
		}
		catch(Exception e)
		{
			Logger.getLogger(EmpyreanCrucibleService.class).error("[ERROR]: Error loading spawns for stage: "+stage+". round: "+round+". race: "+playerRace.toString());
		}finally
		{
			DatabaseFactory.close(con);
		}
		if(result)
			return tmpList;
		else
			return null;
	}
}
