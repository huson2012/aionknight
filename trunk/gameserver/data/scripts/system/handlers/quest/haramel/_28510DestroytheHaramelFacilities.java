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
package quest.haramel;


import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;


/**
 * @author Nephis
 * 
 */
public class _28510DestroytheHaramelFacilities extends QuestHandler
{
	private final static int	questId	= 28510;

	public _28510DestroytheHaramelFacilities()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203649).addOnQuestStart(questId);
		qe.setNpcQuestData(203649).addOnTalkEvent(questId);
		qe.setNpcQuestData(700953).addOnTalkEvent(questId);
		qe.setNpcQuestData(700950).addOnKillEvent(questId); 
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
        targetId = ((Npc) env.getVisibleObject()).getNpcId();

		switch(targetId)
		{
			case 700950:
				if(qs.getQuestVarById(0) < 3)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				} 
		}
			return false;
    }

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 203649)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
		}
		else if(targetId == 700953)
		{
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) >= 3 && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) < 5)
			{	
				if(env.getDialogId() == -1)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
				}
			}
			else if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 5)
			{	
				if(env.getDialogId() == -1)
				{
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}
		}
		return false;
	}
}
