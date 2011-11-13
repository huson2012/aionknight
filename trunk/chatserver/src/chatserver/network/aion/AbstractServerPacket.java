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

package chatserver.network.aion;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.common.netty.BaseServerPacket;
import chatserver.network.netty.handler.ClientChannelHandler;

public abstract class AbstractServerPacket extends BaseServerPacket
{
	/**
	 * @param opCode
	 */
	public AbstractServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * @param clientChannelHandler
	 * @param buf
	 */
	public void write(ClientChannelHandler clientChannelHandler, ChannelBuffer buf)
	{
		buf.writeShort((short)0);
		writeImpl(clientChannelHandler, buf);
	}
	
	/**
	 * @param cHandler
	 * @param buf
	 */
	protected abstract void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf);

}