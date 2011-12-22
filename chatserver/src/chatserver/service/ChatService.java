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

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import javolution.util.FastMap;
import org.apache.log4j.Logger;
import chatserver.model.ChatClient;
import chatserver.model.channel.Channel;
import chatserver.model.channel.Channels;
import chatserver.network.aion.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.network.netty.handler.ClientChannelHandler.State;

public class ChatService
{
	private static final Logger	log = Logger.getLogger(ChatService.class);
	
	private Map<Integer, ChatClient>	players	= new FastMap<Integer, ChatClient>();
	
	public static final ChatService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	/**
	 * Player registered from server side
	 * 
	 * @param playerId
	 * @param token
	 * @param identifier 
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public ChatClient registerPlayer(int playerId, String playerLogin) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(playerLogin.getBytes("UTF-8"), 0, playerLogin.length());
		byte[] accountToken = md.digest();
		byte[] token = generateToken(accountToken);
		ChatClient chatClient = new ChatClient(playerId, token);
		players.put(playerId, chatClient);
		return chatClient;
	}

	/**
	 * @param playerId
	 * @return
	 */
	private byte[] generateToken(byte[] accountToken)
	{
		byte[] dynamicToken = new byte[16];
		new Random().nextBytes(dynamicToken);
		byte[] token = new byte[48];
		for (int i = 0; i < token.length; i++)
		{
			if (i < 16)
				token[i] = dynamicToken[i];
			else
				token[i] = accountToken[i - 16];
		}
		return token;
	}

	/**
	 * Player registered from client request
	 * 
	 * @param playerId
	 * @param token
	 * @param identifier
	 * @param clientChannelHandler
	 */
	public void registerPlayerConnection(int playerId, byte[] token, byte[] identifier, ClientChannelHandler channelHandler)
	{
		ChatClient chatClient = players.get(playerId);
		if(chatClient != null)
		{
			byte[] regToken = chatClient.getToken();
			if(Arrays.equals(regToken, token))
			{
				chatClient.setIdentifier(identifier);
				chatClient.setChannelHandler(channelHandler);
				channelHandler.sendPacket(new SM_PLAYER_AUTH_RESPONSE());
				channelHandler.setState(State.AUTHED);
				channelHandler.setChatClient(chatClient);
				BroadcastService.getInstance().addClient(chatClient);
			}
		}
	}

	/**
	 * @param chatClient 
	 * @param channelIndex
	 * @param channelIdentifier
	 * @return
	 */
	public Channel registerPlayerWithChannel(ChatClient chatClient, int channelIndex, byte[] channelIdentifier)
	{
		Channel channel = Channels.getChannelByIdentifier(channelIdentifier);	
		if(channel != null)
			chatClient.addChannel(channel);
		return channel;
	}
	
	/**
	 * @param playerId
	 */
	public void playerLogout(int playerId)
	{
		ChatClient chatClient = players.get(playerId);
		if(chatClient != null)
		{
			players.remove(playerId);
			BroadcastService.getInstance().removeClient(chatClient);
			if(chatClient.getChannelHandler() != null)
				chatClient.getChannelHandler().close();
			else
				log.warn("Received logout event without client authentication for player " + playerId);
		}		
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ChatService instance = new ChatService();
	}
}