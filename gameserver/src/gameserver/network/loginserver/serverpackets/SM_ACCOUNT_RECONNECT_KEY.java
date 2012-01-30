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

package gameserver.network.loginserver.serverpackets;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;

/**
 * This packet is sended by GameServer when player is requesting fast reconnect to login server. LoginServer in response
 * will send reconectKey.
 */
public class SM_ACCOUNT_RECONNECT_KEY extends LsServerPacket
{
	/**
	 * AccountId of client that is requested reconnection to LoginServer.
	 */
	private final int	accountId;

	/**
	 * Constructs new instance of <tt>SM_ACCOUNT_RECONNECT_KEY </tt> packet.
	 * 
	 * @param accountId
	 *           account identifier.
	 */
	public SM_ACCOUNT_RECONNECT_KEY(int accountId)
	{
		super(0x02);
		this.accountId = accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
	}
}
