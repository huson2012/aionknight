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

package usercommands;

import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.SkillLearnService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

public class GiveMissingSkills extends UserCommand
{
	/**
	 * ������ �� ��������� ������������ ������ �� ������� //givemissingskills
	 */
	public GiveMissingSkills()
	{
		super("givemissingskills");
	}

	private int countSkills(Player player)
	{
		int count = 0;
		
		for (int i = 0; i < player.getSkillList().getAllSkills().length; i++)
		{
			if (!player.getSkillList().getAllSkills()[i].isStigma())
			{
				count++;
			}
		}
		
		return count;
	}
	
	private int countStigmaSkills(Player player)
	{
		int count = 0;
		
		for (int i = 0; i < player.getSkillList().getAllSkills().length; i++)
		{
			if (player.getSkillList().getAllSkills()[i].isStigma())
			{
				count++;
			}
		}
		
		return count;
	}
	
	@Override
	public void executeCommand(Player player, String params)
	{
		if (!CustomConfig.SKILL_AUTOLEARN)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
			return;
		}
		
		int bStigmaCount = countStigmaSkills(player);
		int bSkillCount = countSkills(player);
		
		SkillLearnService.addMissingSkills(player);
		
		int aStigmaCount = countStigmaSkills(player);
		int aSkillCount = countSkills(player);
		
		if (CustomConfig.STIGMA_AUTOLEARN)
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, aSkillCount - bSkillCount, aStigmaCount - bStigmaCount));
		}
		else
		{
			PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, aSkillCount - bSkillCount));
		}
	}
}