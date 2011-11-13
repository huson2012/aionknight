/**
 * This file is part of Aion-Knight Dev. Team <http://aion-knight.ru>.
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

package chatserver.network.aion.clientpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.network.aion.AbstractClientPacket;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.service.ChatService;

public class CM_PLAYER_AUTH extends AbstractClientPacket
{

	private int			playerId;
	private byte[]		token;
	private byte[]		identifier;

	@SuppressWarnings("unused")
	private byte[]		accountName;

	/**
	 * 
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_PLAYER_AUTH(ChannelBuffer channelBuffer, ClientChannelHandler clientChannelHandler)
	{
		super(channelBuffer, clientChannelHandler, 0x05);
	}

	@Override
	protected void readImpl()
	{
		readC(); // 0x40
		readH(); // 0x00
		readH(); // 0x01
		readH(); // 0x04
		readS(); // AION
		this.playerId = readD();
		readD(); // 0x00
		readD(); // 0x00
		int length = readH() * 2;
		identifier = readB(length);
		int accountLenght = readH() * 2;
		accountName = readB(accountLenght);
		int tokenLength = readH();
		token = readB(tokenLength);
	}

	@Override
	protected void runImpl()
	{
		ChatService.getInstance().registerPlayerConnection(playerId, token, identifier, clientChannelHandler);
	}
}