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

package gameserver.ai.npcai;

import gameserver.ai.AI;
import gameserver.ai.desires.AbstractDesire;
import gameserver.ai.events.Event;
import gameserver.ai.events.handler.EventHandler;
import gameserver.ai.state.AIState;
import gameserver.ai.state.handler.NoneNpcStateHandler;
import gameserver.ai.state.handler.StateHandler;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Skill;
import gameserver.utils.MathUtil;
import gameserver.utils.ThreadPoolManager;

public class TrapAi extends NpcAi
{
	public TrapAi()
	{
		/**
		 * Event handlers
		 */
		this.addEventHandler(new SeeObjectEventHandler());

		/**
		 * State handlers
		 */
		this.addStateHandler(new ActiveTrapStateHandler());
		this.addStateHandler(new NoneNpcStateHandler());
	}

	public class SeeObjectEventHandler implements EventHandler
	{
		@Override
		public Event getEvent()
		{
			return Event.SEE_CREATURE;
		}

		@Override
		public void handleEvent(Event event, AI<?> ai)
		{
			ai.setAiState(AIState.ACTIVE);
			if(!ai.isScheduled())
				ai.analyzeState();
		}
	}

	class ActiveTrapStateHandler extends StateHandler
	{
		@Override
		public AIState getState()
		{
			return AIState.ACTIVE;
		}

		@Override
		public void handleState(AIState state, final AI<?> ai)
		{
			ai.clearDesires();
			final Trap owner = (Trap) ai.getOwner();
			final Creature trapCreator = owner.getCreator();			
			
			if (owner.getActingCreature() == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return;
			}
			
			owner.getKnownList().doOnAllObjects(new Executor<AionObject>(){
				@Override
				public boolean run (AionObject obj)
				{
					if (obj instanceof VisibleObject && trapCreator.isEnemy((VisibleObject)obj))
					{
						ai.addDesire(new TrapExplodeDesire(owner, trapCreator, AIState.ACTIVE.getPriority()));
						return false;
					}
					return true;
				}
			}, true);
			
			if(ai.desireQueueSize() == 0)
				ai.handleEvent(Event.NOTHING_TODO);
			else
				ai.schedule();
		}
	}

	class TrapExplodeDesire extends AbstractDesire
	{
		/**
		 * Trap object
		 */
		private Trap owner;
		
		/**
		 * Owner of trap
		 */
		private Creature creator;

		/**
		 * @param desirePower
		 * @param owner
		 */
		private TrapExplodeDesire(Trap owner, Creature creator, int desirePower)
		{
			super(desirePower);
			this.owner = owner;
			this.creator = creator;
		}

		@Override
		public boolean handleDesire(AI<?> ai)
		{
			if (creator == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return false;
			}
			owner.getKnownList().doOnAllObjects(new Executor<AionObject>(){
				@Override
				public boolean run (AionObject visibleObject)
				{
					if(visibleObject == null)
						return true;

					if(visibleObject instanceof Creature)
					{
						Creature creature = (Creature) visibleObject;

						if(creature.getLifeStats() != null && !creature.getLifeStats().isAlreadyDead()
							&& MathUtil.isIn3dRange(owner, creature, owner.getAggroRange()))
						{
							if(!creator.isEnemy(creature))
								return true;

							owner.getAi().setAiState(AIState.NONE);

							int skillId = owner.getSkillId();
							Skill skill = SkillEngine.getInstance().getSkill(owner, skillId, 1, creature);
							skill.useSkill();
							ThreadPoolManager.getInstance().schedule(new Runnable() 
							{
								public void run() 
								{
									owner.getController().onDespawn(true);
								}
							}, skill.getSkillTemplate().getDuration() + 1000);
							return false;
						}
					}
					
					return true;
				}
			}, true);
			
			return true;
		}

		@Override
		public int getExecutionInterval()
		{
			return 2;
		}

		@Override
		public void onClear()
		{
			// TODO: Auto-generated method stub
		}
	}
}