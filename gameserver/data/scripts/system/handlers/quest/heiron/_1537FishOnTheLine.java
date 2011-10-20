/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;


import ru.aionknight.gameserver.model.EmotionType;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_EMOTION;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.utils.ThreadPoolManager;


/**
 * @author Nephis and quest helper team
 * 
 */
public class _1537FishOnTheLine extends QuestHandler
{

	private final static int	questId	= 1537;

	public _1537FishOnTheLine()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204588).addOnQuestStart(questId);
		qe.setNpcQuestData(204588).addOnTalkEvent(questId);
		qe.setNpcQuestData(730189).addOnTalkEvent(questId);
		qe.setNpcQuestData(730190).addOnTalkEvent(questId);
		qe.setNpcQuestData(730191).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 204588)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 730189:
				{
					if(qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
							3000, 1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								if(!player.isTargeting(targetObjectId))
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player,
									new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
								{
									qs.setQuestVarById(0, 1);
									updateQuestStatus(env);
								}
							}
						}, 3000);
					}
				}

				case 730190:
				{
					if(qs.getQuestVarById(0) == 1 && env.getDialogId() == -1)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
							3000, 1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								if(!player.isTargeting(targetObjectId))
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player,
									new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
								{
									qs.setQuestVarById(0, 2);
									updateQuestStatus(env);
								}
							}
						}, 3000);
					}
				}

				case 730191:
				{
					if(qs.getQuestVarById(0) == 2 && env.getDialogId() == -1)
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
							3000, 1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								if(!player.isTargeting(targetObjectId))
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player,
									new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
								qs.setQuestVarById(0, 3);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
							}
						}, 3000);
					}
				}
			}
		}

		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204588)
				return defaultQuestEndDialog(env);
		}
		return false;
	}
}