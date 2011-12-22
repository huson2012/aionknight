/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
		
		/** MoveToHome if *target* is trying to lure it away from home 
		 * (this will prevent Npcs from going home and heal inevitably) 
			and not only if target is too far away **/
		if (owner.getSpawn() != null &&
			MathUtil.getDistance(target, owner.getSpawn().getX(), owner.getSpawn().getY(), owner.getSpawn().getZ()) > 125)
		{
			owner.getLifeStats().increaseHp(TYPE.NATURAL_HP, owner.getLifeStats().getMaxHp());
			ai.setAiState(AIState.MOVINGTOHOME);
			return false;
		}

		if(owner.getMoveController().getDistanceToTarget() > 150)
			return false;
		
		return true;
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