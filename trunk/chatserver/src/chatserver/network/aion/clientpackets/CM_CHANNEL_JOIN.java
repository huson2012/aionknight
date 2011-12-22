/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
 *
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова)
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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