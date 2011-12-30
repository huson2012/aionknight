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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Movie extends AdminCommand
{
	public Movie()
	{
		super("movie");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVIE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		int movieId = 0;
		int type = 0;
		VisibleObject target = admin.getTarget();

		if (target == null || !(target instanceof Player))
		{
			target = admin;
		}
		if (params.length == 0)
		{
			PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			return;
		}
		if (params.length == 1)
		{
			try
			{
				movieId = Integer.valueOf(params[0]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(0, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}
		else if (params.length == 2)
		{
			try
			{
				type = Integer.valueOf(params[0]);
				movieId = Integer.valueOf(params[1]);
				PacketSendUtility.sendPacket((Player)target, new SM_PLAY_MOVIE(type, movieId));
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //movie <0 | 1> <movie id>");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Use numbers only!");
			}
		}

	}
}