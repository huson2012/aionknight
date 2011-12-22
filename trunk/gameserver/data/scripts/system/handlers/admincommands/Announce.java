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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class Announce extends AdminCommand
{
	public Announce()
	{
		super("announce");
	}

	@Override
	public int getSplitSize()
	{
		return 2;
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{

		if (admin.getAccessLevel() < AdminConfig.COMMAND_ANNOUNCE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params == null || params.length != 2)
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //announce <anonymous|name> <message>");
			return;
		}

		String message = "";

		if (("anonymous").startsWith(params[0].toLowerCase()))
		{
			message += "Announce: ";
		}
		else if (("name").startsWith(params[0].toLowerCase()))
		{
			if(CustomConfig.GMTAG_DISPLAY)
			{
				if(admin.getAccessLevel() == 1 )
				{
					message += CustomConfig.GM_LEVEL1.trim();
				}
				else if (admin.getAccessLevel() == 2 )
				{
					message += CustomConfig.GM_LEVEL2.trim();
				}
				else if (admin.getAccessLevel() == 3 )
				{
					message += CustomConfig.GM_LEVEL3.trim();
				}
			}

			message += admin.getName() + ": ";
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //announce <anonymous|name> <message>");
			return;
		}
		
		message += params[1];
		final String _message = message;

		World.getInstance().doOnAllPlayers(new Executor<Player> () {
			@Override
			public boolean run(Player player)
			{
				PacketSendUtility.sendSysMessage(player, _message);
				return true;
			}
		});
	}
}
