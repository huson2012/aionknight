/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
 *
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова)
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package quest.altgard;

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
 * @author Vincas
 */
public class _2294IronWarriorAndScout extends QuestHandler
{

	private final static int questId = 2294;
	static QuestsData questsData = DataManager.QUEST_DATA;

	public _2294IronWarriorAndScout()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		if(!CustomConfig.ENABLE_DAILYCOIN_EX)
		{
			qe.setNpcQuestData(203659).addOnQuestStart(questId);
			qe.setNpcQuestData(203659).addOnTalkEvent(questId);
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
		if(targetId == 203659)
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
					if(player.getInventory().getItemCountByItemId(186000006) >= 2)
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
					if(player.getInventory().getItemCountByItemId(186000006) >= 4)
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
