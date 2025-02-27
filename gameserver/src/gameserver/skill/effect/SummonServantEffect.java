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
package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Servant;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skill.model.Effect;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.ThreadPoolManager;
import org.apache.log4j.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;



/**

 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonServantEffect")
public class SummonServantEffect extends SummonEffect
{

	@XmlAttribute(name = "skill_id", required = true)
	protected int	skillId;
	@XmlAttribute(name = "hp_ratio", required = true)
	protected float	hpRatio;
	@XmlAttribute(name = "count", required = false)
	protected int 	count;

	@Override
	public void applyEffect(Effect effect)
	{
		Creature effector = effect.getEffector();
		SpawnEngine spawnEngine = SpawnEngine.getInstance();
		float x = effector.getX();
		float y = effector.getY();
		float z = effector.getZ();
		byte heading = effector.getHeading();
		int worldId = effector.getWorldId();
		int instanceId = effector.getInstanceId();
		
		final Creature target = (Creature)effector.getTarget();
		
		if(target == null)
		{
			//hack!!!
			Logger.getLogger(SummonServantEffect.class).warn("Servant trying to attack null target!!");
			return;
		}
		final SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
		final Servant servant = spawnEngine.spawnServant(spawn, instanceId, effector, skillId, hpRatio);
		ThreadPoolManager.getInstance().schedule(new Runnable(){

				@Override
				public void run()
				{
					servant.getLifeStats().reduceHp(10000, servant, true);
				}
			}, 30000);
			target.getAggroList().addHate(effector, 50);
	}

	@Override
	public void calculate(Effect effect)
	{
		effect.addSucessEffect(this);
	}
}
