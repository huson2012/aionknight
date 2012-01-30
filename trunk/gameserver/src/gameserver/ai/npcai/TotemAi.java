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
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Totem;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Skill;

public class TotemAi extends NpcAi
{
	public TotemAi()
	{
		/**
		 * Event handlers
		 */
		this.addEventHandler(new RespawnEventHandler());
		this.addEventHandler(new AttackedEventHandler());
			
		/**
		 * State handlers
		 */
		this.addStateHandler(new ActiveTotemStateHandler());
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
	
	class AttackedEventHandler implements EventHandler
	{
		@Override
		public Event getEvent()
		{
			return Event.ATTACKED;
		}

		@Override
		public void handleEvent(Event event, AI<?> ai)
		{
			ai.setAiState(AIState.ACTIVE);
			if(!ai.isScheduled())
				ai.analyzeState();
		}
	}
	
	class ActiveTotemStateHandler extends StateHandler
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
			Totem owner = (Totem) ai.getOwner();
			Creature totemOwner = owner.getCreator();
			
			Creature target = null;
			
			if (totemOwner instanceof Player)
			{
				target = totemOwner;
			}

			if (target != null)
			{
				ai.addDesire(new TotemSkillUseDesire(owner, (Creature) target, AIState.ACTIVE
					.getPriority()));
			}
			
			
			if(ai.desireQueueSize() == 0)
			{
				ai.handleEvent(Event.NOTHING_TODO);
			}
			else
			{
				ai.schedule();
			}
		}
	}
	
	class TotemSkillUseDesire extends AbstractDesire
	{
		/**
		 * Totem object
		 */
		private Totem owner;
		
		/**
		 * Owner of totem
		 */
		private Creature target;

		/**
		 * @param desirePower
		 * @param owner
		 */
		private TotemSkillUseDesire(Totem owner, Creature target, int desirePower)
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
			
			if(owner.getActingCreature().isEnemy(target))
				return false;
			
			if(owner.getSkillId() == 0)
				return false;
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
			// Interval 3 sec.
			return 3;
		}

		@Override
		public void onClear()
		{
			// TODO: Auto-generated method stub
		}
	}
}