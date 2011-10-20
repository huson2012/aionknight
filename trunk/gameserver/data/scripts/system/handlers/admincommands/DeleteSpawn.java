/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;


import ru.aionknight.gameserver.configs.administration.AdminConfig;
import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.VisibleObject;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.chathandlers.AdminCommand;

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
			PacketSendUtility.sendMessage(admin, "Wrong target");
			return;
		}
		Npc npc = (Npc) cre;
		DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
		npc.getController().delete();
		PacketSendUtility.sendMessage(admin, "Spawn removed");
	}
}
