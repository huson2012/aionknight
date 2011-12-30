/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
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