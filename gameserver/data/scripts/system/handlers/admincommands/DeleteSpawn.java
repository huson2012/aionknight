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
			PacketSendUtility.sendMessage(admin, "Не был взят тагрет спавна.");
			return;
		}
		
		Npc npc = (Npc) cre;
        SpawnTemplate template = npc.getSpawn();
        int spawnId = DAOManager.getDAO(SpawnDAO.class).isInDB(template.getSpawnGroup().getNpcid(), template.getX(), template.getY());
        DAOManager.getDAO(SpawnDAO.class).deleteSpawn(spawnId);
		
        DataManager.SPAWNS_DATA.removeSpawn(npc.getSpawn());
        npc.getController().delete();
        PacketSendUtility.sendMessage(admin, "Спавн был удален!");
	}
}