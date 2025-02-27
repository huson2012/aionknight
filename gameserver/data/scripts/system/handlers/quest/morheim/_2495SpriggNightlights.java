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


import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**
 * @author Atomics
 */
public class _2495SpriggNightlights extends QuestHandler
{
	private final static int	questId	= 2495;

	public _2495SpriggNightlights()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798125).addOnQuestStart(questId);
		qe.setNpcQuestData(798125).addOnTalkEvent(questId);
		qe.setNpcQuestData(700317).addOnTalkEvent(questId); // jaune
		qe.setNpcQuestData(700319).addOnTalkEvent(questId); // rouge
		qe.setNpcQuestData(700318).addOnTalkEvent(questId); // vert
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if(player.getCommonData().getLevel() < 21)
			return false;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(targetId == 798125)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.START)
			{
				long itemCount;
				long itemCount1;
				long itemCount2;
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 0)
				{
					return sendQuestDialog(env, 2375);
				}
				else if(env.getDialogId() == 34 && qs.getQuestVarById(0) == 0)
				{
					itemCount = player.getInventory().getItemCountByItemId(182204227);
					itemCount1 = player.getInventory().getItemCountByItemId(182204228);
					itemCount2 = player.getInventory().getItemCountByItemId(182204229);
					if(itemCount > 4 && itemCount1 > 4 && itemCount2 > 4)
					{
						player.getInventory().removeFromBagByItemId(182204227, 5);
						player.getInventory().removeFromBagByItemId(182204228, 5);
						player.getInventory().removeFromBagByItemId(182204229, 5);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else
					{
						return sendQuestDialog(env, 2716);
					}
				}
				else
					return defaultQuestEndDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.REWARD)
			{
				if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 5)
				{
					return sendQuestDialog(env, 5);
				}
				else if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 6)
				{
					return sendQuestDialog(env, 6);
				}
				else if(env.getDialogId() == 26 && qs.getQuestVarById(0) == 7)
				{
					return sendQuestDialog(env, 7);
				}
				else
				{
					return defaultQuestEndDialog(env);
				}
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(targetId == 700317)
		{
			long itemCount;
			itemCount = player.getInventory().getItemCountByItemId(182204227);
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0 && itemCount < 5)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
							targetObjectId), true);
					}
				}, 3000);
				return true;
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(targetId == 700318)
		{
			long itemCount1;
			itemCount1 = player.getInventory().getItemCountByItemId(182204228);
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0 && itemCount1 < 5)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
							targetObjectId), true);
					}
				}, 3000);
				return true;
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else if(targetId == 700319)
		{
			long itemCount2;
			itemCount2 = player.getInventory().getItemCountByItemId(182204229);
			if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0 && itemCount2 < 5)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
					targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
							targetObjectId), true);
					}
				}, 3000);
				return true;
			}
			else
			{
				return defaultQuestEndDialog(env);
			}
		}
		else
		{
			return defaultQuestEndDialog(env);
		}
	}

}
