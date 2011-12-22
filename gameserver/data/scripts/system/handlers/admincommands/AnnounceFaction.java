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
import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;

public class AnnounceFaction extends AdminCommand
{
	public AnnounceFaction()
	{
		super("announcefaction");
	}

	@Override
	public void executeCommand(Player admin, final String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ANNOUNCE_FACTION)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //announcefaction <ely | asmo> <message>");
		}
		else
		{
			String message = "";

			if (params[0].equals("ely"))
				message += "Elyos : ";
			else
				message += "Asmodians : ";

			// Add with space
			for (int i=1; i<params.length-1; i++)
				message += params[i] + " ";

			// Add the last without the end space
			message += params[params.length-1];

			final String _message = message;
			World.getInstance().doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if (player.getCommonData().getRace() == Race.ELYOS && params[0].equals("ely"))
						PacketSendUtility.sendSysMessage(player, _message);
					else if (player.getCommonData().getRace() == Race.ASMODIANS && params[0].equals("asmo"))
						PacketSendUtility.sendSysMessage(player, _message);
					return true;
				}
			});
		}
	}
}
