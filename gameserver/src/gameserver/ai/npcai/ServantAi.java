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
package gameserver.ai.npcai;


import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.events.Event;
import gameserver.ai.events.handler.EventHandler;
import gameserver.ai.state.AIState;
import gameserver.ai.state.handler.NoneNpcStateHandler;
import gameserver.ai.state.handler.StateHandler;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Servant;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Skill;


/**

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
