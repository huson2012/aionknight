/** * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General  Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed  in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General  Public License for more details.
 *
 *  You should have received a copy of the GNU General  Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */

package quest.inggison;


import ru.aionknight.gameserver.model.gameobjects.Item;
import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.quest.QuestItems;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.services.ItemService;
import ru.aionknight.gameserver.services.QuestService;
import ru.aionknight.gameserver.utils.PacketSendUtility;

/**
 * @author Fennek
 * 
 */

public class _30206DragonboundDiaries	extends QuestHandler
{
	private final static int questId = 30206;
	
	public _30206DragonboundDiaries()
	{
		super (questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(798941).addOnQuestStart(questId); //Pilomenes start
		qe.setNpcQuestData(798941).addOnTalkEvent(questId); // Pilomenes
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		//Instanceof
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		// ------------------------------------------------------------
        // NPC Quest :
		// Pilomenes start
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 798941) //Pilomenes start
			{
				switch (env.getDialogId())
				{
				case 26:
					return sendQuestDialog(env, 1011);
				case 1008:
					return sendQuestDialog(env, 2375);
				case 34:
					if (player.getInventory().getItemCountByItemId(182209608) < 1)
					{
						return sendQuestDialog(env, 2716);
					}
					player.getInventory().removeFromBagByItemId(182209608, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
			}
			return false;
		}
		return false;
	}
}
