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

import java.util.Collections;

public class _1535TheColdColdGround extends QuestHandler
{

	private final static int	questId	= 1535;

	public _1535TheColdColdGround()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204580).addOnQuestStart(questId);
		qe.setNpcQuestData(204580).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(targetId != 204580)
			return false;
		
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(env.getDialogId() == 26)
				return sendQuestDialog(env, 4762);
			else
				return defaultQuestStartDialog(env);
		}
				
		if(qs.getStatus() == QuestStatus.START)
		{
			boolean abexSkins = player.getInventory().getItemCountByItemId(182201818) > 4;
			boolean worgSkins = player.getInventory().getItemCountByItemId(182201819) > 2;
			boolean karnifSkins = player.getInventory().getItemCountByItemId(182201820) > 0;
			
			switch(env.getDialogId())
			{
				case -1:
				case 26:
					if(abexSkins || worgSkins || karnifSkins)
						return sendQuestDialog(env, 1352);
				case 10000:
					if (abexSkins) {
						qs.setQuestVarById(0, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					break;
				case 10001:
					if (worgSkins) {
					    qs.setQuestVarById(0, 2);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);	
						return sendQuestDialog(env, 6);
					}
					break;
				case 10002:
					if (karnifSkins) {
					    qs.setQuestVarById(0, 3);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 7);
					}
					break;
			}
			return sendQuestDialog(env, 1693);
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			int var = qs.getQuestVarById(0);
			if(var == 1) {
				player.getInventory().removeFromBagByItemId(182201818, 5);
				return defaultQuestEndDialog(env);
			} else if(var == 2) {
				//add Greater Mana Potion x 5
				if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(162000010, 5)))) {
					//check later
					qs.setStatus(QuestStatus.START);
					updateQuestStatus(env);
				} else {
					player.getInventory().removeFromBagByItemId(182201819, 3);
				}
				defaultQuestEndDialog(env);
				return true;
			} else if(var == 3) {
				//add Greater Life Serum x 5
				if(!ItemService.addItems(player, Collections.singletonList(new QuestItems(162000015, 5)))) {
					//check later
					qs.setStatus(QuestStatus.START);
					updateQuestStatus(env);
				} else {
					player.getInventory().removeFromBagByItemId(182201820, 1);
				}
				defaultQuestEndDialog(env);
				return true;
			}
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
		}
		return false;
	}
}