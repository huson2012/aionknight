/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.reshanta;



import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.utils.PacketSendUtility;


/**
 * @author Hilgert
 * 
 */
public class _2725GoneNative extends QuestHandler
{
	private final static int	questId	= 2725;

	public _2725GoneNative()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(278003).addOnQuestStart(questId);
		qe.setNpcQuestData(278003).addOnTalkEvent(questId);
		qe.setNpcQuestData(278091).addOnTalkEvent(questId);
		qe.setNpcQuestData(278086).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
		    if(targetId == 278003)
		    {
				if(env.getDialogId() == 26)
				     return sendQuestDialog(env, 1011);
			    else 
				     return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 278091)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
				{
					 qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}
			else if(targetId == 278086)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 1009)
				{
				     qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					 qs.setStatus(QuestStatus.REWARD);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return defaultQuestEndDialog(env);
				}
			}		
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 278086)
		{
			return defaultQuestEndDialog(env);
		}
		return false;
    }		
}
