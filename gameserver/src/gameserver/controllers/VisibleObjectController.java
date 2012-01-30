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

package gameserver.controllers;

import gameserver.model.gameobjects.VisibleObject;
import gameserver.world.World;

/**
 * This class is for controlling VisibleObjects [players, npc's etc].
 * Its controlling movement, visibility etc.
 */
public abstract class VisibleObjectController<T extends VisibleObject>
{
	/**
	 * Object that is controlled by this controller.
	 */
	private T owner;

	/**
	 * Set owner (controller object).
	 * 
	 * @param owner
	 */
	public void setOwner(T owner)
	{
		this.owner = owner;
	}

	/**
	 * Get owner (controller object).
	 */
	public T getOwner()
	{
		return owner;
	}

	/**
	 * Called when controlled object is seeing other VisibleObject.
	 * 
	 * @param object
	 */
	public void see(VisibleObject object)
	{

	}

	/**
	 * Called when controlled object no longer see some other VisibleObject.
	 * 
	 * @param object
	 */
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{

	}
	
	/**
	 * Removes controlled object from the world.
	 */
	public void delete()
	{
		delete(false);
	}

	/**
	 * Removes controlled object from the world.
	 */
	public void delete(boolean instance)
	{
		/**
		 * despawn object from world.
		 */
		if(getOwner().isSpawned())
			World.getInstance().despawn(getOwner(), instance);
		/**
		 * Delete object from World.
		 */

		World.getInstance().removeObject(getOwner());
	}
	
	/**
	 * Called when object is re-spawned
	 */
	public void onRespawn()
	{
		
	}
}