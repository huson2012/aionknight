/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package gameserver.services;

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_CHAT_INIT;
import gameserver.network.chatserver.ChatServer;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class ChatService
{
	private static final Logger log	= Logger.getLogger(ChatService.class);
	private static Map<Integer, Player>	players	= new HashMap<Integer, Player>();
	private static byte[] ip = { 127, 0, 0, 1 };
	private static int port	= 10241;
	
	/**
	 * Послаем специальный маркер на чат-сервер
	 * 
	 * @param player
	 */
	public static void onPlayerLogin(final Player player)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
				if(!isPlayerConnected(player))
				{
					ChatServer.getInstance().sendPlayerLoginRequst(player);
				}
				else
				{
					log.warn("Player already registered with chat server " + player.getName());
				}
			}
		}, 10000);

	}
	/**
	 * Отключение от чат-сервера
	 * 
	 * @param player
	 */
	public static void onPlayerLogout(Player player)
	{
		players.remove(player.getObjectId());
		ChatServer.getInstance().sendPlayerLogout(player);
	}
	/**
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isPlayerConnected(Player player)
	{
		return players.containsKey(player.getObjectId());
	}
	/**
	 * @param playerId
	 * @param token
	 */
	public static void playerAuthed(int playerId, byte[] token)
	{
		Player player = World.getInstance().findPlayer(playerId);
		if(player != null)
		{
			players.put(playerId, player);
			PacketSendUtility.sendPacket(player, new SM_CHAT_INIT(token));
		}
	}
	/**
	 * @return IP-Адрес
	 */
	public static byte[] getIp()
	{
		return ip;
	}
	/**
	 * @return Порт
	 */
	public static int getPort()
	{
		return port;
	}
	/**
	 * @param ip-ip для соединения
	 */
	public static void setIp(byte[] _ip)
	{
		ip = _ip;
	}
	/**
	 * @param порт-порт для соединения
	 */
	public static void setPort(int _port)
	{
		port = _port;
	}
}