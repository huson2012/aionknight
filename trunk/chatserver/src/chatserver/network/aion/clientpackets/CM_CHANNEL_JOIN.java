/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 */

package chatserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.model.ChatClient;
import chatserver.model.channel.Channel;
import chatserver.network.aion.AbstractClientPacket;
import chatserver.network.aion.serverpackets.SM_CHANNEL_RESPONSE;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.service.ChatService;

public class CM_CHANNEL_JOIN extends AbstractClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_CHANNEL_REQUEST.class);
	
	private int channelIndex;
	private byte[] channelIdentifier;
	private byte[] channelPassword;
	
	public CM_CHANNEL_JOIN(ChannelBuffer channelBuffer, ClientChannelHandler gameChannelHandler) {
		super(channelBuffer, gameChannelHandler, 0x0D);
	}
	
	@Override
	protected void readImpl() {
		readC(); //0x40
		readH(); //0x00
		channelIndex = readH();
		int length = readH() * 2;
		channelIdentifier = readB(length);
		length = readH() * 2;
		if(length == 0)
			channelPassword = null;
		else
			channelPassword = readB(length);
	}
	
	@Override
	protected void runImpl() {
		
		ChatClient chatClient = clientChannelHandler.getChatClient();
		Channel channel = ChatService.getInstance().registerPlayerWithChannel(chatClient, channelIndex, channelIdentifier);
		if (channel != null)
		{
			//log.info("Sending SM_CHANNEL_RESPONSE with channel " + channel.getChannelId() + " index " + channelIndex);
			clientChannelHandler.sendPacket(new SM_CHANNEL_RESPONSE(channel, channelIndex));
		}
	}
}