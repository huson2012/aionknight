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

public class ExchangeToll extends UserCommand 
{
    public ExchangeToll() 
	{
        super("exchangetoll");
    }

    // Просмотр информации возможно только один раз для данного экземпляра команды
    private int apExchangeRate = CustomConfig.TOLL_EXCHANGE_AP_RATE;
    private int kinahExchangeRate = CustomConfig.TOLL_EXCHANGE_KINAH_RATE;
    private String exchangeRestriction = CustomConfig.TOLL_EXCHANGE_RESTRICTION.toLowerCase();

    @Override
    public void executeCommand(Player player, String params) 
	{
            if (player == null) 
			{
                    // Этого не должно случиться!
                    return;
            }

        if (!CustomConfig.TOLL_EXCHANGE_ENABLED) 
		{
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_DISABLED));
            return;
        }

        /**
		 * Проверка установлен ли 'exchangeRestriction'. Если был использован недопустимый параметр, то
		 * по умолчанию будет 'none' (нет ограничений на обмен).
		 */
        if (exchangeRestriction == null && (!exchangeRestriction.equals("none") && !exchangeRestriction.equals("ap") && !exchangeRestriction.equals("kinah")))
                exchangeRestriction = "none";

        if (params == null || params == "") 
		{
                if (exchangeRestriction.equals("kinah"))
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_KINAH_SYNTAX, kinahExchangeRate));
                else if (exchangeRestriction.equals("ap"))
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_AP_SYNTAX, apExchangeRate));
                else
                        PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, apExchangeRate, kinahExchangeRate));
                return;
        }

        int shopMoneyToGain = 0;

        if (params.toLowerCase().indexOf("ap") != -1) 
		{
                if (exchangeRestriction.equals("kinah")) 
				{
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
                else if (apToExchange == 0) 
				{
                    PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_GAINING_TOLL_VIA_AP, Integer.parseInt(args[1]), apExchangeRate));
                        return;
                }

                shopMoneyToGain = (apToExchange / apExchangeRate);

                player.getCommonData().setAp(currentAP - apToExchange);
        } else {
                
                int baseToExchange = Integer.parseInt(params);
                if (exchangeRestriction.equals("ap")) 
				{
                    AbyssRank rank = player.getAbyssRank();
                    int currentAP = rank.getAp();

                    int apToExchange = calculateAPToExchange(player, baseToExchange, currentAP);
                    if (apToExchange == -1)
                            return;
                    if (apToExchange < 0)
                            return;
                    else if (apToExchange == 0) 
					{
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
                        else if (kinahToExchange == 0) 
						{
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

    private int calculateAPToExchange(Player player, int apToExchange, int accountAP) 
	{
        if (accountAP < apToExchange) 
		{
                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP));
            return -1;
        }

        // Конвертировать только в четные числа!
        int difference = apToExchange % apExchangeRate;
        apToExchange -= difference;

        return apToExchange;
    }

    private int calculateAPToExchange(Player player, String[] args, int accountAP) 
	{
        // Обмен AP на ShopMoney.
        if (args.length < 2) 
		{
                // Для использования необходимо указать кол-во AP!
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_SYNTAX, apExchangeRate, kinahExchangeRate));
            return -1;
        }

        int apToExchange = Integer.parseInt(args[1]);
        if (accountAP < apToExchange) 
		{
                PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_AP));
            return -1;
        }

        // Конвертировать только в четные числа!
        int difference = apToExchange % apExchangeRate;
        apToExchange -= difference;

        return apToExchange;
    }

    private int calculateKinahToExchange(Player player, int kinahToExchange) 
	{
        // Игрок не указал кол-во Кинар?
        if (player.getInventory().getKinahCount() < kinahToExchange) 
		{
            PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.COMMAND_EXCHANGETOLL_NOT_ENOUGH_KINAH));
            return -1;
        }

        // Конвертировать только в четные числа!
        int difference = kinahToExchange % kinahExchangeRate;
        kinahToExchange -= difference;

		return kinahToExchange;
    }
}