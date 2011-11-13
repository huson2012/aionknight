/**
 * This file is part of the requirements for the Illusion Gate Skill.
 * Code References from ATracer's SummonTrapEffect.java of Aion-Knight
 */
package gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.GroupGate;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skill.model.Effect;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.ThreadPoolManager;


/**
 * @author LokiReborn
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonGroupGateEffect")
public class SummonGroupGateEffect extends SummonEffect
{
	@Override
	public void applyEffect(Effect effect)
	{
		final Creature effector = effect.getEffector();
		SpawnEngine spawnEngine = SpawnEngine.getInstance();
		float x = effector.getX();
		float y = effector.getY();
		float z = effector.getZ();
		byte heading = effector.getHeading();
		int worldId = effector.getWorldId();
		int instanceId = effector.getInstanceId();

		SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
		final GroupGate groupgate = spawnEngine.spawnGroupGate(spawn, instanceId, effector);

		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				groupgate.getController().onDelete();
			}
		}, time * 1000);
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect);
	}
}
