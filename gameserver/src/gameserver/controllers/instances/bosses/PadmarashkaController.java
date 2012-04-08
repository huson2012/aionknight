package gameserver.controllers.instances.bosses;

import java.util.ArrayList;
import java.util.List;

import commons.utils.Rnd;

import gameserver.controllers.MonsterController;
import gameserver.controllers.attack.AttackStatus;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.InstanceService;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Effect;
import gameserver.skill.model.Skill;
import gameserver.skill.model.SkillTemplate;
import gameserver.utils.ThreadPoolManager;



public class PadmarashkaController extends MonsterController
{
	List<Npc> miniegg = new ArrayList<Npc>();
	List<Npc> bigegg = new ArrayList<Npc>();
	Npc sumdrakanwiegg;
	Npc sumdrakanfiegg;
	Npc neonateDrakan;
	List<Npc> thunderstorm = new ArrayList<Npc>();
	List<Npc> stoneEnergy = new ArrayList<Npc>();
	Npc votaicColumn;
	@Override
	public void useSkill(final int skillId)
	{
		
			int minieggCount = 0;
			int bigeggCount = 0;
			for(final Npc npc : miniegg)
			{
				if(miniegg == null || npc.getLifeStats().isAlreadyDead())
					continue;
				else
					minieggCount++;
			}
			for(final Npc npc : bigegg)
			{
				if(bigegg == null || npc.getLifeStats().isAlreadyDead())
					continue;
				else
					bigeggCount++;
			}
			if(skillId == 19177 && minieggCount == 0 && bigeggCount == 0)
			{
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 584.13, (float) 284.24, (float)66.75, (byte) 89, true));
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 589.66, (float) 258.82, (float)66.25, (byte) 94, true));
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 591.08, (float) 233.69, (float)66.25, (byte) 36, true));
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 563.72, (float) 197.1, (float)66.125, (byte) 35, true));
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 524.48, (float) 222.8, (float)66.875, (byte) 2, true));
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 522.96, (float) 245.81, (float)66.48, (byte) 3, true));
			miniegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282613, (float) 515.15, (float) 273.51, (float)66.125, (byte) 1, true));
			padmarashkasNurturing();
			{
				getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
					  @Override
					  public boolean run(final Npc npc) 
					  {
						  if(npc.getNpcId() == 282613) 
						    {
							  ThreadPoolManager.getInstance().schedule(new Runnable(){
									@Override
									public void run()
									{
										if (npc.getLifeStats().isAlreadyDead())
											return;
					sumdrakanwiegg =(Npc)InstanceService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 282716, (float) npc.getX()+3, (float) npc.getY()-3, (float)npc.getZ()+2, (byte) 0, true);
					sumdrakanfiegg =(Npc)InstanceService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 282715, (float) npc.getX()+4, (float) npc.getY()-4, (float)npc.getZ()+2, (byte) 0, true);
					npc.getController().die();
									}
								}, 30000);
						   
						    }
						  return true;
					  }
				    	});
			}
			bigegg.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282614, (float) 547.31, (float) 260.38, (float)66.87, (byte) 98, true));
			padmarashkasNurturing();
			{
				getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
					  @Override
					  public boolean run(final Npc npc) 
					  {
						  if(npc.getNpcId() == 282614) 
						    {
							  ThreadPoolManager.getInstance().schedule(new Runnable(){
									@Override
									public void run()
									{
										if (npc.getLifeStats().isAlreadyDead())
											return;
				
				neonateDrakan =(Npc)InstanceService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 281456, (float) npc.getX()+3, (float) npc.getY()-3, (float)npc.getZ()+2, (byte) 0, true);
				npc.getController().die();
									}
								}, 30000);
						   
						    }
						  return true;
					  }
				    	});
			}
			}
		super.useSkill(skillId);
		
	}
		   
   
			   
private void despawnVotaicColumn()
{
	getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
		  @Override
		  public boolean run(final Npc npc) 
		  {
			  if(npc.getNpcId() == 282120) 
			    {
				  npc.getController().delete();
				  return true;
			    }
			    return true;
		      }
	    	});
}

private void padmarashkasNurturing()
{
	getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
		  @Override
		  public boolean run(final Npc npc) 
		  {
			  if(npc.getNpcId() == 282613 || npc.getNpcId() == 282614) 
			    {
				  SkillTemplate st =  DataManager.SKILL_DATA.getSkillTemplate(18727);
				   Effect e = new Effect(npc, npc, st, 1,  st.getEffectsDuration());
				   e.initialize();
				   e.applyEffect();	
				   return true;
			    }
			    return true;
		      }
	    	});
}
private void rockSlide()
{
	getOwner().getKnownList().doOnAllNpcs(new Executor<Npc>(){
		  @Override
		  public boolean run(final Npc npc) 
		  {
			  if(npc.getNpcId() == 282120) 
			    {
				  Skill skill = SkillEngine.getInstance().getSkill(getOwner(),19179,1,npc);
				  skill.useSkill();
			    }
			    return true;
		      }
	    	});
}
	
