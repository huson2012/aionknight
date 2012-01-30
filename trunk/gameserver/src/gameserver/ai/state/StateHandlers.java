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

package gameserver.ai.state;

import gameserver.ai.state.handler.*;

public enum StateHandlers
{
	/**
	 * AIState.MOVINGTOHOME
	 */
	MOVINGTOHOME_SH(new MovingToHomeStateHandler()),
	
	/**
	 * AIState.NONE
	 */
	NONE_MONSTER_SH(new NoneNpcStateHandler()),
	
	/**
	 * AIState.ATTACKING
	 */
	ATTACKING_SH(new AttackingStateHandler()),
	
	/**
	 * AIState.THINKING
	 */
	THINKING_SH(new ThinkingStateHandler()),
	
	/**
	 * AIState.ACTIVE
	 */
	ACTIVE_NPC_SH(new ActiveNpcStateHandler()),
	ACTIVE_AGGRO_SH(new ActiveAggroStateHandler()),	
	
	/**
	 * AIState.RESTING
	 */
	RESTING_SH(new RestingStateHandler()),
	
	/**
	 * AIState.TALKING
	 */
	TALKING_SH(new TalkingStateHandler());
	
	private StateHandler stateHandler;
	
	private StateHandlers(StateHandler stateHandler)
	{
		this.stateHandler = stateHandler;
	}
	
	public StateHandler getHandler()
	{
		return stateHandler;
	}
}