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

package quest.altgard;

import java.util.Collections;
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

public class _2019SecuringtheSupplyRoute extends QuestHandler
{

	private final static int	questId	= 2019;

	public _2019SecuringtheSupplyRoute()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(798033).addOnTalkEvent(questId);
		qe.setNpcQuestData(210492).addOnKillEvent(questId);
		qe.setNpcQuestData(210493).addOnKillEvent(questId);		
		qe.setNpcQuestData(203673).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 798033:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 4)
								return sendQuestDialog(env, 1352);
							break;
						case 10000:
							if (var == 0)
							{
								qs.setQuestVarById(0, var+1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 10001:
							if (var== 4)
							{
								if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182203024, 1))))
									return true;
								qs.setQuestVarById(0, var+1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				case 203673:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 1693);
						case 1009:
							if (var==5)
							{
								player.getInventory().removeFromBagByItemId(182203024, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
					}
					
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203673)
					return defaultQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		int[] mobs = {210492, 210493};
		if(defaultQuestOnKillEvent(env, mobs, 1, 4))
			return true;
		else
			return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
}