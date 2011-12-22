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

package chatserver.network.netty.handler;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import chatserver.model.ChatClient;
import chatserver.network.aion.AbstractClientPacket;
import chatserver.network.aion.AbstractServerPacket;
import chatserver.network.aion.ClientPacketHandler;

public class ClientChannelHandler extends AbstractChannelHandler
{
	private static final Logger log	= Logger.getLogger(ClientChannelHandler.class);
	private State state;
	private ChatClient chatClient;

	public ClientChannelHandler()
	{
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);

		state = State.CONNECTED;
		inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
		channel = ctx.getChannel();
		log.info("Channel connected Ip:" + inetAddress.getHostAddress());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);

		AbstractClientPacket clientPacket = ClientPacketHandler.handle((ChannelBuffer) e.getMessage(), this);
		if (clientPacket != null && clientPacket.read())
		{
			clientPacket.run();
		}
		if(clientPacket != null)
			log.debug("Received packet: " + clientPacket);
	}

	/**
	 * 
	 * @param packet
	 */
	public void sendPacket(AbstractServerPacket packet)
	{
		ChannelBuffer cb = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, 2 * 8192);
		packet.write(this, cb);
		channel.write(cb);
		log.debug("Sent packet: " + packet);
	}

	/**
	 * Possible states of channel handler
	 */
	public static enum State
	{
		/**
		 * client just connected
		 */
		CONNECTED,
		/**
		 * client is authenticated
		 */
		AUTHED,
	}

	/**
	 * @return the state
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * @return the chatClient
	 */
	public ChatClient getChatClient()
	{
		return chatClient;
	}

	/**
	 * @param chatClient
	 *           the chatClient to set
	 */
	public void setChatClient(ChatClient chatClient)
	{
		this.chatClient = chatClient;
	}
}