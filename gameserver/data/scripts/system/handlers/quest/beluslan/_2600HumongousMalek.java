/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
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

package quest.beluslan;

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
 
public class _2600HumongousMalek extends QuestHandler {
	private final static int questId = 2600;
	private final static int[] npc_ids = {204734, 798119, 700512};

	public _2600HumongousMalek() {
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204734).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
	if(targetId == 204734)
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
	if(qs.getStatus() == QuestStatus.REWARD)
	{
		if(targetId == 204734)
		{
			return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
	if(targetId == 798119)
	{
		switch(env.getDialogId())
		{
			case 26:
				if(var == 0)
					return sendQuestDialog(env, 1352);
			case 10000:
				if(var == 0)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182204528, 1))));
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);								
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;

				}
			return false;
		}
	}
	else if(targetId == 700512)
	{
		switch(env.getDialogId())
		{
			case -1:
				if(var == 1)
				{
					if (player.getInventory().getItemCountByItemId(182204528) == 1)
 					{
                				final int targetObjectId = env.getVisibleObject().getObjectId();
						player.getInventory().removeFromBagByItemId(182204528, 1);

                				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
                        		targetObjectId), true);
                				ThreadPoolManager.getInstance().schedule(new Runnable() {
                    				@Override
                    				public void run() {
                        			if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId)
                            			return;
                        			PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId,
                                		3000, 0));
                        			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
                                		targetObjectId), true);
							QuestService.addNewSpawn(220040000, 1, 215383, (float) 1140.78, (float) 432.85, (float) 341.0825, (byte) 0, true);
                    				}
                				}, 3000);
            			}
        			}
			return false;
		}
	}
	else if(targetId == 204734)
	{
		switch(env.getDialogId())
		{
			case 26:
				if(var == 1)
					return sendQuestDialog(env, 2375);
			case 1009:
				if(var == 1)
				{
					player.getInventory().removeFromBagByItemId(182204529, 1);
					qs.setQuestVarById(0, var + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
			return false;
		}
	}
	return false;
   }
}