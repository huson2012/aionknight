package ru.aionknight.gameserver.services;


import ru.aionknight.gameserver.model.gameobjects.Monster;
import ru.aionknight.gameserver.model.gameobjects.VisibleObject;
import ru.aionknight.gameserver.model.group.PlayerGroup;
import ru.aionknight.gameserver.model.templates.spawn.SpawnTemplate;
import ru.aionknight.gameserver.spawn.SpawnEngine;

//@author Striker

public class EmpyreanCrucibleInstanceService
{
    protected VisibleObject                 Recordkeeper[];
    int mapId = 300300000;
    
    public void onGroupReward(Monster monster, PlayerGroup group)
    {
        // Lorsque que on entre dans la zone faut parler a l'administrateur qui nous tp a la prochaine zone
		
		// round1 kill Kamara go round2
        if(monster.getObjectTemplate().getTemplateId() == 217599){
            
            SpawnTemplate Kamara;            
            Kamara = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217592, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Kamara, group.getGroupLeader().getInstanceId());
        }
		
		// round2 kill RM-138c go round3
        if(monster.getObjectTemplate().getTemplateId() == 217592) {
            SpawnTemplate RMA;
            RMA = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217593, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(RMA, group.getGroupLeader().getInstanceId());
        }
		
		// round3 kill RM-1337c go round4
		if(monster.getObjectTemplate().getTemplateId() == 217593) {
            SpawnTemplate RMB;
            RMB = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217527, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(RMB, group.getGroupLeader().getInstanceId());
        }
		            
        // round4 kill Tran de Feu go round5
        if(monster.getObjectTemplate().getTemplateId() == 217527) {
            SpawnTemplate TranofFire;
            TranofFire = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217528, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(TranofFire, group.getGroupLeader().getInstanceId());
        }
        
		// round5 kill Tran de Vent go round6
        if(monster.getObjectTemplate().getTemplateId() == 217528) {
            SpawnTemplate TranofWind;
            TranofWind = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217750, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(TranofWind, group.getGroupLeader().getInstanceId());
        }
		
        // round6 kill Administrateur Arminos go round7
        if(monster.getObjectTemplate().getTemplateId() == 217750) {
            SpawnTemplate AdministratorArminos;
            AdministratorArminos = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217598, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(AdministratorArminos, group.getGroupLeader().getInstanceId());
        }
		
		// round7 kill  B�h�moth de la Montagne rocheuse go round8
        if(monster.getObjectTemplate().getTemplateId() == 217598) {
            SpawnTemplate Andre;
            Andre = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217594, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Andre, group.getGroupLeader().getInstanceId());
        }
		
		// round8 kill Crabe Norris go round9
        if(monster.getObjectTemplate().getTemplateId() == 217594) {
            SpawnTemplate CrabNorris;
            CrabNorris = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 217554, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(CrabNorris, group.getGroupLeader().getInstanceId());
        }
		
		// round9 kill Ing�nieur Lahulahu et parler a l'assistant administrateur go round10
        if(monster.getObjectTemplate().getTemplateId() == 217554) {
            SpawnTemplate Ingenieur;
            Ingenieur = SpawnEngine.getInstance().addNewSpawn(300300000, group.getGroupLeader().getInstanceId(), 205506, 1625.8f, 148.939f, 126.0f, (byte)15, 0, 0, true);
            SpawnEngine.getInstance().spawnObject(Ingenieur, group.getGroupLeader().getInstanceId());
        }

		// round10 kill Vanktrist fin
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
