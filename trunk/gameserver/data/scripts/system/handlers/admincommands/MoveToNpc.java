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
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.NpcTemplate;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class MoveToNpc extends AdminCommand
{

	public MoveToNpc()
	{
		super("movetonpc");
	}

	/** (non-Javadoc)
	 * @see gameserver.utils.chathandlers.AdminCommand#executeCommand(gameserver.model.gameobjects.player.Player, java.lang.String[])
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_MOVETONPC)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
			return;
		}
		
		if(params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //movetonpc <npc id|npc name>");
			return;
		}

		int npcId = 0;

		try
		{
			npcId = Integer.valueOf(params[0]);
		}
		catch(NumberFormatException e)
		{
			for(NpcTemplate template : DataManager.NPC_DATA.getTemplates())
			{
				if(template.getName().equals(params[0]))
				{
					npcId = template.getTemplateId();
					break;
				}
			}
			if(npcId == 0)
			{
				PacketSendUtility.sendMessage(admin, "NPC " + params[0] + " cannot be found");
			}
		}
		
		if(npcId > 0)
			TeleportService.teleportToNpc(admin, npcId);		
	}
}
