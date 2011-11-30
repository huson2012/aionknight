/**
 * This file is part of Aion-Knight <www.Aion-Knight.com>.
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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;

/**
 * @author LokiReborn
 * 
 */
public class SM_WINDSTREAM_LOCATIONS extends AionServerPacket
{

	private int bidirectional;
	private int mapId;
	private int streamId;
	private int boost;
	
	public SM_WINDSTREAM_LOCATIONS(int bidirectional, int mapId, int streamId, int boost)
	{
		this.bidirectional = bidirectional;
		this.mapId = mapId;
		this.streamId = streamId;
		this.boost = boost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, bidirectional);
		writeD(buf, mapId);
		writeD(buf, streamId);
		writeC(buf, boost);
	}
}