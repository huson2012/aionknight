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

import gameserver.model.account.AccountTime;
import gameserver.network.loginserver.LoginServer;
import gameserver.network.loginserver.LsClientPacket;

/**
 * In this packet LoginServer is answering on GameServer request about valid authentication data and also sends account
 * name of user that is authenticating on GameServer.
 */
public class CM_ACOUNT_AUTH_RESPONSE extends LsClientPacket
{
	/**
	 * accountId
	 */
	private int	accountId;

	/**
	 * result - true = authed
	 */
	private boolean	result;

	/**
	 * accountName [if response is ok]
	 */
	private String accountName;
	private AccountTime	accountTime;
	/**
	 * access level - regular/gm/admin
	 */
	private byte accessLevel;
	/**
	 * Membership - regular/premium
	 */
	private byte membership;
	
	/**
	 * Constructs new instance of <tt>CM_ACOUNT_AUTH_RESPONSE </tt> packet.
	 * @param opcode
	 */
	public CM_ACOUNT_AUTH_RESPONSE(int opcode)
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
		result = readC() == 1;

		if(result)
		{
			accountName = readS();
			accountTime = new AccountTime();
			accountTime.setAccumulatedOnlineTime(readQ());
			accountTime.setAccumulatedRestTime(readQ());			
			accessLevel = (byte) readC();
			membership = (byte) readC();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		LoginServer.getInstance().accountAuthenticationResponse(accountId, accountName, result, accountTime, accessLevel, membership);
	}
}
