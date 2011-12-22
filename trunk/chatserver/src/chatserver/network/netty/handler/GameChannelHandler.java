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
import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.AbstractGameServerPacket;
import chatserver.network.gameserver.GameServerPacketHandler;

public class GameChannelHandler extends AbstractChannelHandler
{
	/**
	 * Default logger
	 */
	private static final Logger				log	= Logger.getLogger(GameChannelHandler.class);

	/**
	 * Current state of channel
	 */
	private State							state;

	/**
	 * 
	 * @param gameServerPacketHandler
	 */

	public GameChannelHandler()
	{
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		state = State.CONNECTED;
		channel = ctx.getChannel();
		inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();
		log.info("Channel connected Ip:" + inetAddress.getHostAddress());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);
		/**
		 * Packet is frame decoded and decrypted at this stage Here packet will be read and submitted to execution
		 */
		AbstractGameClientPacket gameServerPacket = GameServerPacketHandler
			.handle((ChannelBuffer) e.getMessage(), this);
		log.debug("Received packet: " + gameServerPacket);
		if (gameServerPacket != null && gameServerPacket.read())
		{
			gameServerPacket.run();
		}
	}

	/**
	 * 
	 * @param packet
	 */
	public void sendPacket(AbstractGameServerPacket packet)
	{
		ChannelBuffer cb = ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, 2 * 8192);
		packet.write(this, cb);
		channel.write(cb);
		log.debug("Sent packet: " + packet);
	}

	public static enum State
	{
		/**
		 * Means that GameServer just connected, but is not authenticated yet
		 */
		CONNECTED,
		/**
		 * GameServer is authenticated
		 */
		AUTHED
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
}