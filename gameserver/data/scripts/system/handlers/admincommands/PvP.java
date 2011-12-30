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

import gameserver.model.gameobjects.player.Player;
import gameserver.services.PvPZoneService;
import gameserver.services.PvpService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class PvP extends AdminCommand 
{
    public PvP()
    {
        super("pvp");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < 2) {
             PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
            return;
        }
        if (params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax: //pvp <open|close|reset|ap>");
            return;
        }
        if ("open".equalsIgnoreCase(params[0])) {
			if (!PvPZoneService.Spawn(250148, 250148, 730207))
				PacketSendUtility.sendMessage(admin, "PvP Event ist already started.");
			else
				PacketSendUtility.sendMessage(admin, "PvP Event will be started.");
		} 
		else if ("close".equalsIgnoreCase(params[0])) {
			if (!PvPZoneService.Delete())
				PacketSendUtility.sendMessage(admin, "PvP Event has bin not started.");
		} 
		else if ("ap".equalsIgnoreCase(params[0])) {
			if (params.length == 1) {
				PacketSendUtility.sendMessage(admin, "syntax: //pvp ap <on|off>");
			}
			else if ("on".equalsIgnoreCase(params[1])) {
				if (PvpService.getPvpZoneReward())
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was already set.");
				else
				{
					PvpService.setPvpZoneReward(true);
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was already turn on");
				}
			}
			else if ("off".equalsIgnoreCase(params[1])) {
				if (!PvpService.getPvpZoneReward())
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was turned off.");
				else
				{
					PvpService.setPvpZoneReward(true);
					PacketSendUtility.sendMessage(admin, "PvP Event Reward was set off.");
				}
			}
			else {
				PacketSendUtility.sendMessage(admin, "syntax: //pvp ap <on|off>");
			}
		} 
		else if ("reset".equalsIgnoreCase(params[0])) {
			PvPZoneService.AdminReset();
			PacketSendUtility.sendMessage(admin, "PvP Event has bin resetet.");
		} 
		else {
			PacketSendUtility.sendMessage(admin, "Syntax: //pvp <open|close|reset|ap>");
		}
    }
}