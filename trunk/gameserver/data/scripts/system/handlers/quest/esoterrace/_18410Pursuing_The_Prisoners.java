/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * This is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.If not, see <http://www.gnu.org/licenses/>.
 */

package quest.esoterrace;

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

public class _18410Pursuing_The_Prisoners extends QuestHandler
{
	private final static int questId = 18410;

	public _18410Pursuing_The_Prisoners()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(799584).addOnQuestStart(questId);
		qe.setNpcQuestData(799563).addOnTalkEvent(questId);
		qe.setNpcQuestData(798553).addOnTalkEvent(questId);
	}
	
    @Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(targetId == 799584) //Lanuagua.
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
		
		if(targetId == 799563) //Nepion.
		{
			if(qs.getStatus() == QuestStatus.START && var == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 10000)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182215026, 1)))) //Nepion's Image Marble.
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
                case 799553: //Daidra.
                    switch (env.getDialogId())
					{
                        case 265:
                            return sendQuestDialog(env, 2375);
                        case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        // Collect Nepion's Image Marble (1)
                            if (QuestService.collectItemCheck(env, true))
	          {                 
                                player.getInventory().removeFromBagByItemId(182215026, 1); //Nepion's Image Marble.
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                //
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                //
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 799553) //Daidra.
                return defaultQuestEndDialog(env);
        }
        return false;
    }
}