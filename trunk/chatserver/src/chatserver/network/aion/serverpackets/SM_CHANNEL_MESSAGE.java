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
 
package chatserver.network.aion.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.model.message.Message;
import chatserver.network.aion.AbstractServerPacket;
import chatserver.network.netty.handler.ClientChannelHandler;

public class SM_CHANNEL_MESSAGE extends AbstractServerPacket
{
	
	private Message message;
	
	public SM_CHANNEL_MESSAGE(Message message)
	{
		super(0x1A);
		this.message = message;
	}

	@Override
	protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeC(buf, 0x00);
		writeD(buf, message.getChannel().getChannelId());
		writeD(buf, message.getSender().getClientId());
		writeD(buf, 0x00);
		writeH(buf, message.getSender().getIdentifier().length / 2);
		writeB(buf, message.getSender().getIdentifier());
		writeH(buf, message.size() / 2);
		writeB(buf, message.getText());
	}
}