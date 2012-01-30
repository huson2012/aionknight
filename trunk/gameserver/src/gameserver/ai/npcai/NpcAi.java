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
import gameserver.ai.events.Event;
import gameserver.ai.events.EventHandlers;
import gameserver.ai.events.handler.EventHandler;
import gameserver.ai.state.StateHandlers;
import gameserver.model.gameobjects.Npc;

public class NpcAi extends AI<Npc>
{
	public NpcAi()
	{
		/**
		 * Event Handlers
		 */
		this.addEventHandler(EventHandlers.NOTHINGTODO_EH.getHandler());
		this.addEventHandler(EventHandlers.TIREDATTACKING_EH.getHandler());
		this.addEventHandler(EventHandlers.MOST_HATED_CHANGED_EH.getHandler());
		this.addEventHandler(EventHandlers.RESPAWNED_EH.getHandler());
		this.addEventHandler(EventHandlers.DIED_EH.getHandler());
		this.addEventHandler(EventHandlers.DESPAWN_EH.getHandler());
		this.addEventHandler(EventHandlers.TALK_EH.getHandler());
		this.addEventHandler(EventHandlers.BACKHOME_EH.getHandler());
		this.addEventHandler(EventHandlers.ATTACKED_EH.getHandler());
		
		/**
		 * State Handlers
		 */
		this.addStateHandler(StateHandlers.MOVINGTOHOME_SH.getHandler());
		this.addStateHandler(StateHandlers.ACTIVE_NPC_SH.getHandler());
		this.addStateHandler(StateHandlers.TALKING_SH.getHandler());
		this.addStateHandler(StateHandlers.ATTACKING_SH.getHandler());
		this.addStateHandler(StateHandlers.THINKING_SH.getHandler());
		this.addStateHandler(StateHandlers.RESTING_SH.getHandler());
	}

	@Override
	public void handleEvent(Event event)
	{
		super.handleEvent(event);
		
		// Allow only handling event Event.DIED in dead stats
		// probably i need to define rules for which events could 
		// be handled in which state
		if(event != Event.DIED && owner.getLifeStats().isAlreadyDead())
			return;
		
		EventHandler eventHandler = eventHandlers.get(event);
		if(eventHandler != null)
			eventHandler.handleEvent(event, this);
	}
}