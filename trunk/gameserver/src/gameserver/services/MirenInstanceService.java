package gameserver.services;

import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.utils.PacketSendUtility;
import gameserver.spawn.SpawnEngine;


public class MirenInstanceService
{
	public void onGroupReward(Monster monster, PlayerGroup group)
	{
        //Countdown to kill the Boss Awakened Miren Prince and chests appear
		if(monster.getObjectTemplate().getTemplateId() == 215222)
		{
		    long timeRemain = (group.getInstanceStartTime() + 600000) - System.currentTimeMillis();
            
            if(timeRemain > 1)
			{
			//When kills Awakened Miren Prince spawns chests
            SpawnTemplate chest1, chest2, chest3, chest4, chest5, chest6, chest7, chest8, chest9, chest10, chest11;
            
            chest1 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 478f, 815f, 199f, (byte)10, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest1, group.getGroupLeader().getInstanceId());

            chest2 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 471f, 834f, 199f, (byte)3, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest2, group.getGroupLeader().getInstanceId());

            chest3 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 471f, 854f, 199f, (byte)117, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest3, group.getGroupLeader().getInstanceId());

            chest4 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 478f, 873f, 199f, (byte)110, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest4, group.getGroupLeader().getInstanceId());

            chest5 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 490f, 888f, 199f, (byte)103, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest5, group.getGroupLeader().getInstanceId());

            chest6 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 508f, 899f, 199f, (byte)96, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest6, group.getGroupLeader().getInstanceId());

            chest7 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 527f, 902f, 199f, (byte)90, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest7, group.getGroupLeader().getInstanceId());

            chest8 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 547f, 899f, 199f, (byte)84, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest8, group.getGroupLeader().getInstanceId());

            chest9 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 564f, 889f, 199f, (byte)77, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest9, group.getGroupLeader().getInstanceId());

            chest10 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 577f, 874f, 199f, (byte)70, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest10, group.getGroupLeader().getInstanceId());

            chest11 = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 214804, 584f, 855f, 199f, (byte)63, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(chest11, group.getGroupLeader().getInstanceId());
            }
            else {
                for(Player member : group.getMembers())
                {
                    //TODO: Find retail message
                    PacketSendUtility.sendMessage(member, "Your party is very slow. No chests reward for instance.");
                }
            }
		}

        //When kills 11 Sealed Gold Chest spawn Ancient Treasure Box
        if(monster.getObjectTemplate().getTemplateId() == 214804) {
            
            group.setInstanceKills(group.getInstanceKills() + 1);
            
            if (group.getInstanceKills() == 11){
                SpawnTemplate goldchest;
                goldchest = SpawnEngine.getInstance().addNewSpawn(300130000, group.getGroupLeader().getInstanceId(), 700559, 585f, 835f, 199f, (byte)57, 0, 0, true);
                SpawnEngine.getInstance().spawnObject(goldchest, group.getGroupLeader().getInstanceId());
            }
        }
	}
	
	public static MirenInstanceService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final MirenInstanceService	instance	= new MirenInstanceService();
	}
}
