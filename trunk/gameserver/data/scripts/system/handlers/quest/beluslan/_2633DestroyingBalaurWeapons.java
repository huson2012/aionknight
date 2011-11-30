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

package quest.beluslan;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class _2633DestroyingBalaurWeapons extends QuestHandler {
	private final static int	questId	= 2633;
	private final static int[] npc_ids = {204700, 204807, 700296};
	
	public _2633DestroyingBalaurWeapons() {
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204700).addOnQuestStart(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
		qe.setNpcQuestData(213933).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env) {
	final Player player = env.getPlayer();
	int targetId = 0;
	if (env.getVisibleObject() instanceof Npc)
	targetId = ((Npc) env.getVisibleObject()).getNpcId();
	QuestState qs = player.getQuestStateList().getQuestState(questId);
	if(targetId == 204700)
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
		if(targetId == 204700)
		{
			if (env.getDialogId() == -1)
				return sendQuestDialog(env, 10002);
			else if (env.getDialogId() == 1009)
				return sendQuestDialog(env, 5);
			else return defaultQuestEndDialog(env);
		}
	}
	else if(qs.getStatus() != QuestStatus.START)
	{
		return false;
	}
		if(targetId == 204807)
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
		else if(targetId == 700296)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 1)
					{
                		final int targetObjectId = env.getVisibleObject().getObjectId();
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);	

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
                    				}
                				}, 3000);
            			}
				return false;
			}
		}			
		return false;
   	}

	@Override
	public boolean onKillEvent(QuestCookie env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() != QuestStatus.START)
			return false;
		switch(targetId)
		{
			case 213933:
				if(var == 2)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
		}
		return false;
	}
}