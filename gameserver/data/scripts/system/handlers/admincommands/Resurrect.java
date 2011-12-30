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
import gameserver.model.TaskId;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_RESURRECT;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;
import java.util.concurrent.Future;

public class Resurrect extends AdminCommand
{
	/**
	 * Constructor
	 */
	public Resurrect()
	{
		super("rez");
	}

	/**
	 * If player is still in the process of dying and this is used to resurrect
	 * with the instant flag, it may bug the player. Must wait for 2 or 3
	 * second after death before using resurrect instant. (Prompt may be used
	 * immediately.)
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_RESURRECT)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			return;
		}

		final VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "No target selected.");
			return;
		}

		if (!(target instanceof Player))
		{
			PacketSendUtility.sendMessage(admin, "You can only resurrect other players.");
			return;
		}

		final Player player = (Player)target;

		if (!player.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendMessage(admin, "That player is already alive.");
			return;
		}

		// Default action is to prompt for resurrect.
		if (params == null || params.length == 0 || ("prompt").startsWith(params[0]))
		{
			PacketSendUtility.sendPacket(player, new SM_RESURRECT(admin));
			
			//add task to player
			Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){
				@Override
				public void run()
				{
					//blank
				}
			}, 5 * 60 * 1000);//5minutes
			
			player.getController().addTask(TaskId.SKILL_RESURRECT, task);
			
			return;
		}

		if (("instant").startsWith(params[0]))
		{
			
			player.getReviveController().skillRevive(false);
			return;
		}

		PacketSendUtility.sendMessage(admin, "syntax //rez <instant | prompt>");
	}
}