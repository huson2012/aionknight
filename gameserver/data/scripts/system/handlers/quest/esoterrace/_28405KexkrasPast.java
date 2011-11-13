/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.If not, see <http://www.gnu.org/licenses/>.
 */

package quest.esoterrace;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.*;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import java.util.Collections;

public class _28405KexkrasPast extends QuestHandler {
    private final static int questId = 28405;

    public _28405KexkrasPast() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799558).addOnTalkEvent(questId);
		qe.setNpcQuestData(799557).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();

        if (env.getTargetId() == 0)
            return defaultQuestStartItem(env);

        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 799558) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 0) {
                            if (player.getInventory().getItemCountByItemId(182215014) != 0)
                                return sendQuestDialog(env, 1352);
                            else
                                return sendQuestDialog(env, 1354);
                        }
                    case 1353:
                        if (var == 0) {
                            player.getInventory().removeFromBagByItemId(182215014, 1);
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182215025, 1)));
                        }
                        return false;
                    case 10000:
                        if (var == 0) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
			}else if (env.getTargetId() == 799557) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 1)
                            return sendQuestDialog(env, 2375);
                    case 1009:
                        defaultQuestRemoveItem(env, 182215025, 1);
                        return defaultCloseDialog(env, 1, 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799557, 0);
    }
}