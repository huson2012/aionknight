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

package chatserver.network.gameserver.clientpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.network.netty.handler.GameChannelHandler.State;
import chatserver.service.GameServerService;

public class CM_CS_AUTH extends AbstractGameClientPacket
{
	/**
	 * Password for authentication
	 */
	private String				password;

	/**
	 * Id of GameServer
	 */
	private byte				gameServerId;

	/**
	 * Default address for server
	 */
	private byte[]				defaultAddress;

	public CM_CS_AUTH(ChannelBuffer buf, GameChannelHandler gameChannelHandler)
	{
		super(buf, gameChannelHandler, 0x00);
	}

	@Override
	protected void readImpl()
	{
		gameServerId = (byte) readC();

		defaultAddress = readB(readC());
		password = readS();
	}

	@Override
	protected void runImpl()
	{
		GsAuthResponse resp = GameServerService.registerGameServer(gameChannelHandler, gameServerId, defaultAddress,
			password);

		switch (resp)
		{
			case AUTHED:
				gameChannelHandler.setState(State.AUTHED);
				gameChannelHandler.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			case NOT_AUTHED:
				gameChannelHandler.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			default:
				gameChannelHandler.close(new SM_GS_AUTH_RESPONSE(resp));
		}
	}
}