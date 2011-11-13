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
package quest.eltnen;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;

/**
 * @author Orpheo
 * 
 */
public class _1364JourneytoAgairon extends QuestHandler
{
	private final static int	questId	= 1364;
	
	public _1364JourneytoAgairon()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(203944).addOnQuestStart(questId);
		qe.setNpcQuestData(203944).addOnQuestStart(questId);
		qe.setNpcQuestData(203945).addOnTalkEvent(questId);
		qe.setNpcQuestData(790007).addOnTalkEvent(questId);		
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 203944)
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

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 203945:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(var == 1)
							{
								Npc npc = (Npc)env.getVisibleObject();
								if (MathUtil.getDistance(1890, 1939, 338, npc.getX(), npc.getY(), npc.getZ()) > 8)
								{
									if(!npc.getMoveController().isScheduled())
										npc.getMoveController().schedule();
									npc.getMoveController().followTarget(4);
									return true;
								}
								else
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									npc.getController().onDie(null);
									npc.getController().onDespawn(false);								
								return true;
							}
						}
						case 26:
						{
							if(var == 0)
								return sendQuestDialog(env, 1693);
						}
						case 10000:
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
			if(targetId == 790007)
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