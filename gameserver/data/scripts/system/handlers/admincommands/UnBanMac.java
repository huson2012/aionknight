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

import commons.database.dao.DAOManager;

import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.loginserver.BannedMacManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.utils.Util;

/**
 * @author KID
 * @author drsaluml
 */
public class UnBanMac extends AdminCommand {

	public UnBanMac() {
		super("unbanmac");
	}
	
	@Override
    public void executeCommand(final Player admin, String[] params)
    {
        if(admin.getAccessLevel() < AdminConfig.COMMAND_BANMAC){
        	PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }
        
		if (params.length <= 0) {
				PacketSendUtility.sendMessage(admin, "Syntax: //unbanmac <player>");
				return;
		}
		String name = Util.convertName(params[0]);
		final int playerId = DAOManager.getDAO(PlayerDAO.class).getPlayerIdByName(name);
		
		if(playerId > 0)
		BannedMacManager.getInstance().getMacAddress(playerId);
		
		
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				String macAddress = BannedMacManager.getInstance().loadMacAddress();
				
				if(macAddress != null){
					boolean result = BannedMacManager.getInstance().unbanAddress(macAddress,playerId,
						"uban;mac=" + macAddress + ", playerId : " + playerId + "; admin=" + admin.getName());
					if (result)
						PacketSendUtility.sendMessage(admin, "mac " + macAddress + " has unbanned");
					else
						PacketSendUtility.sendMessage(admin, "mac " + macAddress + " is not banned");
				}else
				{
					PacketSendUtility.sendMessage(admin, "Mac Address was not found!");
				}
			}
		}, 1000);
    }
}



