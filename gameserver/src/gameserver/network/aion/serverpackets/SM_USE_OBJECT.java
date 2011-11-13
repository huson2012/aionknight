/**
 * This file is part of Aion-Knight [http://aion-knight.ru]
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


/**

 */
public class SM_USE_OBJECT extends AionServerPacket
{
	private int playerObjId;
	private int targetObjId;
	private int time;
	private int actionType;

	public SM_USE_OBJECT(int playerObjId, int targetObjId, int time, int actionType)
	{
		super();
		this.playerObjId = playerObjId;
		this.targetObjId = targetObjId;
		this.time = time;
		this.actionType = actionType;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, playerObjId);
		writeD(buf, targetObjId);
		writeD(buf, time);
		writeC(buf, actionType);
	}
}