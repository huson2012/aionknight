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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

public class AddTitle extends AdminCommand
{

	public AddTitle()
	{
		super("addtitle");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDTITLE)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if((params.length < 1) || (params.length > 2))
		{
			PacketSendUtility.sendMessage(admin, "sintax: //addtitle <title id> [expire time]");
			return;
		}

		int titleId = Integer.parseInt(params[0]);

		VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "No target selected");
			return;
		}

		if (target instanceof Player)
		{
			Player player = (Player) target;

			boolean sucess = false;

			try
			{
				if(params.length == 2)
				{
					long expireMinutes = Long.parseLong(params[1]);
					sucess = player.getTitleList().addTitle(titleId, System.currentTimeMillis(), (expireMinutes * 60L));
				}else{
					sucess = player.getTitleList().addTitle(titleId);
				}
			}
			catch (NumberFormatException ex)
			{
				PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETER_REQUIRED));
				return;
			}

			if(sucess)
			{
				PacketSendUtility.sendMessage(admin, "Title added!");
			}else{
				PacketSendUtility.sendMessage(admin, "You can't add this title");
			}
		}
	}
}
