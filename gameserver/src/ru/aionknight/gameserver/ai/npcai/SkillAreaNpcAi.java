package ru.aionknight.gameserver.ai.npcai;


import ru.aionknight.gameserver.ai.AI;
import ru.aionknight.gameserver.ai.desires.AbstractDesire;
import ru.aionknight.gameserver.ai.events.Event;
import ru.aionknight.gameserver.ai.events.handler.EventHandler;
import ru.aionknight.gameserver.ai.state.AIState;
import ru.aionknight.gameserver.ai.state.handler.StateHandler;
import ru.aionknight.gameserver.model.gameobjects.SkillAreaNpc;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.skill.model.Skill;

/**
 * @author ViAl
 *
 */
public class SkillAreaNpcAi extends NpcAi
{
	public SkillAreaNpcAi()
	{
		/**
		 * Event handlers
		 */
		this.addEventHandler(new RespawnedEventHandler());

		/**
		 * State handlers
		 */
		this.addStateHandler(new ActiveNpcStateHandler());
	}

	public class RespawnedEventHandler implements EventHandler
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

	class ActiveNpcStateHandler extends StateHandler
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
			SkillAreaNpc owner =(SkillAreaNpc) ai.getOwner();

			ai.addDesire(new SkillUseDesire(owner, AIState.ACTIVE.getPriority()));


			if(ai.desireQueueSize() == 0)
				ai.handleEvent(Event.RESPAWNED);
			else
				ai.schedule();
		}
	}

	class SkillUseDesire extends AbstractDesire
	{
		/**
		 * SkillAreaNpc object
		 */
		private SkillAreaNpc	owner;

		/**
		 * 
		 * @param desirePower
		 * @param owner
		 */
		private SkillUseDesire(SkillAreaNpc owner, int desirePower)
		{
			super(desirePower);
			this.owner = owner;
		}

		@Override
		public boolean handleDesire(AI<?> ai)
		{
			owner.getAi().setAiState(AIState.ACTIVE);
			
			if (owner.getActingCreature() == null)
			{
				owner.getLifeStats().reduceHp(10000, owner);
				return false;
			}
			
			Skill skill = SkillEngine.getInstance().getSkill(owner, owner.getSkillId(), 1, owner);
			
			if(skill != null)
				skill.useSkill();
			
			return true;
		}

		@Override
		public int getExecutionInterval()
		{
			//every 3 sec
			return 3;
		}

		@Override
		public void onClear()
		{
			// TODO Auto-generated method stub
		}
	}

}
