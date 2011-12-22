/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
 *
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������)
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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