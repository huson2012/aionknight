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
package quest.sanctum;

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
 * @author Rolandas
 * 
 */
public class _3967AndusDyeBox extends QuestHandler
{
	private final static int	questId	= 3967;

	public _3967AndusDyeBox()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798391).addOnQuestStart(questId); // Andu
		qe.setNpcQuestData(798309).addOnTalkEvent(questId);  // Arenzes
		qe.setNpcQuestData(798391).addOnTalkEvent(questId);  // Andu
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		
		QuestState qs2 = player.getQuestStateList().getQuestState(3966);
		if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
			return false;
		
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(targetId == 798391)  // Andu
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if(targetId == 798309) // Arenzes
		{
			if(qs.getStatus() == QuestStatus.START && var == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182206122, 1))))
					{
						qs.setQuestVar(++var);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
					PacketSendUtility
						.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if(targetId == 798391 && qs.getStatus() == QuestStatus.REWARD)  // Andu
		{
			if(env.getDialogId() == -1)
				return sendQuestDialog(env, 2375);
			else if(env.getDialogId() == 1009)
			{
				player.getInventory().removeFromBagByItemId(182206122, 1);
				return defaultQuestEndDialog(env);
			}
			else
				return defaultQuestEndDialog(env);
		}
		return false;
	}
}
