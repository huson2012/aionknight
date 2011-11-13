/**
 * This file is part of Aion-Knight Dev. Team <http://aion-knight.ru>.
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

package chatserver.network.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.common.netty.BaseServerPacket;
import chatserver.network.netty.handler.GameChannelHandler;

public abstract class AbstractGameServerPacket extends BaseServerPacket
{
	/**
	 * @param opCode
	 */
	public AbstractGameServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * @param gameChannelHandler
	 * @param buf
	 */
	public void write(GameChannelHandler gameChannelHandler, ChannelBuffer buf)
	{
		writeH(buf, 0);
		writeImpl(gameChannelHandler, buf);
	}
	
	/**
	 * @param gameChannelHandler
	 * @param buf
	 */
	protected abstract void writeImpl(GameChannelHandler gameChannelHandler, ChannelBuffer buf);
	
}