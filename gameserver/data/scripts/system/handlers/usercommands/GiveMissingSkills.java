/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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
	 * Запрос на получение отсутсвующих умений по команде //givemissingskills
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