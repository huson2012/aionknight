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

package quest.theobomos;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

/**
 * @author Orpheo
 */
 
public class _3096ExamineTheExtractionDevices extends QuestHandler
{
	private final static int	questId	= 3096;
	
	public _3096ExamineTheExtractionDevices()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798225).addOnQuestStart(questId);
		qe.setNpcQuestData(798225).addOnTalkEvent(questId);
		qe.setNpcQuestData(700423).addOnTalkEvent(questId);
		qe.setNpcQuestData(700424).addOnTalkEvent(questId);
		qe.setNpcQuestData(700425).addOnTalkEvent(questId);
		qe.setNpcQuestData(700426).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)  
		{
			if(targetId == 798225)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 1011);
					}
					default: return defaultQuestStartDialog(env);
				}
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 798225:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182208067);
							long itemCount2 = player.getInventory().getItemCountByItemId(182208068);
							long itemCount3 = player.getInventory().getItemCountByItemId(182208069);
							long itemCount4 = player.getInventory().getItemCountByItemId(182208070);
							if(itemCount1 >= 1 && itemCount2 >= 1 && itemCount3 >= 1 && itemCount4 >= 1)
							{
								return sendQuestDialog(env, 5);
							}
						}
						case 17:
						{
							qs.setStatus(QuestStatus.COMPLETE);
							qs.setCompliteCount(qs.getCompleteCount() + 1);
							player.getInventory().removeFromBagByItemId(182208067, 1);
							player.getInventory().removeFromBagByItemId(182208068, 1);
							player.getInventory().removeFromBagByItemId(182208069, 1);
							player.getInventory().removeFromBagByItemId(182208070, 1);
							int rewardExp = player.getRates().getQuestXpRate() * 523900;
							int rewardKinah = player.getRates().getQuestXpRate() * 30000;
							ItemService.addItems(player, Collections.singletonList(new QuestItems(182400001, rewardKinah)));
							player.getCommonData().addExp(rewardExp);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(5, questId, QuestStatus.COMPLETE, 2));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							updateQuestStatus(env);
							return true;
						}
					}
				}
				case 700423:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount = player.getInventory().getItemCountByItemId(182208067);
							if(itemCount < 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700424:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount = player.getInventory().getItemCountByItemId(182208068);
							if(itemCount < 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700425:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount = player.getInventory().getItemCountByItemId(182208069);
							if(itemCount < 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700426:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount = player.getInventory().getItemCountByItemId(182208070);
							if(itemCount < 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}