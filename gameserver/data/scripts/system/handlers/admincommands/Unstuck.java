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
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Unstuck extends AdminCommand
{
	public Unstuck()
	{
		super("unstuck");
	}

	/**
	* Execute admin command represented by this class, with a given list of parametrs.
	*
	* @param admin the player of the admin that requests the command
	* @param params the parameters of the command
	*/

	@Override
	public void executeCommand(Player admin, String[] params) 
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_UNSTUCK)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		if (admin.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendMessage(admin, "You cant execute this command while you are dead");
			return;
		}
		TeleportService.moveToBindLocation(admin, true, CustomConfig.UNSTUCK_DELAY);
	}
}