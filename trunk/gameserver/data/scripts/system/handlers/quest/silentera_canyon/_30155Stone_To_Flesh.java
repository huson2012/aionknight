/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.silentera_canyon;


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

import java.util.Collections;


public class _30155Stone_To_Flesh extends QuestHandler
{
	private final static int questId = 30155;
	
	public _30155Stone_To_Flesh()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 799234) //Nep
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if(targetId == 204433) //Kistig
		{
			if(qs.getStatus() == QuestStatus.START && var == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182209252, 1)))) //Remede de restauration
					{
						qs.setQuestVar(++var);
						updateQuestStatus(env);
						PacketSendUtility
							.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					}
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                case 204304: //Vili
                    switch (env.getDialogId())
					{
                        case 26:
                            return sendQuestDialog(env, 2375);
                         case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        // Collect Remede de restauration (1)
                            if (QuestService.collectItemCheck(env, true))
	          {
                                player.getInventory().removeFromBagByItemId(182209252, 1); //Remede de restauration
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                //
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 204304) //Vili
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
	@Override
	public void register()
	{
	    qe.setNpcQuestData(799234).addOnQuestStart(questId); //Nep
		qe.setNpcQuestData(799234).addOnTalkEvent(questId); //Nep
		qe.setNpcQuestData(204433).addOnTalkEvent(questId); //Kistig
		qe.setNpcQuestData(204304).addOnTalkEvent(questId); //Vili
	}
}