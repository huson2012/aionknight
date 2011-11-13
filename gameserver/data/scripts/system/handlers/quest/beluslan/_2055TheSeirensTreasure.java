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

package quest.beluslan;

import java.util.Collections;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _2055TheSeirensTreasure extends QuestHandler
{	
	private final static int	questId	= 2055;
	private final static int[]	npc_ids	= { 204768, 204743, 204808 };

	public _2055TheSeirensTreasure()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);	
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204768)
			{
				return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 204768)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
					if(var == 2)
						return sendQuestDialog(env, 1693);	
					if(var == 6)
						return sendQuestDialog(env, 3057);							
				case 1012:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 239));
						break;	
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204310, 1)));
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10002:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 1009:
					if(var == 6)
					{
						player.getInventory().removeFromBagByItemId(182204321, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return defaultQuestEndDialog(env);
					}	
				case 10006:
					if(var == 6)
					{
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 241));
						player.getInventory().removeFromBagByItemId(182204321, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}		
			}
		}
		else if(targetId == 204743)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 1)
						return sendQuestDialog(env, 1352);						
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						player.getInventory().removeFromBagByItemId(182204310, 1);
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204311, 1)));
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}				
			}
		}
		else if(targetId == 204808)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 3)
						return sendQuestDialog(env, 2034);
					if(var == 4)
						return sendQuestDialog(env, 2375);	
					if(var == 5)
						return sendQuestDialog(env, 2716);		
				case 2035:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 240));
					break;		
				case 10003:
					if(var == 3)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						player.getInventory().removeFromBagByItemId(182204311, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10005:
					if(var == 5)
					{
						qs.setQuestVarById(0, var + 1);					
						updateQuestStatus(env);
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204321, 1)));
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
				case 34:
					if(QuestService.collectItemCheck(env, true))				
					{	
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return sendQuestDialog(env, 10000);
					}
					else
						return sendQuestDialog(env, 10001);			
			}
		}
		return false;
	}
}