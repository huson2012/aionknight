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

package gameserver.ai.desires.impl;

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.desires.MoveDesire;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.utils.MathUtil;

public class MoveToTargetDesire extends AbstractDesire implements MoveDesire
{
	private Npc owner;
	private Creature target;
	private float targetOffset;
	
	/**
	 * @param crt 
	 * @param desirePower
	 */
	public MoveToTargetDesire(Npc owner, Creature target, float targetOffset, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		this.target = target;
		this.targetOffset = targetOffset;
	}

	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if (owner == null || owner.getLifeStats().isAlreadyDead())
			return false;
		if(target == null || target.getLifeStats().isAlreadyDead())
			return false;

		if(MathUtil.isInRange(owner, target, targetOffset))
		{
			owner.getMoveController().stop();
			return true;
		}

		owner.getMoveController().followTarget(targetOffset);
		if(!owner.getMoveController().isScheduled())
			owner.getMoveController().schedule();
		
		/** 
		 * MoveToHome if *target* is trying to lure it away from home 
		 * (this will prevent Npcs from going home and heal inevitably) 
		 * and not only if target is too far away 
		 */
		if (owner.getSpawn() != null &&
			MathUtil.getDistance(target, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 125)
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, owner.getLifeStats().getMaxHp());
			ai.setAiState(AIState.MOVINGTOHOME);
			return false;
		}
		
        return owner.getMoveController().getDistanceToTarget() <= 150;
    }

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof MoveToTargetDesire))
			return false;

		MoveToTargetDesire that = (MoveToTargetDesire) o;
		return target.equals(that.target);
	}

	/**
	 * @return the target
	 */
	public Creature getTarget()
	{	
		return target;
	}

	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		owner.getMoveController().stop();
	}
}