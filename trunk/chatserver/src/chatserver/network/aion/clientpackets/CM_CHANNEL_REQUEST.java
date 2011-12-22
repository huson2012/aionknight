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

package chatserver.network.aion.clientpackets;

import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import chatserver.model.ChatClient;
import chatserver.model.channel.Channel;
import chatserver.network.aion.AbstractClientPacket;
import chatserver.network.aion.serverpackets.SM_CHANNEL_RESPONSE;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.service.ChatService;

public class CM_CHANNEL_REQUEST extends AbstractClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_CHANNEL_REQUEST.class);

	private int					channelIndex;
	private byte[]				channelIdentifier;

	/**
	 * 
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_CHANNEL_REQUEST(ChannelBuffer channelBuffer, ClientChannelHandler gameChannelHandler)
	{
		super(channelBuffer, gameChannelHandler, 0x10);
	}

	@Override
	protected void readImpl()
	{
		readC(); // 0x40
		readH(); // 0x00
		channelIndex = readH();
		int length = readH() * 2;
		channelIdentifier = readB(length);
	}

	@Override
	protected void runImpl()
	{
		/** try
		{
			log.info("CM_CHANNEL_REQUEST: " + channelIndex + " and channel: " + new String(channelIdentifier, "UTF-16le"));
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} */
		
		ChatClient chatClient = clientChannelHandler.getChatClient();
		Channel channel = ChatService.getInstance().registerPlayerWithChannel(chatClient, channelIndex, channelIdentifier);
		if (channel != null)
		{
			//log.info("Sending SM_CHANNEL_RESPONSE with channel " + channel.getChannelId() + " index " + channelIndex);
			clientChannelHandler.sendPacket(new SM_CHANNEL_RESPONSE(channel, channelIndex));
		}
		else
		{
			try
			{
				log.error("CM_CHANNEL_REQUEST NULL!: " + channelIndex + " and channel: " + new String(channelIdentifier, "UTF-16le"));
			}
			catch(UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString()
	{
		return "CM_CHANNEL_REQUEST [channelIndex=" + channelIndex + ", channelIdentifier="
			+ new String(channelIdentifier) + "]";
	}

}