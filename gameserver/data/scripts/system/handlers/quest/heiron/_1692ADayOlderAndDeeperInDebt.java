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
package quest.heiron;

import java.util.Collections;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo
 */
 
public class _1692ADayOlderAndDeeperInDebt extends QuestHandler
{
	private final static int	questId	= 1692;
	
	public _1692ADayOlderAndDeeperInDebt()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798386).addOnQuestStart(questId);
		qe.setNpcQuestData(798386).addOnTalkEvent(questId);
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
			if(targetId == 798386)
			{
				if(env.getDialogId() == 26)
				{
					return sendQuestDialog(env, 1011);
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 798386:
				{
					switch(env.getDialogId())
					{
						case 26:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(152000104);
							if(itemCount1 >= 10)
							{
								return sendQuestDialog(env, 5);
							}
						}
						case 17:
						{
							player.getInventory().removeFromBagByItemId(152000104, 1);
							player.getInventory().removeFromBagByItemId(182400001, 1);
							qs.setStatus(QuestStatus.COMPLETE);
							qs.setCompliteCount(1);
							int rewardExp = player.getRates().getQuestXpRate() * 268200;
							int rewardKinah = player.getRates().getQuestXpRate() * 35700;
							ItemService.addItems(player, Collections.singletonList(new QuestItems(182400001, rewardKinah)));
							player.getCommonData().addExp(rewardExp);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(5, questId, QuestStatus.COMPLETE, 2));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							updateQuestStatus(env);
							return true;
						}
					}
				}
			}
		}		
		return false;
	}
}