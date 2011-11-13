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

import java.util.Collections;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;


/**
 * @author Orpheo
 */
 
public class _1643TheStarOfHeiron extends QuestHandler
{
	private final static int	questId	= 1643;
	
	public _1643TheStarOfHeiron()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addOnEnterWorld(questId);
		qe.setNpcQuestData(204545).addOnQuestStart(questId);
		qe.setNpcQuestData(204545).addOnTalkEvent(questId);
		qe.setNpcQuestData(204630).addOnTalkEvent(questId);
		qe.setNpcQuestData(204614).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204545)
			{
				switch(env.getDialogId())
				{
					case 26:
					{
						return sendQuestDialog(env, 4762);
					}
					case 1002:
					{
						if(player.getInventory().getItemCountByItemId(182201764) == 0)
						{
							if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182201764, 1))))
							{
								return true;
							}
						}
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
				case 204630:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if(qs.getQuestVarById(0) == 2)
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							player.getInventory().removeFromBagByItemId(182201764, 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							QuestService.addNewSpawn(210040000, 1, 204614, (float) 1591.4327, (float) 2774.2283, (float) 127.63001, (byte) 0, true);						
							return true;
						}
						case 10255:
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
				case 204614:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							if(qs.getQuestVarById(0) == 1)
							{
								return sendQuestDialog(env, 1011);
							}
						}
						case 10000:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							final Npc npc = (Npc)env.getVisibleObject();
							ThreadPoolManager.getInstance().schedule(new Runnable()
							{
								@Override
								public void run()
								{
									npc.getController().onDelete();	
								}
							}, 40000);								
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204545)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
		}		
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (qs == null)
		{
			return false;
		}
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(qs.getQuestVarById(0) == 1)
			{
				qs.setQuestVar(0);
				updateQuestStatus(env);
			}
		}
		return false;
	}
}