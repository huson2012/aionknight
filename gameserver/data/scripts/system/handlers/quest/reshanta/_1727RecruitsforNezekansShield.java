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
package quest.reshanta;



import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;


/**
 * @author Hilgert
 * 
 */
public class _1727RecruitsforNezekansShield extends QuestHandler
{
	private final static int	questId	= 1727;

	public _1727RecruitsforNezekansShield()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(278520).addOnQuestStart(questId);
		qe.setNpcQuestData(278579).addOnTalkEvent(questId);
		qe.setNpcQuestData(278574).addOnTalkEvent(questId);
		qe.setNpcQuestData(278604).addOnTalkEvent(questId);
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
		    if(targetId == 278520)
		    {
				if(env.getDialogId() == 26)
				     return sendQuestDialog(env, 1011);
			    else 
				     return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			if(targetId == 278579)
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
			else if(targetId == 278574)
			{
				if(env.getDialogId() == 26)
					 return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 10001)
				{
				     qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					 qs.setStatus(QuestStatus.REWARD);
					 updateQuestStatus(env);
					 PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					 return true;
				}
			}		
		}
		else if(qs.getStatus() == QuestStatus.REWARD && targetId == 278604)
		{
			return defaultQuestEndDialog(env);
		}
		return false;
    }		
}
