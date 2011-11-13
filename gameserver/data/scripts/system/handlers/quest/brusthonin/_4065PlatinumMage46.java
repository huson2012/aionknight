/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package quest.brusthonin;


import gameserver.dataholders.DataManager;
import gameserver.dataholders.QuestsData;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author Orpheo
 */
public class _4065PlatinumMage46 extends QuestHandler {

    static QuestsData questsData = DataManager.QUEST_DATA;
    private final static int questId = 4065;

    public _4065PlatinumMage46() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(205162).addOnQuestStart(questId);
        qe.setNpcQuestData(205162).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        if (player.getLevel() > 46)
            return false;
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        QuestTemplate template = questsData.getQuestById(questId);

        if (targetId == 205162) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE) {
                if (env.getDialogId() == 2) {
                    PlayerClass playerClass = player.getCommonData().getPlayerClass();
                    if (playerClass == PlayerClass.MAGE || playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else {
                        return sendQuestDialog(env, 3739);
                    }
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.START) {
                if (env.getDialogId() == 2) {
                    return sendQuestDialog(env, 1011);
                } else if (env.getDialogId() == 10000) {
                    if (player.getInventory().getItemCountByItemId(186000010) >= 2000) {
                        qs.setQuestVarById(1, 0);
                        player.getInventory().removeFromBagByItemId(186000010, 2000);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 10001) {
                    if (player.getInventory().getItemCountByItemId(186000010) >= 400) {
                        qs.setQuestVarById(1, 1);
                        player.getInventory().removeFromBagByItemId(186000010, 400);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 6);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 10002) {
                    if (player.getInventory().getItemCountByItemId(186000010) >= 1000) {
                        qs.setQuestVarById(1, 2);
                        player.getInventory().removeFromBagByItemId(186000010, 1000);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 7);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                } else if (env.getDialogId() == 10003) {
                    if (player.getInventory().getItemCountByItemId(186000010) >= 200) {
                        qs.setQuestVarById(1, 3);
                        player.getInventory().removeFromBagByItemId(186000010, 200);
                        qs.setStatus(QuestStatus.REWARD);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 8);
                    } else {
                        return sendQuestDialog(env, 1009);
                    }
                }
            } else if (qs.getStatus() == QuestStatus.COMPLETE) {
                if (env.getDialogId() == 2) {
                    if ((qs.getCompleteCount() <= template.getMaxRepeatCount())) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else
                        return sendQuestDialog(env, 1008);
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                return defaultQuestEndDialog(env, qs.getQuestVarById(1));
            }
        }
        return false;
    }
}
