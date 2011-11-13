/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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