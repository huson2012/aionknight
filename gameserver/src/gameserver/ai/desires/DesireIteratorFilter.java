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

public interface DesireIteratorFilter
{
	/**
	 * This method is called each time for every desire that is in the queue.<br>
	 * <br> {@link java.util.ConcurrentModificationException} will be thrown by
	 * {@link gameserver.ai.desires.DesireQueue#iterateDesires(DesireIteratorHandler, DesireIteratorFilter[])}
	 * if any of the following methods will be called from here:
	 * <ul>
	 * <li>{@link gameserver.ai.desires.DesireQueue#addDesire(Desire)}</li>
	 * <li>{@link gameserver.ai.desires.DesireQueue#poll()}</li>
	 * <li>{@link gameserver.ai.desires.DesireQueue#removeDesire(Desire)}</li>
	 * </ul>
	 * <p/>
	 * However {@link gameserver.ai.desires.DesireQueue#clear()} can be called.
	 * 
	 * @param desire
	 *           current element of iteration that is being filtered
	 * @return true if this filter accepted desire, false otherwise
	 */
	public boolean isOk(Desire desire);
}