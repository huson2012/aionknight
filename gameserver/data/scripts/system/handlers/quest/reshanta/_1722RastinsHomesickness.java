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
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;


/**
 * @author Rhys2002
 * 
 */
public class _1722RastinsHomesickness extends QuestHandler
{
	private final static int	questId	= 1722;
	private final static int[]	npc_ids	= { 278547, 278560, 278517, 278544, 278532, 278539, 278524, 278555, 278567 };

	public _1722RastinsHomesickness()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(278547).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 278547)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);		
		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 278547)
				player.getInventory().removeFromBagByItemId(182202101, 1);
				return defaultQuestEndDialog(env);
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 278560)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}		
		else if(targetId == 278517)
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
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}		
		else if(targetId == 278544)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 2)
						return sendQuestDialog(env, 1693);
				case 1009:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}		
		else if(targetId == 278532)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 3)
						return sendQuestDialog(env, 2034);
				case 10003:
					if(var == 3)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 278539)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 4)
						return sendQuestDialog(env, 2375);
				case 10004:
					if(var == 4)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 278524)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 5)
						return sendQuestDialog(env, 2716);
				case 10005:
					if(var == 5)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 278555)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 6)
						return sendQuestDialog(env, 3057);
				case 10006:
					if(var == 6)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}
		else if(targetId == 278567)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 7)
						return sendQuestDialog(env, 3398);
				case 10255:
					if(var == 7)
					{
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182202101, 1)));
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				return false;
			}
		}	
		return false;
	}
}
