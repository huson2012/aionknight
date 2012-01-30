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

package gameserver.ai.desires;

import gameserver.ai.AI;

/**
 * This interface represents basic desire functions.<br>
 * Each desire should implement {@link #handleDesire(gameserver.ai.AI)} method with default behaviour.<br>
 * AI can override {@link gameserver.ai.AI#handleDesire(Desire)} to implement custom behaviour of desire.<br>
 * 
 * @author SoulKeeper
 * @modified ATracer
 * @see gameserver.ai.AI
 * @see gameserver.ai.AI#handleDesire(Desire)
 * @see gameserver.ai.desires.AbstractDesire
 */
public interface Desire extends Comparable<Desire>
{
	/**
	 * Invokes default desire action. AI can override invocation of this method to handle desire in it's own way
	 * 
	 * @param ai
	 *           actor that is doing this desire
	 */
	boolean handleDesire(AI<?> ai);

	/**
	 * Returns hashcode for this object, must be overrided by child
	 * 
	 * @return hashcode for this object
	 */
	int hashCode();

	/**
	 * Compares this Desire with another object, must overriden by child
	 * 
	 * @param obj
	 *           another object to compare with
	 * @return result of object comparation
	 */
	boolean equals(Object obj);

	/**
	 * Returns desire power of this object
	 * 
	 * @return desire power of the object
	 */
	int getDesirePower();

	/**
	 * Adds desire power to this desire, this call is synchronized.<br>
	 * <br>
	 * <b>WARNING!!! Changing desire power after adding it to queue will not affect it's position, you have to call
	 * {@link gameserver.ai.desires.DesireQueue#addDesire(Desire)} passing this instance as argument</b>
	 * 
	 * @param desirePower
	 *           amount of desirePower to add
	 * @see DesireQueue#addDesire(Desire)
	 */
	void increaseDesirePower(int desirePower);

	/**
	 * Reduces desire power by give amount.<br>
	 * <br>
	 * <b>WARNING!!! Changing desire power after adding it to queue will not affect it's position, you have to call
	 * {@link gameserver.ai.desires.DesireQueue#addDesire(Desire)} passing this instance as argument</b>
	 * 
	 * @param desirePower
	 *           amount of desirePower to substract
	 * @see DesireQueue#addDesire(Desire)
	 */
	void reduceDesirePower(int desirePower);
	
	/**
	 * Used in desire filters
	 */
	boolean isReadyToRun();
	
	/**
	 * Will be called by ai when clearing desire queue.
	 */
	void onClear();
}