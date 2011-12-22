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

package chatserver.model;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javolution.util.FastMap;
import chatserver.model.channel.Channel;
import chatserver.network.netty.handler.ClientChannelHandler;

public class ChatClient
{
	/**
	 * Id ������� (player id)
	 */
	private int							clientId;

	/**
	 * �������������, ������� ������������ ��� �������� ���������
	 */
	private byte[]						identifier;

	/**
	 * ����, ������������ � �������� ����������� � '��'
	 */
	private byte[]						token;

	/**
	 * ����� ���������� ��� <--> ������
	 */
	private ClientChannelHandler		channelHandler;

	/**
	 * ����� ���� ������������ �������. ������ ���� ����� ����������� ���� ����� ���� ���������
	 */
	private Map<ChannelType, Channel>	channelsList	= new FastMap<ChannelType, Channel>();

	/**
	 * Incremented during each new channel request
	 */
	private AtomicInteger				channelIndex	= new AtomicInteger(1);

	/**
	 * @param clientId
	 * @param token
	 * @param identifier
	 */
	public ChatClient(int clientId, byte[] token)
	{
		this.clientId = clientId;
		this.token = token;
	}

	/**
	 * @param channel
	 */
	public void addChannel(Channel channel)
	{
		channelsList.put(channel.getChannelType(), channel);
	}

	/**
	 * @param channel
	 */
	public boolean isInChannel(Channel channel)
	{
		return channelsList.containsKey(channel.getChannelType());
	}

	/**
	 * @return the clientId
	 */
	public int getClientId()
	{
		return clientId;
	}

	/**
	 * @return the token
	 */
	public byte[] getToken()
	{
		return token;
	}

	/**
	 * @return the identifier
	 */
	public byte[] getIdentifier()
	{
		return identifier;
	}

	/**
	 * @return the channelHandler
	 */
	public ClientChannelHandler getChannelHandler()
	{
		return channelHandler;
	}

	/**
	 * @param channelHandler the channelHandler to set
	 */
	public void setChannelHandler(ClientChannelHandler channelHandler)
	{
		this.channelHandler = channelHandler;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(byte[] identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * @return
	 */
	public int nextIndex()
	{
		return channelIndex.incrementAndGet();
	}
}