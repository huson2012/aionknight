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
 * In this packet Gameserver is asking if given account sessionKey is valid at Loginserver side. [if user that is
 * authenticating on Gameserver is already authenticated on Loginserver]
 */
public class SM_ACCOUNT_AUTH extends LsServerPacket
{
	/**
	 * accountId [part of session key]
	 */
	private final int	accountId;
	/**
	 * loginOk [part of session key]
	 */
	private final int	loginOk;
	/**
	 * playOk1 [part of session key]
	 */
	private final int	playOk1;
	/**
	 * playOk2 [part of session key]
	 */
	private final int	playOk2;

	/**
	 * Constructs new instance of <tt>SM_ACCOUNT_AUTH </tt> packet.
	 * 
	 * @param accountId
	 *           account identifier.
	 * @param loginOk
	 * @param playOk1
	 * @param playOk2
	 */
	public SM_ACCOUNT_AUTH(int accountId, int loginOk, int playOk1, int playOk2)
	{
		super(0x01);
		this.accountId = accountId;
		this.loginOk = loginOk;
		this.playOk1 = playOk1;
		this.playOk2 = playOk2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeD(buf, loginOk);
		writeD(buf, playOk1);
		writeD(buf, playOk2);
	}
}
