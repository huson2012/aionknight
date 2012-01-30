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

package gameserver.ai.events.handler;

import gameserver.ai.AI;
import gameserver.ai.desires.impl.AggressionDesire;
import gameserver.ai.events.Event;
import gameserver.ai.state.AIState;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.stats.modifiers.Executor;

public class SeePlayerEventHandler implements EventHandler
{
	@Override
	public Event getEvent()
	{
		return Event.SEE_PLAYER;
	}

	@Override
	public void handleEvent(Event event, final AI<?> ai)
	{
		ai.getOwner().updateKnownlist();
		ai.setAiState(AIState.ACTIVE);
		if(!ai.isScheduled())
			ai.analyzeState();
		else if(ai.getDesireQueue().hasWalkingDesire())
		{
			ai.getOwner().getKnownList().doOnAllObjects(new Executor<AionObject>(){
				@Override
				public boolean run(AionObject object)
				{
					if (object instanceof Creature && ai.getOwner().isAggressiveTo((Creature)object))
					{
						ai.addDesire(new AggressionDesire((Npc)ai.getOwner(), AIState.ACTIVE.getPriority()));
						return false;
					}
					return true;
				}
			}, true);
		}
	}
}