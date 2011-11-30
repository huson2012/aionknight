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

package gameserver.ai;

import gameserver.ai.desires.Desire;
import gameserver.ai.desires.DesireQueue;
import gameserver.ai.desires.impl.CounterBasedDesireFilter;
import gameserver.ai.desires.impl.GeneralDesireIteratorHandler;
import gameserver.ai.events.Event;
import gameserver.ai.events.handler.EventHandler;
import gameserver.ai.npcai.DummyAi;
import gameserver.ai.state.AIState;
import gameserver.ai.state.handler.StateHandler;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.ThreadPoolManager;
import javolution.util.FastMap;

import java.util.Map;
import java.util.concurrent.Future;

public abstract class AI<T extends Creature> implements Runnable
{	

	private static final DummyAi dummyAi = new DummyAi();
	protected Map<Event, EventHandler> eventHandlers = new FastMap<Event, EventHandler>();
	protected Map<AIState, StateHandler> stateHandlers = new FastMap<AIState, StateHandler>();
	protected DesireQueue desireQueue = new DesireQueue();
	protected Creature owner;
	protected AIState aiState = AIState.NONE;
	protected boolean isStateChanged = false;
	private Future<?> aiTask;	
	public void handleEvent(Event event)
	{

	}

	public void handleTalk(Player player)
	{
		
	}

	public Creature getOwner()
	{
		return owner;
	}

	public void setOwner(Creature owner)
	{
		this.owner = owner;
	}

	public AIState getAiState()
	{
		return aiState;
	}

	protected void addEventHandler(EventHandler eventHandler)
	{
		this.eventHandlers.put(eventHandler.getEvent(), eventHandler);
	}
	
	public void clearEventHandler()
	{
		this.eventHandlers.clear();
	}

	protected void addStateHandler(StateHandler stateHandler)
	{
		this.stateHandlers.put(stateHandler.getState(), stateHandler);
	}
	
	public void clearStateHandler()
	{
		this.stateHandlers.clear();
	}

	public void setAiState(AIState aiState)
	{
		if(this.aiState != aiState)
		{
			this.aiState = aiState;
			isStateChanged = true;
		}
	}

	public void analyzeState()
	{
		isStateChanged = false;
		StateHandler stateHandler = stateHandlers.get(aiState);
		if(stateHandler != null)
			stateHandler.handleState(aiState, this);
	}
	
	@Override
	public void run()
	{
		desireQueue.iterateDesires(new GeneralDesireIteratorHandler(this), new CounterBasedDesireFilter());
		if(desireQueue.isEmpty() || isStateChanged)
		{
			analyzeState();
		}
	}

	public void schedule()
	{
		if(!isScheduled())
		{
			aiTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 1000, 1000);
		}	
	}

	public void stop()
	{
		if(aiTask != null && !aiTask.isCancelled())
		{
			aiTask.cancel(true);
			aiTask = null;
		}
	}

	public boolean isScheduled()
	{
		return aiTask != null && !aiTask.isCancelled();
	}
	
	public void clearDesires()
	{
		this.desireQueue.clear();
	}
	
	public void addDesire(Desire desire)
	{
		this.desireQueue.add(desire);
	}
	
	public int desireQueueSize()
	{
		return desireQueue.isEmpty() ? 0 : desireQueue.size();
	}
	
	public DesireQueue getDesireQueue()
	{
		return desireQueue;
	}

	public static DummyAi dummyAi()
	{
		return dummyAi;
	}
}