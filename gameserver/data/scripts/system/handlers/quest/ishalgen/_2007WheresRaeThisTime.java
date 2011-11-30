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
package quest.ishalgen;


import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 * Fixed by Pashatr, Lucky
 */
public class _2007WheresRaeThisTime extends QuestHandler
{
	private final static int	questId	= 2007;
	private final static int[]	npcs = {203516, 203519, 203539, 203552 ,203554, 700085, 700086, 700087};

	public _2007WheresRaeThisTime()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for (int npc : npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203516:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203519:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
					break;
				case 203539:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
						case 1694:
							return defaultQuestMovie(env, 55);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
					break;
				case 203552:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							return defaultCloseDialog(env, 3, 4);
					}
					break;
				case 203554:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10004:
							return defaultCloseDialog(env, 4, 5);
					}
					break;
				case 700085:
					return defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700086:
					return defaultQuestUseNpc(env, 6, 7, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700087:
					return defaultQuestUseNpc(env, 7, 8, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
			}
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            switch (env.getTargetId()) {
                case 10005:
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 8)
                                return sendQuestDialog(env, 3057);
                        case 1006:
                            PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 171));
                    }
                    break;
            }
        }
		return defaultQuestRewardDialog(env, 203516, 3057);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int[] quests = {2001, 2002, 2003, 2004, 2005, 2006};
		return defaultQuestOnLvlUpEvent(env, quests, true);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		switch(var)
		{
			case 5:
				qs.setQuestVar(6);
				break;
			case 6:
				qs.setQuestVar(7);
				break;
			case 7:
				qs.setQuestVar(8);
				break;					
			case 8:
				defaultQuestMovie(env, 56);
				qs.setStatus(QuestStatus.REWARD);				
				break;
		}
		updateQuestStatus(env);
	}
}