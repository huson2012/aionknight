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

package usercommands;

import gameserver.configs.main.CustomConfig;
import gameserver.model.account.Account;
import gameserver.model.gameobjects.player.AbyssRank;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_INGAMESHOP_BALANCE;
import gameserver.services.CashShopManager;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;

public class ExchangeToll extends UserCommand {
    public ExchangeToll() {
        super("exchangetoll");
    }

    //Look up information only once for this instance of the command
    private int apExchangeRate = CustomConfig.TOLL_EXCHANGE_AP_RATE;
    private int kinahExchangeRate = CustomConfig.TOLL_EXCHANGE_KINAH_RATE;
    private String exchangeRestriction = CustomConfig.TOLL_EXCHANGE_RESTRICTION.toLowerCase();

    @Override
    public void executeCommand(Player player, String params) {
            if (player == null) {
                    //This should not happen!
                    return;
            }

        if (!CustomConfig.TOLL_EXCHANGE_ENABLED) {
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
            return;
        }

        //Verify exchangeRestriction is set.  An invalid parameter will cause it to default to none (no restriction on exchange)
        if (exchangeRestriction == null && (!exchangeRestriction.equals("none") && !exchangeRestriction.equals("ap") && !exchangeRestriction.equals("kinah")))
                exchangeRestriction = "none";

        if (params == null || params == "") {
                if (exchangeRestriction.equals("kinah"))
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_KINAH_SYNTAX, kinahExchangeRate));
                else if (exchangeRestriction.equals("ap"))
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_AP_SYNTAX, apExchangeRate));
                else
                        PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, apExchangeRate, kinahExchangeRate));
                return;
        }

        int shopMoneyToGain = 0;

        if (params.toLowerCase().indexOf("ap") != -1) {
                if (exchangeRestriction.equals("kinah")) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, kinahExchangeRate));
                    return;
                }

            String[] args = params.split(" ");

                AbyssRank rank = player.getAbyssRank();
                int currentAP = rank.getAp();

                int apToExchange = calculateAPToExchange(player, args, currentAP);
                if (apToExchange == -1)
                        return;
                if (apToExchange < 0)
                        return;
                else if (apToExchange == 0) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_AP, Integer.parseInt(args[1]), apExchangeRate));
                        return;
                }

                shopMoneyToGain = (apToExchange / apExchangeRate);

                player.getCommonData().setAp(currentAP - apToExchange);
        } else {
                //Exchange Amount into ShopMoney (default is Kinah, but overridden if restriction is AP
                int baseToExchange = Integer.parseInt(params);
                if (exchangeRestriction.equals("ap")) {
                    AbyssRank rank = player.getAbyssRank();
                    int currentAP = rank.getAp();

                    int apToExchange = calculateAPToExchange(player, baseToExchange, currentAP);
                    if (apToExchange == -1)
                            return;
                    if (apToExchange < 0)
                            return;
                    else if (apToExchange == 0) {
                        PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_AP, baseToExchange, apExchangeRate));
                            return;
                    }

                    shopMoneyToGain = (apToExchange / apExchangeRate);

                    player.getCommonData().setAp(currentAP - apToExchange);
                } else {
                        int kinahToExchange = calculateKinahToExchange(player, baseToExchange);
                        if (kinahToExchange == -1)
                                return;
                        if (kinahToExchange < 0)
                                return;
                        else if (kinahToExchange == 0) {
                                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_KINAH, baseToExchange, kinahExchangeRate));
                                return;
                        }

                        shopMoneyToGain = (kinahToExchange / kinahExchangeRate);

                        player.getInventory().decreaseKinah(kinahToExchange);
                }
        }

        Account playerAccount = player.getPlayerAccount();
        if (playerAccount != null) {
		int currentShopMoney = playerAccount.getShopMoney();
                playerAccount.SetShopMoney(currentShopMoney + shopMoneyToGain);
        }

            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SUCCESS, shopMoneyToGain));
	    CashShopManager.getInstance().increaseCredits(player, shopMoneyToGain);		
            PacketSendUtility.sendPacket(player, new SM_INGAMESHOP_BALANCE());
    }

    private int calculateAPToExchange(Player player, int apToExchange, int accountAP) {
            if (accountAP < apToExchange) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP));
                    return -1;
            }

            //Only convert in even numbers and don't steal from the player!
            int difference = apToExchange % apExchangeRate;
            apToExchange -= difference;

            return apToExchange;
    }

    private int calculateAPToExchange(Player player, String[] args, int accountAP) {
            //Exchange AP into ShopMoney.
            if (args.length < 2) {
                    //Must specify the amount of AP to use
                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, apExchangeRate, kinahExchangeRate));
                return -1;
            }

            int apToExchange = Integer.parseInt(args[1]);
            if (accountAP < apToExchange) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP));
                    return -1;
            }

            //Only convert in even numbers and don't steal from the player!
            int difference = apToExchange % apExchangeRate;
            apToExchange -= difference;

            return apToExchange;
    }

    private int calculateKinahToExchange(Player player, int kinahToExchange) {
            //Does the player even have the Kinah specified?
            if (player.getInventory().getKinahCount() < kinahToExchange) {
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_KINAH));
                    return -1;
            }

            //Only convert in even numbers and don't steal from the player!
            int difference = kinahToExchange % kinahExchangeRate;
            kinahToExchange -= difference;

            return kinahToExchange;
    }
}