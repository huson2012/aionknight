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

package gameserver.services;

import gameserver.model.gameobjects.Monster;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.spawn.SpawnEngine;

public class EmpyreanCrucibleInstanceService
{
    protected VisibleObject Recordkeeper[];
    int mapId = 300300000;
    
    public void onGroupReward(Monster monster, PlayerGroup group)
    {
		// Round-1 kill Kamara go round2
        if(monster.getObjectTemplate().getTemplateId() == 217599){
            
            SpawnTemplate Kamara;            
            Kamara = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217592, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Kamara, group.getGroupLeader().getInstanceId());
        }
		// Round-2 kill RM-138c go round3
        if(monster.getObjectTemplate().getTemplateId() == 217592) {
            SpawnTemplate RMA;
            RMA = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217593, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(RMA, group.getGroupLeader().getInstanceId());
        }
		// Round-3 kill RM-1337c go round4
		if(monster.getObjectTemplate().getTemplateId() == 217593) {
            SpawnTemplate RMB;
            RMB = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217527, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(RMB, group.getGroupLeader().getInstanceId());
        }
        // Round-4 kill Tran de Feu go round5
        if(monster.getObjectTemplate().getTemplateId() == 217527) {
            SpawnTemplate TranofFire;
            TranofFire = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217528, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(TranofFire, group.getGroupLeader().getInstanceId());
        }
        
		// Round-5 kill Tran de Vent go round6
        if(monster.getObjectTemplate().getTemplateId() == 217528) {
            SpawnTemplate TranofWind;
            TranofWind = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217750, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(TranofWind, group.getGroupLeader().getInstanceId());
        }
		
        // Round-6 kill Administrateur Arminos go round7
        if(monster.getObjectTemplate().getTemplateId() == 217750) {
            SpawnTemplate AdministratorArminos;
            AdministratorArminos = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217598, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(AdministratorArminos, group.getGroupLeader().getInstanceId());
        }
		
		// Round-7 kill  BР В Р’В Р вЂ™Р’В Р В РІР‚в„ўР вЂ™Р’В Р В Р’В Р В Р вЂ№Р В Р вЂ Р В РІР‚С™Р Р†Р вЂљРЎСљР В Р’В Р вЂ™Р’В Р В Р’В Р В РІР‚в„–Р В Р’В Р В РІР‚В Р В Р’В Р Р†Р вЂљРЎв„ўР В Р вЂ Р В РІР‚С™Р РЋРЎС™Р В Р’В Р вЂ™Р’В Р В РІР‚в„ўР вЂ™Р’В Р В Р’В Р В РІР‚В Р В Р’В Р Р†Р вЂљРЎв„ўР В РІР‚в„ўР вЂ™Р’В¦hР В Р’В Р вЂ™Р’В Р В РІР‚в„ўР вЂ™Р’В Р В Р’В Р В Р вЂ№Р В Р вЂ Р В РІР‚С™Р Р†Р вЂљРЎСљР В Р’В Р вЂ™Р’В Р В Р’В Р В РІР‚в„–Р В Р’В Р В РІР‚В Р В Р’В Р Р†Р вЂљРЎв„ўР В Р вЂ Р В РІР‚С™Р РЋРЎС™Р В Р’В Р вЂ™Р’В Р В РІР‚в„ўР вЂ™Р’В Р В Р’В Р В РІР‚В Р В Р’В Р Р†Р вЂљРЎв„ўР В РІР‚в„ўР вЂ™Р’В¦moth de la Montagne rocheuse go round8
        if(monster.getObjectTemplate().getTemplateId() == 217598) {
            SpawnTemplate Andre;
            Andre = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217594, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Andre, group.getGroupLeader().getInstanceId());
        }
		
		// Round-8 kill Crabe Norris go round9
        if(monster.getObjectTemplate().getTemplateId() == 217594) {
            SpawnTemplate CrabNorris;
            CrabNorris = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217554, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(CrabNorris, group.getGroupLeader().getInstanceId());
        }
		
		// Round-9 kill IngР В Р’В Р вЂ™Р’В Р В РІР‚в„ўР вЂ™Р’В Р В Р’В Р В Р вЂ№Р В Р вЂ Р В РІР‚С™Р Р†Р вЂљРЎСљР В Р’В Р вЂ™Р’В Р В Р’В Р В РІР‚в„–Р В Р’В Р В РІР‚В Р В Р’В Р Р†Р вЂљРЎв„ўР В Р вЂ Р В РІР‚С™Р РЋРЎС™Р В Р’В Р вЂ™Р’В Р В РІР‚в„ўР вЂ™Р’В Р В Р’В Р В РІР‚В Р В Р’В Р Р†Р вЂљРЎв„ўР В РІР‚в„ўР вЂ™Р’В¦nieur Lahulahu et parler a l'assistant administrateur go round10
        if(monster.getObjectTemplate().getTemplateId() == 217554) {
            SpawnTemplate Ingenieur;
            Ingenieur = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 205506, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Ingenieur, group.getGroupLeader().getInstanceId());
        }

		// Round-10 kill Vanktrist fin
        if(monster.getObjectTemplate().getTemplateId() == 217609) {
           SpawnTemplate Vanktrist;
            Vanktrist = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 258133, 346.438f, 324.84f, 96.0909f, (byte)31, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Vanktrist, group.getGroupLeader().getInstanceId());
        }
    }
	
    public static EmpyreanCrucibleInstanceService getInstance()
    {
        return SingletonHolder.instance;
    }
    
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final EmpyreanCrucibleInstanceService instance = new EmpyreanCrucibleInstanceService();
    }
}
