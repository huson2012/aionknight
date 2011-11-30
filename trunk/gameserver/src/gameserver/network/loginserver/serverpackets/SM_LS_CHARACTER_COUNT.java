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

import gameserver.network.loginserver.LoginServerConnection;
import gameserver.network.loginserver.LsServerPacket;

import java.nio.ByteBuffer;


/**
 * @author blakawk
 *
 */
public class SM_LS_CHARACTER_COUNT extends LsServerPacket
{
	private int accountId;
	private int characterCount;
	
	public SM_LS_CHARACTER_COUNT(int accountId, int characterCount)
	{
		super(0x07);
		
		this.accountId = accountId;
		this.characterCount = characterCount;
	}

	/** (non-Javadoc)
	 * @see gameserver.network.loginserver.LsServerPacket#writeImpl(gameserver.network.loginserver.LoginServerConnection, java.nio.ByteBuffer)
	 */
	@Override
	protected void writeImpl(LoginServerConnection con, ByteBuffer buf)
	{
		writeC(buf, getOpcode());
		writeD(buf, accountId);
		writeC(buf, characterCount);
	}

}
