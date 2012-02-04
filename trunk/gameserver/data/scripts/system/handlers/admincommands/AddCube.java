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
import gameserver.model.gameobjects.player.Player;
import gameserver.services.CubeExpandService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

/**
 * Команда, позволяющая расширить куб игрока
 */
 
public class AddCube extends AdminCommand
{
	public AddCube()
	{
		super("addcube");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDCUBE)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
			return;
		}

		if (params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_SYNTAX));
			return;
		}

		Player receiver = null;

		receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

		if (receiver == null)
		{
			PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.PLAYER_NOT_ONLINE, Util.convertName(params[0])));
			return;
		}

		if (receiver != null)
		{
			if (receiver.getCubeSize() < 9)
			{
				CubeExpandService.expand(receiver);
				PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_ADMIN_SUCCESS, receiver.getName()));
				PacketSendUtility.sendMessage(receiver, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_PLAYER_SUCCESS, admin.getName()));
			}
			else
			{
				PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADDCUBE_FAILURE, receiver.getName()));
				return;
			}
		}
	}
}