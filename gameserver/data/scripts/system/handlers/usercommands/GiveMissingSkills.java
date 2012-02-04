/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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