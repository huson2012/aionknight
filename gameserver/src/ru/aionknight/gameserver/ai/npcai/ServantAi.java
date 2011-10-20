/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.ai.npcai;


import ru.aionknight.gameserver.ai.AI;
import ru.aionknight.gameserver.ai.desires.AbstractDesire;
import ru.aionknight.gameserver.ai.events.Event;
import ru.aionknight.gameserver.ai.events.handler.EventHandler;
import ru.aionknight.gameserver.ai.state.AIState;
import ru.aionknight.gameserver.ai.state.handler.NoneNpcStateHandler;
import ru.aionknight.gameserver.ai.state.handler.StateHandler;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.gameobjects.Servant;
import ru.aionknight.gameserver.model.gameobjects.VisibleObject;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.skill.model.Skill;


/**
 * @author ATracer
 *
 */
public class ServantAi extends NpcAi
{
	public ServantAi()
	{
		/**
		 * Event handlers
		 */
		this.addEventHandler(new RespawnEventHandler());
		
		/**
		 * State handlers
		 */
		this.addStateHandler(new ActiveServantStateHandler());
		this.addStateHandler(new NoneNpcStateHandler());
	}
	
	public class RespawnEventHandler implements EventHandler
	{
		@Override
		public Event getEvent()
		{
			return Event.RESPAWNED;
		}

		@Override
		public void handleEvent(Event event, AI<?> ai)
		{
			ai.setAiState(AIState.ACTIVE);
			if(!ai.isScheduled())
				ai.analyzeState();
		}

	}
	
	class ActiveServantStateHandler extends StateHandler
	{
		@Override
		public AIState getState()
		{
			return AIState.ACTIVE;
		}

		@Override
		public void handleState(AIState state, AI<?> ai)
		{
			ai.clearDesires();
			Servant owner = (Servant) ai.getOwner();
			Creature servantOwner = owner.getCreator();

			VisibleObject servantOwnerTarget = servantOwner.getTarget();
			if(servantOwnerTarget instanceof Creature)
			{
				ai.addDesire(new ServantSkillUseDesire(owner, (Creature) servantOwnerTarget, AIState.ACTIVE
					.getPriority()));
			}

			if(ai.desireQueueSize() == 0)
				ai.handleEvent(Event.NOTHING_TODO);
			else
				ai.schedule();
		}
	}
	
	class ServantSkillUseDesire extends AbstractDesire
	{
		/**
		 * Trap object
		 */
		private Servant		owner;
		/**
		 * Owner of trap
		 */
		private Creature	target;

		/**
		 * 
		 * @param desirePower
		 * @param owner
		 */
		private ServantSkillUseDesire(Servant owner, Creature target, int desirePower)
		{
			super(desirePower);
			this.owner = owner;
			this.target = target;
		}

		@Override
		public boolean handleDesire(AI<?> ai)
		{		
			if(target == null || target.getLifeStats().isAlreadyDead())
				return true;
			
			if (owner.getActingCreature() == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return false;
			}
			
			if(!owner.getActingCreature().isEnemy(target))
				return false;
			
			if(owner.getSkillId() == 0 && owner.getTarget() instanceof Creature)
				owner.getController().attackTarget((Creature)owner.getTarget());
			else
			{
				Skill skill = SkillEngine.getInstance().getSkill(owner, owner.getSkillId(), 1, target);
				if(skill != null)
				{
					skill.useSkill();
				}
			}
			return true;
		}

		@Override
		public int getExecutionInterval()
		{
			return 4;
		}

		@Override
		public void onClear()
		{
			// TODO Auto-generated method stub
		}
	}
}
