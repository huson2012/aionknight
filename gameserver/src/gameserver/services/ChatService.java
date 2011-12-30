/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
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
	 * ������� ����������� ������ �� ���-������
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
	 * ���������� �� ���-�������
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
	 * @return IP-�����
	 */
	public static byte[] getIp()
	{
		return ip;
	}
	/**
	 * @return ����
	 */
	public static int getPort()
	{
		return port;
	}
	/**
	 * @param ip-ip ��� ����������
	 */
	public static void setIp(byte[] _ip)
	{
		ip = _ip;
	}
	/**
	 * @param ����-���� ��� ����������
	 */
	public static void setPort(int _port)
	{
		port = _port;
	}
}