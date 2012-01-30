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

package gameserver.network.loginserver.clientpackets;

import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LsClientPacket;

/**
 * In this packet LoginServer is sending response for SM_ACCOUNT_RECONNECT_KEY with account name and reconnectionKey.
 */
public class CM_ACCOUNT_RECONNECT_KEY extends LsClientPacket
{
	/**
	 * accountId of account that will be reconnecting.
	 */
	private int	accountId;
	/**
	 * ReconnectKey that will be used for authentication.
	 */
	private int	reconnectKey;

	/**
	 * Constructs new instance of <tt>CM_ACCOUNT_RECONNECT_KEY </tt> packet
	 * @param opcode
	 */
	public CM_ACCOUNT_RECONNECT_KEY(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountId = readD();
		reconnectKey = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		LoginServer.getInstance().authReconnectionResponse(accountId, reconnectKey);
	}
}
