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

package chatserver.network.aion.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.model.channel.Channel;
import chatserver.network.aion.AbstractServerPacket;
import chatserver.network.netty.handler.ClientChannelHandler;

public class SM_CHANNEL_RESPONSE extends AbstractServerPacket
{
	
	private Channel channel;
	private int channelIndex;
	
	public SM_CHANNEL_RESPONSE(Channel channel, int channelIndex)
	{
		super(0x11);
		this.channel = channel;
		this.channelIndex = channelIndex;
	}

	@Override
	protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeC(buf, 0x40);
		writeH(buf, channelIndex);
		writeH(buf, 0x00);
		writeD(buf, channel.getChannelId());
	}
}