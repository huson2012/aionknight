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
import gameserver.model.ChatType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Say extends AdminCommand
{
	public Say()
	{
		super("say");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		String syntaxCommand = "Syntax: //say <message>";

		if (admin.getAccessLevel() < AdminConfig.COMMAND_SAY)
		{
			 PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command.");
			 return;
		}

		if (params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, syntaxCommand);
			return;
		}

		VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "You must select a target first !");
			return;
		}

		StringBuilder sbMessage = new StringBuilder();

		for (String p : params)
			sbMessage.append(p + " ");

		String sMessage = sbMessage.toString().trim();

		if (target instanceof Player)
		{
			PacketSendUtility.broadcastPacket(((Player) target), new SM_MESSAGE(((Player) target), sMessage, ChatType.NORMAL), true);
		}
		else if (target instanceof Npc)
		{
			// admin is not right, but works
			PacketSendUtility.broadcastPacket(admin, new SM_MESSAGE(((Npc) target).getObjectId(), ((Npc) target).getName(), sMessage, ChatType.NORMAL), true);
		}
	}
}