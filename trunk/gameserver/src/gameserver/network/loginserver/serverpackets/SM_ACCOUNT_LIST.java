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

import gameserver.network.aion.AionConnection;
import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * GameServer packet that sends list of logged in accounts
 */
public class SM_ACCOUNT_LIST extends LsServerPacket
{

	/**
	 * Map with loaded accounts
	 */
	private final Map<Integer, AionConnection>	accounts;

	/**
	 * constructs new server packet with specified opcode.
	 */
	public SM_ACCOUNT_LIST(Map<Integer, AionConnection> accounts)
	{
		super(0x04);
		this.accounts = accounts;
	}

	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accounts.size());
		for(AionConnection ac : accounts.values())
		{
			writeS(buf, ac.getAccount().getName());
		}
	}
}
