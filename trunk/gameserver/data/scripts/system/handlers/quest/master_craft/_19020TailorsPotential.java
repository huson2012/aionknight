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

package quest.master_craft;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _19020TailorsPotential extends QuestHandler
{
	private final static int	questId	= 19020;
	private final static int	questStartNpc = 203793;
	private final static int	secondNpc = 203794;
	private final static int[]	recipesItemIds = {152201962, 152201963};
	private final static int[]	recipesIds = {155002000, 155002001};

	public _19020TailorsPotential()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, questStartNpc, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case secondNpc:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 2)
								return sendQuestDialog(env, 4080);
						case 10009:
							if(player.getInventory().decreaseKinah(84000))
								return defaultCloseDialog(env, var, 1, recipesItemIds[0], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10019:
							if(player.getInventory().decreaseKinah(111500))
								return defaultCloseDialog(env, var, 1, recipesItemIds[1], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
					}
					break;
				case questStartNpc:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 34:
							if (var == 1)
							{
								if(QuestService.collectItemCheck(env, true))
								{
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									return sendQuestDialog(env, 5);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[0]) || player.getRecipeList().isRecipePresent(recipesIds[1]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[0]) > 0 || player.getInventory().getItemCountByItemId(recipesItemIds[1]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(2);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, questStartNpc, 0);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(questStartNpc).addOnQuestStart(questId);
		qe.setNpcQuestData(questStartNpc).addOnTalkEvent(questId);
		qe.setNpcQuestData(secondNpc).addOnTalkEvent(questId);
	}
}