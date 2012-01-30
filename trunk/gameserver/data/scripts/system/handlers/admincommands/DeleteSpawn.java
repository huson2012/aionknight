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
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.PacketSendUtility;
import gameserver.dao.SpawnDAO;

import commons.database.dao.DAOManager;

/**
 * @author Luno
 * 
 */

public class DeleteSpawn extends AdminCommand
{

	public DeleteSpawn()
	{
		super("delete");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_DELETESPAWN)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		VisibleObject cre = admin.getTarget();
		if (!(cre instanceof Npc))
		{
			PacketSendUtility.sendMessage(admin, "�� ��� ���� ������ ������.");
			return;
		}
		
		Npc npc = (Npc) cre;
        SpawnTemplate template = npc.getSpawn();
        int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(template.getSpawnGroup().getNpcid(), template.getX(), template.getY());
        DAOManager.getDAO(SpawnDAO.class).deleteSpawn(spawnId);
		
        DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
        npc.getController().delete();
        PacketSendUtility.sendMessage(admin, "����� ��� ������!");
		

	}
}
