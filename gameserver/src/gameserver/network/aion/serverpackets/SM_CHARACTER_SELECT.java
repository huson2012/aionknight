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
import gameserver.configs.main.GSConfig;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

public class SM_CHARACTER_SELECT extends AionServerPacket
{
	private int type;
	private int messageType;
	private int wrongCount;

	public SM_CHARACTER_SELECT(int type)
	{
		this.type = type;
	}

	public SM_CHARACTER_SELECT(int type, int messageType, int wrongCount)
	{
		this.type = type;
		this.messageType = messageType;
		this.wrongCount = wrongCount;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, type);

		switch (type)
		{
			case 0:
				break;
			case 1:
				break;
			case 2:
				writeH(buf, messageType);
				writeC(buf, wrongCount > 0 ? 1 : 0);
				writeD(buf, wrongCount);
				writeD(buf, GSConfig.PASSKEY_WRONG_MAXCOUNT);
				break;
		}
	}
}