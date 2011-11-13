/**
 * This file is part of Aion-Knight.
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
package quest.kromedes_trial;


import gameserver.dataholders.DataManager;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;


public class _28623KaligasCollection extends QuestHandler {
	private final static int questId = 28623;

	public _28623KaligasCollection() {
		super(questId);
	}

	@Override
	public boolean onActionItemEvent(QuestCookie env) {
		if(env.getPlayer().getTribe().equals("PC_DARK"))
		{
			int targetId = 0;
			if (env.getVisibleObject() instanceof Npc)
				targetId = ((Npc) env.getVisibleObject()).getNpcId();
			return (targetId == 730331);
		} else {
			return false;
		}
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) {
		if(env.getPlayer().getTribe().equals("PC_DARK"))
		{
			final Player player = env.getPlayer();
			int targetId = 0;
			if (env.getVisibleObject() instanceof Npc)
				targetId = ((Npc) env.getVisibleObject()).getNpcId();
			QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (targetId == 730331) 
			{
				PlayerClass playerClass = player.getCommonData().getPlayerClass();
				if (playerClass == PlayerClass.TEMPLAR || playerClass == PlayerClass.CHANTER || playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.MAGE || playerClass == PlayerClass.PRIEST || playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.WARRIOR || playerClass == PlayerClass.SCOUT) 
				{
					if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompleteCount() <= template.getMaxRepeatCount()))) 
					{
						if (env.getDialogId() == -1)
							return sendQuestDialog(env, 1011);
						else
							return defaultQuestStartDialog(env);
					}  
					else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) 
					{
						if (env.getDialogId() == -1)
							return sendQuestDialog(env, 2375);
						else if (env.getDialogId() == 34) 
						{
							if (player.getInventory().getItemCountByItemId(185000102) >= 1) 
							{
								player.getInventory().removeFromBagByItemId(185000102, 1);
								qs.setStatus(QuestStatus.REWARD);
								qs.setQuestVar(1);
								qs.setCompliteCount(0);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							} 
							else
								return sendQuestDialog(env, 2716);
						}
					}  
					else if (qs != null && qs.getStatus() == QuestStatus.REWARD) 
					{
						int var = qs.getQuestVarById(0);
						switch (env.getDialogId()) 
						{
							case -1:
								if (var == 1)
									return sendQuestDialog(env, 5);
							case 17:
								QuestService.questFinish(env, qs.getQuestVars().getQuestVars() - 1);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
				}
				return false;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public void register() {
		qe.setNpcQuestData(730331).addOnQuestStart(questId);
		qe.setNpcQuestData(730331).addOnActionItemEvent(questId);
		qe.setNpcQuestData(730331).addOnTalkEvent(questId);
	}
}
