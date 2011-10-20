/*
 * This file is part of Aion-Knight Dev. Team <aion-knight.ru>.
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 */

package quest.esoterrace;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;

/**
 * Author: Dimoalex
 * Modified: Frost
 */

public class _18405MemoriesintheCornerofHisMind extends QuestHandler {
    private final static int questId = 18405;

    public _18405MemoriesintheCornerofHisMind() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799553).addOnTalkEvent(questId);
		qe.setNpcQuestData(799552).addOnTalkEvent(questId);
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
            if (env.getTargetId() == 799553) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 1)
                    return sendQuestDialog(env, 1011);

					case 1009:
                        defaultQuestRemoveItem(env, 188051351, 1);
                    return defaultCloseDialog(env, 0, 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799552, 2375);
    }
}
