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
package quest.gelkmaros;


import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;


/**
 * @author HellBoy
 * 
 */
public class _21073ListentoMySongStrigiks extends QuestHandler
{
	private final static int	questId	= 21073;

	public _21073ListentoMySongStrigiks()
	{
		super(questId);		
	}

	@Override
	public void register()
	{
		int[] npcs = {799407, 799408};
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
		qe.setNpcQuestData(799407).addOnQuestStart(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 799407))
			return true;
		
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799408)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
					case 10000:
						return defaultCloseDialog(env, 0, 1, true, false);
				}
			}
		}
		return defaultQuestRewardDialog(env, 799407, 2375);
	}
}