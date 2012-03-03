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
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.BannedMacManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

/**
 * @author Rossdale
 */

public class BanMac extends AdminCommand {

	public BanMac() {
		super("banmac");
	}

    @Override
    public void executeCommand(Player admin, String[] params)
    {
		int time = 0; // Default: infinity
		String targetName = "direct_type";
		String macAddress = null;
		int targetId = 0;
        if(admin.getAccessLevel() < AdminConfig.COMMAND_BANMAC)
        {
        	PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }

		VisibleObject target = admin.getTarget();
		if (target != null && target instanceof Player) {
			if (target.getObjectId() == admin.getObjectId()) {
				PacketSendUtility.sendMessage(admin, "omg, disselect yourself please.");
				return;
			}

			Player targetpl = (Player) target;
			macAddress = targetpl.getClientConnection().getMacAddress();
			targetName = targetpl.getName();
			targetId = targetpl.getObjectId();
			targetpl.getClientConnection().closeNow();
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "Please select target first!!");
			return;
		}

		if (params.length > 0) {
			try {
				time = Integer.parseInt(params[0]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "Syntax: //banmac [time in minutes]");
				return;
			}
		}

		BannedMacManager.getInstance().banAddress(macAddress,targetId, System.currentTimeMillis() + time * 60000,
			"author=" + admin.getName() + ", " + admin.getObjectId() + "; target=" + targetName);
	}
}
