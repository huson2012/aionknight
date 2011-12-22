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

package chatserver.service;

import java.util.Map;
import javolution.util.FastMap;
import chatserver.model.ChatClient;
import chatserver.model.message.Message;
import chatserver.network.aion.serverpackets.SM_CHANNEL_MESSAGE;
import chatserver.network.netty.handler.ClientChannelHandler;

public class BroadcastService
{
	private Map<Integer, ChatClient> clients;
	
	public static final BroadcastService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private BroadcastService()
	{
		clients = new FastMap<Integer, ChatClient>();
	}

	/**
	 * @param client
	 */
	public void addClient(ChatClient client)
	{
		clients.put(client.getClientId(), client);
	}
	
	/**
	 * @param client
	 */
	public void removeClient(ChatClient client)
	{
		clients.remove(client.getClientId());
	}
	
	/**
	 * @param message
	 */
	public void broadcastMessage(Message message)
	{
		for(ChatClient client : clients.values())
		{
			if(client.isInChannel(message.getChannel()))
				sendMessage(client, message);
		}
	}
	
	/**
	 * @param chatClient
	 * @param message
	 */
	public void sendMessage(ChatClient chatClient, Message message)
	{
		ClientChannelHandler cch = chatClient.getChannelHandler();
		cch.sendPacket(new SM_CHANNEL_MESSAGE(message));
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final BroadcastService instance = new BroadcastService();
	}
}