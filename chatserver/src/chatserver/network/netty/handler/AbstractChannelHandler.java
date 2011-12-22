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

import java.net.InetAddress;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import chatserver.common.netty.BaseServerPacket;

public abstract class AbstractChannelHandler extends SimpleChannelUpstreamHandler
{
	private static final Logger	log	= Logger.getLogger(AbstractChannelHandler.class);

	/**
	 * IP address of channel client
	 */
	protected InetAddress		inetAddress;
	/**
	 * Associated channel
	 */
	protected Channel			channel;

	/**
	 * Invoked when a Channel was disconnected from its remote peer
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		log.info("Channel disconnected IP: " + inetAddress.getHostAddress());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
	{
		// noinspection ThrowableResultOfMethodCallIgnored
		// log.error("NETTY: Exception caught: ", e.getCause());
		e.getChannel().close();
	}

	/**
	 * Closes the channel but ensures that packet is send before close
	 * 
	 * @param packet
	 */
	public void close(BaseServerPacket packet)
	{
		channel.write(packet).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Closes the channel
	 */
	public void close()
	{
		channel.close();
	}

	/**
	 * @return the IP address string
	 */
	public String getIP()
	{
		return inetAddress.getHostAddress();
	}
}