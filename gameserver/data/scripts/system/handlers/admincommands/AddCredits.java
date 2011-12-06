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
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_INGAMESHOP_BALANCE;
import gameserver.services.CashShopManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;

public class AddCredits extends AdminCommand {

    public AddCredits() {
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
