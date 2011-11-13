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
package quest.brusthonin;

import java.util.Collections;


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
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**
 * @author Nephis
 * 
 */
public class _4042MessageinaBottle extends QuestHandler
{
	
	private final static int	questId	= 4042;

	public _4042MessageinaBottle()
	{
		super(questId);
	}
	
    @Override
	public void register()
	{
		qe.setNpcQuestData(730150).addOnQuestStart(questId); //Bottle
		qe.setNpcQuestData(730150).addOnTalkEvent(questId);
		qe.setNpcQuestData(205192).addOnTalkEvent(questId); //Sahnu
		qe.setNpcQuestData(204225).addOnTalkEvent(questId); //Gunter
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

			switch(targetId)
			{
				case 730150:
				{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
							1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
							targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{						
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182209024, 1)));
								if(player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
									targetObjectId), true);
							}
						}, 3000);
				}
				
				case 205192:
				{
					if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 1352);
						else if(env.getDialogId() == 10000)
						{
							ItemService.addItems(player, Collections.singletonList(new QuestItems(182209025, 1)));
							player.getInventory().removeFromBagByItemId(182209024, 1);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility
							.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
						else
							return defaultQuestStartDialog(env);
					}
					
					else if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 2375);
						else if(env.getDialogId() == 1009)
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
								return sendQuestDialog(env, 5);
						}
						else
							return defaultQuestStartDialog(env);
					}
					
					else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
					{
						return defaultQuestEndDialog(env);
					}
				}
				
				case 204225:
				{
					if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 1693);
						else if(env.getDialogId() == 10001)
						{
							player.getInventory().removeFromBagByItemId(182209025, 1);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility
							.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
						else
							return defaultQuestStartDialog(env);
					}
				}
				
				case 0:
				{
					if(env.getDialogId() == 1002)
					{
						QuestService.startQuest(env, QuestStatus.START);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
					}
				}
			}
				return false;
	}	
}
