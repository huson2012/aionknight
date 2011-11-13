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
package quest.morheim;


import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;


/**
 * @author MrPoke remod By Nephis
 * 
 */
public class _2493BringingUpTayga extends QuestHandler
{
	private final static int	questId	= 2493;

	public _2493BringingUpTayga()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
    	int npcs[] = {204325, 204435, 204436, 204437, 204438};
		qe.setNpcQuestData(204325).addOnQuestStart(questId);
		for(int id: npcs)
			qe.setNpcQuestData(id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 204325, 4762))
			return true;
		
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204435:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10255:
							return defaultCloseDialog(env, 0, 1, true, false);
					}
					break;
				case 204436:
				case 204437:
				case 204438:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1353);
					}
					break;
					
			}
		}
		return defaultQuestRewardDialog(env, 204325, 10002);
	}
}
