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

package usercommands;

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.CustomChannel;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

public class WorldChannelMessage extends CustomChannel
{
	public WorldChannelMessage ()
	{
		super(LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_WORLD), Player.CHAT_FIXED_ON_WORLD);
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		if (!CustomConfig.CHANNEL_WORLD_ENABLED)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.CHANNEL_WORLD_DISABLED, Player.getChanCommand(Player.CHAT_FIXED_ON_WORLD), Player.getChanCommand(Player.CHAT_FIXED_ON_ELYOS), Player.getChanCommand(Player.CHAT_FIXED_ON_ASMOS)));
			return;
		}
		
		super.executeCommand(player, params);
	}
}