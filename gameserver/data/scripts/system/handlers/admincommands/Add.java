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
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.AdminCommand;
import gameserver.utils.i18n.CustomMessageId;
import gameserver.utils.i18n.LanguageHandler;
import gameserver.world.World;


public class Add extends AdminCommand
{

        public Add()
        {
                super("add");
        }

        @Override
        public void executeCommand(Player admin, String[] params)
        {
                if (admin.getAccessLevel() < AdminConfig.COMMAND_ADD)
                {
                        PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS));
                        return;
                }

                if (params.length == 0 || params.length > 3)
                {
                        PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.COMMAND_ADD_SYNTAX));
                        return;
                }

                int itemId = 0;
                long itemCount = 1;
                Player receiver = null;

                try
                {
                        itemId = Integer.parseInt(params[0]);

                        if ( params.length == 2 )
                        {
                                itemCount = Long.parseLong(params[1]);
                        }
                        receiver = admin;
                }
                catch (NumberFormatException e)
                {
                        receiver = World.getInstance().findPlayer(Util.convertName(params[0]));

                        if (receiver == null)
                        {
                                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.PLAYER_NOT_ONLINE, Util.convertName(params[0])));
                                return;
                        }

                        try
                        {
                                itemId = Integer.parseInt(params[1]);

                                if ( params.length == 3 )
                                {
                                        itemCount = Long.parseLong(params[2]);
                                }
                        }
                        catch (NumberFormatException ex)
                        {
                                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.INTEGER_PARAMETER_REQUIRED));
                                return;
                        }
                        catch (Exception ex2)
                        {
                                PacketSendUtility.sendMessage(admin, LanguageHandler.translate(CustomMessageId.SOMETHING_WRONG_HAPPENED));
                                return;
                        }
                }
                
                /* Список запрещеных ID Item */
                if (admin.getAccessLevel() == AdminConfig.COMMAND_ADD) {
                        if (itemId == 112600799 || itemId == 112600800 || itemId == 100000729 || itemId == 100000730 || itemId == 100000731 || itemId == 100000732 || itemId == 100000732 || itemId == 100100574 || itemId == 100100575 || itemId == 100200679 || itemId == 100200680 || itemId == 100200681 || itemId == 100500578 || itemId == 100500579 || itemId == 100600616 || itemId == 100600615 || itemId == 100600616 || itemId == 101300544 || itemId == 101500584 || itemId == 101500585 || itemId == 101700600 || itemId == 110100948 || itemId == 110100949 || itemId == 110100950 || itemId == 110100951 || itemId == 110300896 || itemId == 110300897 || itemId == 110300898 || itemId == 110500863 || itemId == 110500864 || itemId == 110500865 || itemId == 110500866 || itemId == 110500864 || itemId == 110600848 || itemId == 110600849 || itemId == 110600850 || itemId == 115000834 || itemId == 115000835 || itemId == 115000836 || itemId == 168000015 || itemId == 168000016 || itemId == 168000017 || itemId == 168000018 || itemId == 168000019 || itemId == 168000020 || itemId == 168000021 || itemId == 168000022 || itemId == 168000023 || itemId == 168000024 || itemId == 168000025 || itemId == 168000026 || itemId == 168000027 || itemId == 111100438 || itemId == 111100442 || itemId == 115000854 || itemId == 100600665 || itemId == 100600669 || itemId == 100600673 || itemId == 100600677 || itemId == 122001156 || itemId == 122001155 || itemId == 122000933 || itemId == 122000934 || itemId == 122000935 || itemId == 122000936 || itemId == 122000937 || itemId == 122000938 || itemId == 122000939 || itemId == 122000940 || itemId == 122000941 || itemId == 122000942 || itemId == 122000943 || itemId == 122000944 || itemId == 122000945 || itemId == 122000946 || itemId == 122000947 || itemId == 122000948 || itemId == 122000949 || itemId == 122000950 || itemId == 122000951 || itemId == 122000952 || itemId == 122000953 || itemId == 122000954 || itemId == 122000955 || itemId == 122000956 || itemId == 122000957 || itemId == 122000958 || itemId == 122000959 || itemId == 122000960 || itemId == 122000961 || itemId == 122000962 || itemId == 122000963 || itemId == 122000964 || itemId == 122000965 || itemId == 122000966 || itemId == 122000967 || itemId == 122000968 || itemId == 122000969 || itemId == 122000970 || itemId == 122000971 || itemId == 122000972 || itemId == 122000973 || itemId == 122000974 || itemId == 122000975 || itemId == 122000976 || itemId == 122000977 || itemId == 122000978 || itemId == 122000979 || itemId == 122000980 || itemId == 122000981 || itemId == 122000982 || itemId == 122000983 || itemId == 122000984 || itemId == 122000985 || itemId == 122000986 || itemId == 122000987 || itemId == 122000988 || itemId == 122000989 || itemId == 122000990 || itemId == 122000991 || itemId == 122000992 || itemId == 122000993 || itemId == 122000994 || itemId == 122000995 || itemId == 122000996 || itemId == 122000997 || itemId == 122000998 || itemId == 122000999 || itemId == 122001000 || itemId == 122001001 || itemId == 122001002 || itemId == 122001003 || itemId == 122001004 || itemId == 122001005 || itemId == 122001006 || itemId == 122001007 || itemId == 122001008 || itemId == 122001009 || itemId == 122001010 || itemId == 122001011 || itemId == 122001012 || itemId == 122001013 || itemId == 122001014 || itemId == 122001015 || itemId == 122001016 || itemId == 122001017 || itemId == 122001018 || itemId == 122001019 || itemId == 122001020 || itemId == 122001021 || itemId == 122001022 || itemId == 122001023 || itemId == 122001024 || itemId == 122001025 || itemId == 122001026 || itemId == 122001027 || itemId == 122001028 || itemId == 122001029 || itemId == 122001030 || itemId == 122001031 || itemId == 122001032 || itemId == 122001166 || itemId == 122001164) {
                                PacketSendUtility.sendMessage(admin, "Предмет[item:"+itemId+"] не может быть добавлен.");
                                return;
						}
                        if (itemId == 123456789 || itemId == 123456789 || itemId == 123456789) {
                                PacketSendUtility.sendMessage(admin, "Предмет[item:"+itemId+"] не может быть добавлен.");
                                return;
                        }
                }
                /* Конец списка */

                long count = ItemService.addItem(receiver, itemId, itemCount);

                if (count == 0)
                {
                        PacketSendUtility.sendMessage(admin, receiver.getName()+" получил предмет [item:"+itemId+"] в количестве "+itemCount+" шт.");
                        PacketSendUtility.sendMessage(receiver, "Получено от администратора "+admin.getName()+ " [item:"+itemId+"] "+ itemCount+" шт.");
                }
                else
                {
                        PacketSendUtility.sendMessage(admin, "Не смог дать [item:"+itemId+"] "+ receiver.getName());
                }
        }
}