public void onAttack(final Creature creature, int skillId, TYPE type, int damage, int logId, AttackStatus status, boolean notifyAttackedObservers, boolean sendPacket)
{

	super.onAttack(creature, skillId, type, damage, logId, status, notifyAttackedObservers, sendPacket);
	
	if(Rnd.get(1, 100) < 3)
	{
		int thunderstormCount = 0;
		int stoneEnergyCount = 0;
		for(final Npc npc : thunderstorm)
		{
			if(thunderstorm == null || npc.getLifeStats().isAlreadyDead())
				continue;
			else
				thunderstormCount++;
		}
		for(final Npc npc : stoneEnergy)
		{
			if(thunderstorm == null || npc.getLifeStats().isAlreadyDead())
				continue;
			else
				stoneEnergyCount++;
		}
	if(stoneEnergyCount == 0 && thunderstormCount == 0)
	{
	
	int random = Rnd.get(1, 2);
		if(random == 1)
		   {
			thunderstorm.add((Npc) InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 281453, (float) getOwner().getX()+15, (float) getOwner().getY()-15, (float)getOwner().getZ()+2, (byte) 0, true));
			thunderstorm.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 281453, (float) getOwner().getX()+17, (float) getOwner().getY()-16, (float)getOwner().getZ()+2, (byte) 0, true));
			thunderstorm.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 281453, (float) getOwner().getX()+13, (float) getOwner().getY()-12, (float)getOwner().getZ()+2, (byte) 0, true));
		   }
		if(random == 2)
		   {
		    stoneEnergy.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 281459, (float) getOwner().getX()+13, (float) getOwner().getY()-13, (float)getOwner().getZ()+2, (byte) 0, true));
		    stoneEnergy.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 281459, (float) getOwner().getX()+16, (float) getOwner().getY()-16, (float)getOwner().getZ()+2, (byte) 0, true));
		    stoneEnergy.add((Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 281459, (float) getOwner().getX()+9, (float) getOwner().getY()-12, (float)getOwner().getZ()+2, (byte) 0, true));
		   }
	}
}
	
	int creatureHP = getOwner().getLifeStats().getHpPercentage();
	if(((creatureHP <=25)||(creatureHP <=75 && creatureHP >=60 )||(creatureHP <=85 && creatureHP >=75)) && Rnd.get(1, 100) < 5 )
	{
	int random = Rnd.get(1, 4);
	if(random == 1)
       {

	votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-37, (float) getOwner().getY()-23, (float)getOwner().getZ()+2, (byte) 0, true);
	votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-8, (float) getOwner().getY()-33, (float)getOwner().getZ()+2, (byte) 0, true);
	votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-11, (float) getOwner().getY()+28, (float)getOwner().getZ()+2, (byte) 0, true);
	votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()+53, (float) getOwner().getY()+4, (float)getOwner().getZ()+2, (byte) 0, true);
	getOwner().getMoveController().stopFollowing();
	ThreadPoolManager.getInstance().schedule(new Runnable(){
		@Override
		public void run()
		{
			
			rockSlide();
	        despawnVotaicColumn();
			
		}
	}, 10000);
	
}



if(random == 2)
{
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()+30, (float) getOwner().getY()-35, (float)getOwner().getZ()+2, (byte) 0, true);
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-40, (float) getOwner().getY()-38, (float)getOwner().getZ()+2, (byte) 0, true);
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-47, (float) getOwner().getY()+26, (float)getOwner().getZ()+2, (byte) 0, true);
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()+21, (float) getOwner().getY()+46, (float)getOwner().getZ()+2, (byte) 0, true);
getOwner().getMoveController().stopFollowing();
ThreadPoolManager.getInstance().schedule(new Runnable(){
	@Override
	public void run()
	{
		
		rockSlide();
        despawnVotaicColumn();
		
	}
}, 10000);

}
	   


	
    
if(random == 3)
{
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()+30, (float) getOwner().getY()-35, (float)getOwner().getZ()+2, (byte) 0, true);
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-40, (float) getOwner().getY()-38, (float)getOwner().getZ()+2, (byte) 0, true);
getOwner().getMoveController().stopFollowing();
ThreadPoolManager.getInstance().schedule(new Runnable(){
	@Override
	public void run()
	{
		rockSlide();
        despawnVotaicColumn();
	}
}, 5000);

}




if(random == 4)
{
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()-47, (float) getOwner().getY()+26, (float)getOwner().getZ()+2, (byte) 0, true);
votaicColumn =(Npc)InstanceService.addNewSpawn(getOwner().getWorldId(), getOwner().getInstanceId(), 282120, (float) getOwner().getX()+21, (float) getOwner().getY()+46, (float)getOwner().getZ()+2, (byte) 0, true);
getOwner().getMoveController().stopFollowing();
ThreadPoolManager.getInstance().schedule(new Runnable(){
	@Override
	public void run()
	{
		rockSlide();
        despawnVotaicColumn();
	}
}, 5000);

}

}
}
}



