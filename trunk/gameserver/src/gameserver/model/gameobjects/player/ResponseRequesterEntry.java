/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.model.gameobjects.player;

import gameserver.network.aion.AionServerPacket;

/**
 * @author Jego
 */
public class ResponseRequesterEntry
{
	private int						messageId;
	private RequestResponseHandler	handler;
	private AionServerPacket		packet;

	public ResponseRequesterEntry(int messageId, RequestResponseHandler handler, AionServerPacket packet)
	{
		this.messageId = messageId;
		this.handler = handler;
		this.packet = packet;
	}

	/**
	 * @return the messageId
	 */
	public int getMessageId()
	{
		return messageId;
	}

	/**
	 * @return the handler
	 */
	public RequestResponseHandler getHandler()
	{
		return handler;
	}

	/**
	 * @return the packet
	 */
	public AionServerPacket getPacket()
	{
		return packet;
	}
}
