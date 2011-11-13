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

package quest.heiron;

import commons.utils.Rnd;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _1053TheKlawThreat extends QuestHandler
{
	private final static int	questId	= 1053;
	private final static int[]	npc_ids	= { 204583, 204502 };

	public _1053TheKlawThreat()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(700169).addOnKillEvent(questId);
		qe.setNpcQuestData(212120).addOnKillEvent(questId);		
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204502)
				return defaultQuestEndDialog(env);
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 204583)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
					else if(var == 1)
						return sendQuestDialog(env, 1352);
					else if(var == 2)
						return sendQuestDialog(env, 1693);						
				case 34:
					if(var == 1 && QuestService.collectItemCheck(env, true))
						return sendQuestDialog(env, 10000);
					else
						return sendQuestDialog(env, 10001);
				case 1693:
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 186));
					return false;						
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10002:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 2);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}					
					return false;
			}
		}
		return false;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 700169, 3, 4))
		{
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			qs.setQuestVar(3);
			updateQuestStatus(env);
			Rnd queen = new Rnd();
			int spawn = queen.nextInt(5);
			Npc npc = (Npc)env.getVisibleObject();
			if(spawn == 1)
				QuestService.addNewSpawn(210040000, 1, 212120, (float) npc.getX(),(float) npc.getY(), (float) npc.getZ(), (byte) 0, true);
			return true;
		}
		if(defaultQuestOnKillEvent(env, 212120, 3, true))
			return true;
		else
			return false;
	}		
}