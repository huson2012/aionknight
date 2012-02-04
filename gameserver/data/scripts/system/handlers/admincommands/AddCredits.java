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
import gameserver.network.aion.serverpackets.SM_INGAMESHOP_BALANCE;
import gameserver.services.CashShopManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

/**
 * Команда на выдачу игроку валюты для оплаты товаров в 'ingameShop'
 */

public class AddCredits extends AdminCommand 
{
    public AddCredits() 
	{
        super("addcredits");
    }
    
    @Override
    public void executeCommand(Player admin, String[] params) {
        if (admin.getAccessLevel() < AdminConfig.COMMAND_ADDCREDITS) {
            PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
            return;
        }
        
        if (params.length != 2) {
            PacketSendUtility.sendMessage(admin, "Syntax: //addcredits <playername> <Quantity>");
            return;
        }
        
        int Credits = 0;
        String name = Util.convertName(params[0]);
        Player player = World.getInstance().findPlayer(name);
        
        if (player == null) {
            PacketSendUtility.sendMessage(admin, "The specified player is not online.");
            return;
        }
        
        try
        {
            Credits = Integer.parseInt(params[1]);
        }
        catch (NumberFormatException e) {
            PacketSendUtility.sendMessage(admin, "<credits> value must be an integer.");
            return;
        }
        
        CashShopManager.getInstance().increaseCredits(player, Credits);
        PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
        PacketSendUtility.sendMessage(admin, name + " Increase " + Credits + " Integral success.");
        PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " You increase " + Credits + " Integral.");
    }
}