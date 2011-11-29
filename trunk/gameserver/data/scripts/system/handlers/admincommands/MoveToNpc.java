/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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
