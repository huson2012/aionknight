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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.PunishmentService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import java.util.NoSuchElementException;

public class SendToPrison extends AdminCommand
{

	public SendToPrison()
	{
		super("sprison");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_PRISON)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params.length == 0 || params.length != 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //sprison <player name> <time delay>");
			return;
		}

		try
		{
			Player playerToPrison = World.getInstance().findPlayer(Util.convertName(params[0]));
			int delay = Integer.parseInt(params[1]);

			if (playerToPrison != null)
			{
				PunishmentService.setIsInPrison(playerToPrison, true, delay);
				if (CustomConfig.CHANNEL_ALL_ENABLED)
					playerToPrison.banFromWorld(admin.getName(), "you have been sent to prison", 0);
				PacketSendUtility.sendMessage(admin, "Player " + playerToPrison.getName() + " has been sent to prison for "
					+ delay + ".");
			}
		}
		catch (NoSuchElementException nsee)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
		}
		catch (Exception e)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sprison <player name> <time delay>");
		}
	}
}