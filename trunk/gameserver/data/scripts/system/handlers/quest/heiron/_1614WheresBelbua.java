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
package quest.heiron;


import ru.aionknight.gameserver.model.gameobjects.Npc;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.utils.MathUtil;
import ru.aionknight.gameserver.utils.PacketSendUtility;

/**
 * @author Rhys2002 @modifie Leunam
 * 
 */
public class _1614WheresBelbua extends QuestHandler {
	private final static int questId = 1614;
	private final static int[] npc_ids ={ 204519, 204645 };
	
	public _1614WheresBelbua() {
		super(questId);
	}
	
	@Override
	public void register() {
		qe.setNpcQuestData(204519).addOnQuestStart(questId);
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
		if(targetId == 204519)
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
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 204645:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(var == 1)
							{
								Npc npc = (Npc)env.getVisibleObject();
								if (MathUtil.getDistance(376, 529, 133, npc.getX(), npc.getY(), npc.getZ()) > 15)
								{
									if(!npc.getMoveController().isScheduled())
										npc.getMoveController().schedule();
									npc.getMoveController().followTarget(4);
									return true;
								}
								else
								{
									qs.setQuestVarById(0, var + 1);
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);	
									npc.getController().onDie(null);
									npc.getController().onDespawn(false);								
									return true;
								}
							}
						}
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							if(var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);	
								Npc npc = (Npc)env.getVisibleObject();
								npc.getMoveController().setDistance(4);
								npc.getMoveController().followTarget(4);
								npc.getMoveController().schedule();						
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
								return true;
							}
					}
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204519)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}	
		return false;
	}
}
