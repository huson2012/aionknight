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

package gameserver.network.aion.clientpackets;

import gameserver.network.aion.AionClientPacket;
import gameserver.network.loginserver.LoginServer;

/**
 * In this packets aion client is authenticating himself by providing accountId and rest of sessionKey - we will check
 * if its valid at login server side.
 */
// TODO: L2AUTH? Really? :O
public class CM_L2AUTH_LOGIN_CHECK extends AionClientPacket
{

	/**
	 * playOk2 is part of session key - its used for security purposes we will check if this is the key what login
	 * server sends.
	 */
	private int			playOk2;
	/**
	 * playOk1 is part of session key - its used for security purposes we will check if this is the key what login
	 * server sends.
	 */
	private int			playOk1;
	/**
	 * accountId is part of session key - its used for authentication we will check if this accountId is matching any
	 * waiting account login server side and check if rest of session key is ok.
	 */
	private int			accountId;
	/**
	 * loginOk is part of session key - its used for security purposes we will check if this is the key what login
	 * server sends.
	 */
	private int			loginOk;

	/**
	 * Constructs new instance of <tt>CM_L2AUTH_LOGIN_CHECK </tt> packet
	 * @param opcode
	 */
	public CM_L2AUTH_LOGIN_CHECK(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		playOk2 = readD();
		playOk1 = readD();
		accountId = readD();
		loginOk = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		LoginServer.getInstance().requestAuthenticationOfClient(accountId, getConnection(), loginOk, playOk1, playOk2);
	}
}