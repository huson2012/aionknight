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
import gameserver.ai.events.Event;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Kisk;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.siege.FortressGate;
import gameserver.model.templates.stats.NpcRank;
import gameserver.utils.MathUtil;

/**
 * This class indicates that character wants to attack somebody
 */
public class AttackDesire extends AbstractDesire
{
	private int attackNotPossibleCounter;
	private int maxAtkTry		= 6;
	private int attackCounter 	= 1;
	/**
	 * Target of this desire
	 */
	protected Creature	target;
	
	protected Npc owner;

	/**
	 * Creates new attack desire, target can't be changed
	 * 
	 * @param npc
	 *           The Npc that's attacking
	 * @param target
	 * 			  whom to attack
	 * @param desirePower
	 *           initial attack power
	 */
	public AttackDesire(Npc npc, Creature target, int desirePower)
	{
		super(desirePower);
		this.target = target;
		this.owner = npc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean handleDesire(AI<?> ai)
	{
		if(target == null || target.getLifeStats().isAlreadyDead() || target.getLifeStats().getCurrentHp() <= 0 || 
			owner instanceof FortressGate || owner instanceof Kisk)
		{
			//TODO lower hate and not reset
			owner.getAggroList().stopHating(target);
			owner.getAggroList().remove(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}
		
		double distance = MathUtil.getDistance(owner.getX(), owner.getY(), owner.getZ(), target.getX(), target.getY(), target.getZ());
		
		if(distance > 50)
		{
			owner.getAggroList().stopHating(target);
			owner.getAggroList().remove(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}

		
		if(target.getVisualState() != 0 && owner instanceof Npc)
		{
			NpcRank npcrank = owner.getObjectTemplate().getRank();
			/** 3 currently GM invis
			 * This will only exclude elites from hide1
			 */
			if(target.getVisualState() == 3 || (npcrank != NpcRank.LEGENDARY && npcrank != NpcRank.HERO &&
			(target.getVisualState() != 1 || npcrank != NpcRank.ELITE)))
			{
				owner.getAggroList().stopHating(target);
				owner.getController().cancelCurrentSkill();//prevent npc from ending cast of skill
				owner.getAggroList().remove(target);
				owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
				return false;
			}
		}

		attackCounter++;
		
		if(attackCounter % 2 == 0)
		{
			if(!owner.getAggroList().isMostHated(target))
			{
				owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
				return false;
			}
		}

		//TODO: We should use isIn3dRange but should be implemented after we have leaks fixed for performance issues
		if((MathUtil.isIn3dRange(owner, target, (float) owner.getGameStats().getCurrentStat(StatEnum.ATTACK_RANGE)/1000f + 1.5f)) ||
			(distance * 1000 <= owner.getGameStats().getCurrentStat(StatEnum.SPEED) && owner.canPerformMove()))// Added 1.5 as tolerance
		{
			owner.getController().attackTarget(target);
			attackNotPossibleCounter = 0;
		}
		else
			attackNotPossibleCounter++;
			
		// Instance mobs take MUCH more time to loose aggro
		if(owner.isInInstance())
			maxAtkTry = 20;
		
		if(attackNotPossibleCounter > maxAtkTry)
		{
			owner.getAggroList().stopHating(target);
			owner.getAggroList().remove(target);
			owner.getAi().handleEvent(Event.TIRED_ATTACKING_TARGET);
			return false;
		}	
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof AttackDesire))
			return false;

		AttackDesire that = (AttackDesire) o;

		return target.equals(that.target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return target.hashCode();
	}

	/**
	 * Returns target of this desire
	 * 
	 * @return target of this desire
	 */
	public Creature getTarget()
	{
		return target;
	}

	@Override
	public int getExecutionInterval()
	{
		return 2;
	}

	@Override
	public void onClear()
	{
		owner.unsetState(CreatureState.WEAPON_EQUIPPED);
	}
	
}