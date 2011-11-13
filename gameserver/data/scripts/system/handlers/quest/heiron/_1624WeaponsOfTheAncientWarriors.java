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
package quest.heiron;


import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * @author Orpheo
 */
 
public class _1624WeaponsOfTheAncientWarriors extends QuestHandler
{
	private final static int	questId	= 1624;
	
	public _1624WeaponsOfTheAncientWarriors()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204593).addOnQuestStart(questId);
		qe.setNpcQuestData(204593).addOnTalkEvent(questId);
		qe.setNpcQuestData(700201).addOnTalkEvent(questId);
		qe.setNpcQuestData(700202).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204593)
			{
				if(env.getDialogId() == 26)
				{
					return sendQuestDialog(env, 1011);
				}
				else return defaultQuestStartDialog(env);
			}
		}
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 204593:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201753);
							long itemCount2 = player.getInventory().getItemCountByItemId(182201754);
							if(itemCount1 >= 3 && itemCount2 >= 3)
							{
								player.getInventory().removeFromBagByItemId(182201753, 3);
								player.getInventory().removeFromBagByItemId(182201754, 3);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return defaultQuestEndDialog(env);
							}
						}
					}
				}
				case 700201:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount3 = player.getInventory().getItemCountByItemId(182201753);
							if(itemCount3 < 3)
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
				case 700202:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount3 = player.getInventory().getItemCountByItemId(182201754);
							if(itemCount3 < 3)
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
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204593)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
		}	
		return false;
	}
}