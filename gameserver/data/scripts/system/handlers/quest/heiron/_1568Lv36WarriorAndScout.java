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
package quest.heiron;

import gameserver.configs.main.CustomConfig;
import gameserver.dataholders.DataManager;
import gameserver.dataholders.QuestsData;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.model.templates.bonus.AbstractInventoryBonus;
import gameserver.model.templates.bonus.CoinBonus;
import gameserver.model.templates.bonus.InventoryBonusType;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

/**
 * @author Orpheo
 */
public class _1568Lv36WarriorAndScout extends QuestHandler
{

	private final static int questId = 1568;
	static QuestsData questsData = DataManager.QUEST_DATA;

	public _1568Lv36WarriorAndScout()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		if(!CustomConfig.ENABLE_DAILYCOIN_EX)
		{
			qe.setNpcQuestData(204571).addOnQuestStart(questId);
			qe.setNpcQuestData(204571).addOnTalkEvent(questId);
			qe.addOnQuestFinish(questId);
			qe.setQuestBonusType(InventoryBonusType.COIN).add(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestTemplate template = questsData.getQuestById(questId);
		if(targetId == 204571)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 2)
				{
					PlayerClass playerClass = player.getCommonData().getPlayerClass();
					PlayerClass startPC = null;
					try
					{
						startPC = PlayerClass.getStartingClassFor(playerClass);
					}
					catch(IllegalArgumentException e)
					{
						startPC = playerClass; // already a start class
					}
					if(startPC == PlayerClass.SCOUT || startPC == PlayerClass.WARRIOR)
					{
						QuestService.startQuest(env, QuestStatus.START);
						return sendQuestDialog(env, 1011);
					}
					else
					{
						return sendQuestDialog(env, 3739);
					}
				}
			}
			else if(qs != null && qs.getStatus() == QuestStatus.START)
			{
				if(env.getDialogId() == 2)
				{
					return sendQuestDialog(env, 1011);
				}
				else if(env.getDialogId() == 1011)
				{
					if(player.getInventory().getItemCountByItemId(186000003) >= 16)
					{
						qs.setQuestVarById(0, 0);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					else
					{
						return sendQuestDialog(env, 1009);
					}
				}
				else if(env.getDialogId() == 1352)
				{
					if(player.getInventory().getItemCountByItemId(186000003) >= 32)
					{
						qs.setQuestVarById(0, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 6);
					}
					else
					{
						return sendQuestDialog(env, 1009);
					}
				}
			}
			else if(qs.getStatus() == QuestStatus.COMPLETE)
			{
				if(env.getDialogId() == 2)
				{
					if((qs.getCompleteCount() <= template.getMaxRepeatCount()))
					{
						QuestService.startQuest(env, QuestStatus.START);
						return sendQuestDialog(env, 1011);
					}
					else
						return sendQuestDialog(env, 1008);
				}
			}
			else if(qs != null && qs.getStatus() == QuestStatus.REWARD){ return defaultQuestEndDialog(env, qs.getQuestVarById(0)); }
		}
		return false;
	}

	@Override
	public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus)
	{
		if(!(bonus instanceof CoinBonus))
			return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			if(index == 0 && qs.getQuestVarById(0) == 0 || index == 1 && qs.getQuestVarById(0) == 1)
				return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}
