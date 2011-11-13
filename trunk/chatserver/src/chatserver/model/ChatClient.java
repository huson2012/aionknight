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

package chatserver.model;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javolution.util.FastMap;
import chatserver.model.channel.Channel;
import chatserver.network.netty.handler.ClientChannelHandler;

public class ChatClient
{
	/**
	 * Id клиента (player id)
	 */
	private int							clientId;

	/**
	 * Идентификатор, который используется при отправке сообщений
	 */
	private byte[]						identifier;

	/**
	 * Знак, используемый в проверке подключения к 'ИС'
	 */
	private byte[]						token;

	/**
	 * Канал обработчик чат <--> клиент
	 */
	private ClientChannelHandler		channelHandler;

	/**
	 * Карта всех подключенных каналов. Только один канал конкретного типа может быть добавлены
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