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

package gameserver.network;

@SuppressWarnings("serial")
public class KeyAlreadySetException extends RuntimeException
{
	/**
	 * Constructs an <code>KeyAlreadySetException</code> with no detail message.
	 */
	public KeyAlreadySetException()
	{
		super();
	}

	/**
	 * Constructs an <code>KeyAlreadySetException</code> with the specified detail message.
	 * 
	 * @param s
	 *           the detail message.
	 */
	public KeyAlreadySetException(String s)
	{
		super(s);
	}

	/**
	 * Creates new error
	 * 
	 * @param message
	 *           exception description
	 * @param cause
	 *           reason of this exception
	 */
	public KeyAlreadySetException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Creates new error
	 * 
	 * @param cause
	 *           reason of this exception
	 */
	public KeyAlreadySetException(Throwable cause)
	{
		super(cause);
	}
}