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