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
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.world.World;
import gameserver.world.WorldMapType;

public class MovePlayerTo extends AdminCommand
{
	public MovePlayerTo()
	{
		super("moveplayerto");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVEPLAYERTO)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 5)
		{
			PacketSendUtility.sendMessage(admin, "syntax //moveplayerto <player name> <world Id> <X> <Y> <Z>");
			return;
		}

		Player playerToMove = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (playerToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		
		int worldId;
		float x, y, z;
		
		try
		{
			worldId = Integer.parseInt(params[1]);
			x = Float.parseFloat(params[2]);
			y = Float.parseFloat(params[3]);
			z = Float.parseFloat(params[4]);
		}
		catch(NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "WorldID, x, y and z should be numbers!");
			return;
		}
		
		if (WorldMapType.getWorld(worldId) == null)
		{
			PacketSendUtility.sendMessage(admin, "Illegal WorldId %d " + worldId );
		}
		else
		{
			TeleportService.teleportTo(playerToMove, worldId, x, y, z, 0);
			PacketSendUtility.sendMessage(admin, "Teleported player " + playerToMove.getName() + " to " + x + " " + y + " " + z + " [" + worldId + "]");
			PacketSendUtility.sendMessage(playerToMove, "You have been teleported by an Administrator.");
		}
	}
}