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

package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

public class SM_BLOCK_RESPONSE extends AionServerPacket
{
	public static final int BLOCK_SUCCESSFUL = 0;
	public static final int UNBLOCK_SUCCESSFUL = 1;
	public static final int	TARGET_NOT_FOUND = 2;
	public static final int LIST_FULL = 3;
	public static final int CANT_BLOCK_SELF	= 4;
	private int	code;
	private String playerName;

	public SM_BLOCK_RESPONSE(int code, String playerName)
	{
		this.code = code;
		this.playerName = playerName;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeS(buf, playerName);
		writeD(buf, code);		
	}
}