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

package gameserver.model.gameobjects.player;

import gameserver.model.gameobjects.Creature;

public abstract class RequestResponseHandler
{
	private Creature requester;

	public RequestResponseHandler(Creature requester)
	{
		this.requester = requester;
	}

	/**
	 * Called when a response is received
	 * @param requested Player whom requested this response
	 * @param responder Player whom responded to this request
	 * @param responseCode The response the player gave, usually 0 = no 1 = yes
	 */
	public void handle(Player responder, int response)
	{
		if (response == 0)
			denyRequest(requester, responder);
		else
			acceptRequest(requester, responder);
	}

	/**
	 * Called when the player accepts a request
	 * @param requester Creature whom requested this response
	 * @param responder Player whom responded to this request
	 */
	public abstract void acceptRequest(Creature requester, Player responder);

	/**
	 * Called when the player denies a request
	 * @param requester Creature whom requested this response
	 * @param responder Player whom responded to this request
	 */
	public abstract void denyRequest(Creature requester, Player responder);
}
