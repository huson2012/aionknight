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

package gameserver.network.loginserver.serverpackets;

import java.nio.ByteBuffer;

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;


/**
 * 
 * @author Aionchs-Wylovech
 * 
 */
public class SM_LS_CONTROL extends LsServerPacket
{
	private final String		accountName;

	private final String		adminName;

	private final String		playerName;

	private final int		param;

	private final int		type;

	public SM_LS_CONTROL(String accountName, String playerName, String adminName, int param, int type)
	{

		super(0x05);
		this.accountName = accountName;
		this.param = param;
		this.playerName = playerName;
		this.adminName = adminName;
		this.type = type;

	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeC(buf, type);
		writeS(buf, adminName);
		writeS(buf, accountName);
		writeS(buf, playerName);
		writeC(buf, param);
	}
}
