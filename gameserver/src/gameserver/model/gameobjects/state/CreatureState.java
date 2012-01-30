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
 
package gameserver.model.gameobjects.state;

public enum CreatureState
{
	NPC_IDLE(1<<6), // 64 (for npc)

	FLIGHT_TELEPORT(1<<1), // 2
	CHAIR(3<<1), // 6
	LOOTING(3<<2), // 12

	//confirmed in SM_EMOTION
	ACTIVE(1), // basic 1
	FLYING(1<<1), // 2
	RESTING(1<<2), // 4
	DEAD(3<<1), // 6
	//UNKNOWN8(1<<3), // 8
	PRIVATE_SHOP(5<<1), // 10
	//UNKNOWN16(1<<4), // 16
	WEAPON_EQUIPPED(1<<5), // 32
	WALKING(1<<6), // 64
	POWERSHARD(1<<7), // 128
	TREATMENT(1<<8), // 256
	GLIDING(1<<9); // 512
	/**
	 * Standing, path flying, 
	 * free flying, riding, 
	 * sitting, sitting on chair, 
	 * dead, fly dead, private shop, 
	 * looting, fly looting, default
	 */

	private int id;

	private CreatureState(int id)
	{
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}
}
