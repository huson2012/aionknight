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

package quest.beluslan;

import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2630TheDutifulSpy extends QuestHandler {
	private final static int questId = 2630;

	public _2630TheDutifulSpy() {
		super(questId);
	}

	@Override
	public void register() {
	int[] npcs = {204799, 204777};
	for (int npc : npcs)
		qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	qe.setNpcQuestData(204799).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
	if (defaultQuestNoneDialog(env, 204799))
	return true;
	QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
	if (qs == null)
	return false;
	int var = qs.getQuestVarById(0);
	if (qs.getStatus() == QuestStatus.START) 
	{
            	if (env.getTargetId() == 204777) 
            	{
			switch (env.getDialogId()) 
			{
			case 26:
				if (var == 0)
					return sendQuestDialog(env, 1352);
			case 10000:
				return defaultCloseDialog(env, 0, 1, true, false);
			}
		}
	}
	return defaultQuestRewardDialog(env, 204799, 2375);
    }
}