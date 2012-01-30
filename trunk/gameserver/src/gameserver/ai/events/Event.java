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

package gameserver.ai.events;

public enum Event
{
	/**
	 * This event is received on each enemy attack
	 */
	ATTACKED,
	/**
	 * Target is too far or long time passed since last attak
	 */
	TIRED_ATTACKING_TARGET,
	/**
	 * During attack most hated creature changed from current target
	 */
	MOST_HATED_CHANGED,
	/**
	 * In active state there is nothing to do
	 */
	NOTHING_TODO,
	/**
	 * Npc is far from spawn point
	 */
	FAR_FROM_HOME,
	/**
	 * Npc returned to spawn point
	 */
	BACK_HOME,
	/**
	 * Npc restored health fully (after returning to home)
	 */
	RESTORED_HEALTH,
	/**
	 * Npc sees another player
	 */
	SEE_PLAYER,
	/**
	 * Player removed from knownlist
	 */
	NOT_SEE_PLAYER,
	/**
	 * Any creature is in the visible radius
	 */
	SEE_CREATURE,
	/**
	 * Creature removed from knownlist
	 */
	NOT_SEE_CREATURE,
	/**
	 * Talk request
	 */
	TALK,
	/**
	 * Npc is respawned
	 */
	RESPAWNED,
	/**
	 * Creature died
	 */
	DIED,
	/**
	 * Despawn service was called
	 */
	DESPAWN
}