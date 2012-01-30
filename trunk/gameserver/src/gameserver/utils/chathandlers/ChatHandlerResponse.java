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


package gameserver.utils.chathandlers;

/**
 * Every {@link ChatHandler} as a result returns object of ChatHandlerResponse type. Objects of this class contains dual
 * information:
 * <ul>
 * <li>(maybe) Transformed message in accessible by {@link #getMessage()}</li>
 * <li>information whether handler blocked this message (it means, that it won't be sent to client(s)</li>
 * </ul>
 */
public class ChatHandlerResponse
{
	/** Single instance of <tt>ChatHandlerResponse</tt> representing response with blocked message */
	public static final ChatHandlerResponse	BLOCKED_MESSAGE	= new ChatHandlerResponse(true, "");

	private boolean							messageBlocked;
	private String							message;

	/**
	 * 
	 * @param messageBlocked
	 * @param message
	 */
	public ChatHandlerResponse(boolean messageBlocked, String message)
	{
		this.messageBlocked = messageBlocked;
		this.message = message;
	}

	/**
	 * A message (maybe) changed by handler.
	 * 
	 * @return a message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * 
	 * @return if true, it means that handler blocked sending this message to client.
	 */
	public boolean isBlocked()
	{
		return messageBlocked;
	}
}
