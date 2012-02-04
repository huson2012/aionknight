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

import commons.database.dao.DAOManager;
import gameserver.controllers.PlayerController;
import gameserver.dao.PlayerDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.world.World;
import gameserver.world.WorldPosition;

public class Repair extends UserCommand {
	
	public Repair() {
        super("repair");
    }

    @Override
    public void executeCommand(Player player, String arg) {
    	WorldPosition position	= null;
        Player repairPlayer		= null;
    	PlayerCommonData pcd 	= DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(arg);
        int accountId 			= DAOManager.getDAO(PlayerDAO.class).getAccountIdByName(arg);
        
        if(accountId != player.getAccount().getId())
        {
        	PacketSendUtility.sendMessage(player, "You cannot repair that player");
        	return;
        }
        
        if(player.getName().toLowerCase().equalsIgnoreCase(pcd.getName().toLowerCase()))
        {
        	PacketSendUtility.sendMessage(player, "You cannot repair yourself");
        	return;
        }
        
        switch(pcd.getRace())
        {
        	case ELYOS:
        		position = World.getInstance().createPosition(210010000, 1212.9423f, 1044.8516f, 140.75568f, (byte) 32);
        		pcd.setPosition(position, true);
        		break;
        	case ASMODIANS:
        		position = World.getInstance().createPosition(220010000, 571.0388f, 2787.3420f, 299.8750f, (byte) 32);
        		pcd.setPosition(position, true);
        		break;
        }
        
        repairPlayer = new Player(new PlayerController(), pcd, null, player.getAccount());
        
        DAOManager.getDAO(PlayerDAO.class).storePlayer(repairPlayer);
        PacketSendUtility.sendMessage(player, "Player " + pcd.getName() + " was repaired");
    }
}