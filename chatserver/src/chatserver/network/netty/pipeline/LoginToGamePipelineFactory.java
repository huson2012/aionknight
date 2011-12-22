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

package chatserver.network.netty.pipeline;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import chatserver.network.netty.coder.GameServerPacketDecoder;
import chatserver.network.netty.coder.GameServerPacketEncoder;
import chatserver.network.netty.coder.PacketFrameDecoder;
import chatserver.network.netty.handler.GameChannelHandler;

public class LoginToGamePipelineFactory implements ChannelPipelineFactory
{
	private static final int						THREADS_MAX			= 10;
	private static final int						MEMORY_PER_CHANNEL	= 1048576;
	private static final int						TOTAL_MEMORY		= 134217728;
	private static final int						TIMEOUT				= 100;

	private OrderedMemoryAwareThreadPoolExecutor	pipelineExecutor;

	public LoginToGamePipelineFactory()
	{
		pipelineExecutor = new OrderedMemoryAwareThreadPoolExecutor(THREADS_MAX, MEMORY_PER_CHANNEL, TOTAL_MEMORY,
			TIMEOUT, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory());
	}

	/**
	 * Decoding process will include the following handlers: - framedecoder - packetdecoder - handler
	 * 
	 * Encoding process: - packetencoder
	 * 
	 * Please note the sequence of handlers
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();

		pipeline.addLast("framedecoder", new PacketFrameDecoder());
		pipeline.addLast("packetdecoder", new GameServerPacketDecoder());
		pipeline.addLast("packetencoder", new GameServerPacketEncoder());

		pipeline.addLast("executor", new ExecutionHandler(pipelineExecutor));
		pipeline.addLast("handler", new GameChannelHandler());

		return pipeline;
	}
}