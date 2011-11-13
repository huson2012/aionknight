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
		
		// Round-7 kill  B�h�moth de la Montagne rocheuse go round8
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
		
		// Round-9 kill Ing�nieur Lahulahu et parler a l'assistant administrateur go round10
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