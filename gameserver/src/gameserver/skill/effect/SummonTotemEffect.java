package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Totem;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skill.model.Effect;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * @author kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonTotemEffect")
public class SummonTotemEffect extends SummonEffect
{

	@XmlAttribute(name = "skill_id", required = true)
	protected int	skillId;

	@Override
	public void applyEffect(Effect effect)
	{
		//(no skill)
		if(skillId == 0)
			return;
		
		Creature effector = effect.getEffector();
		SpawnEngine spawnEngine = SpawnEngine.getInstance();
		float x = effector.getX();
		float y = effector.getY();
		float z = effector.getZ();
		byte heading = effector.getHeading();
		int worldId = effector.getWorldId();
		int instanceId = effector.getInstanceId();
		
		SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
		final Totem totem = spawnEngine.spawnTotem(spawn, instanceId, effector, skillId);
		totem.getKnownList().doUpdate();
		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				totem.getLifeStats().reduceHp(10000, totem, true);
			}
		}, time * 1000);
	}

	@Override
	public void calculate(Effect effect)
	{
		effect.addSucessEffect(this);
	}
}