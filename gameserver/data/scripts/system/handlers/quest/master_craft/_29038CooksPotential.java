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

public class _29038CooksPotential extends QuestHandler
{
	private final static int	questId	= 29038;
	private final static int	questStartNpc = 204100;
	private final static int	secondNpc = 204101;
	private final static int[]	recipesItemIds = {152207202, 152207203, 152207204, 152207205};
	private final static int[]	recipesIds = {155007241, 155007242, 155007243, 155007244};
	private final static int[]	products = {182207907, 182207908, 182207909, 182207910};

	public _29038CooksPotential()
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
							else if(var == 1)
								return sendQuestDialog(env, 4081);
							else if(var == 3)
								return sendQuestDialog(env, 1352);
							else if(var == 4)
								return sendQuestDialog(env, 4166);
							else if(var == 6)
								return sendQuestDialog(env, 1693);
							else if(var == 7)
								return sendQuestDialog(env, 4251);
							else if(var == 9)
								return sendQuestDialog(env, 2034);
							else if(var == 10)
								return sendQuestDialog(env, 4336);
						case 10009:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 2, recipesItemIds[0], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10019:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 5, recipesItemIds[1], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10029:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 8, recipesItemIds[2], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
						case 10039:
							if(player.getInventory().decreaseKinah(6500))
								return defaultCloseDialog(env, var, 11, recipesItemIds[3], 1, 0, 0);
							else
								return sendQuestDialog(env, 4400);
					}
					break;
				case questStartNpc:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1097);
							else if(var == 5)
								return sendQuestDialog(env, 1438);
							else if(var == 8)
								return sendQuestDialog(env, 1779);
							else if(var == 11)
								return sendQuestDialog(env, 2120);
						case 10010:
							if(var == 2)
							{
								if(player.getInventory().getItemCountByItemId(products[0]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[0], 1);
									qs.setQuestVar(3);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1182);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[0]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[0]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(1);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
						case 10020:
							if(var == 5)
							{
								if(player.getInventory().getItemCountByItemId(products[1]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[1], 1);
									qs.setQuestVar(6);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1523);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[1]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[1]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(4);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
						case 10030:
							if(var == 8)
							{
								if(player.getInventory().getItemCountByItemId(products[2]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[2], 1);
									qs.setQuestVar(9);
									updateQuestStatus(env);
									return sendQuestDialog(env, 1864);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[2]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[2]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(7);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
						case 10040:
							if(var == 11)
							{
								if(player.getInventory().getItemCountByItemId(products[3]) > 0)
								{
									player.getInventory().removeFromBagByItemId(products[3], 1);
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									return sendQuestDialog(env, 5);
								}
								else
								{
									int checkFailId = 3398;
									if(player.getRecipeList().isRecipePresent(recipesIds[3]))
										checkFailId = 2716;
									else if(player.getInventory().getItemCountByItemId(recipesItemIds[3]) > 0)
										checkFailId = 3057;
									
									if(checkFailId == 3398)
									{
										qs.setQuestVar(10);
										updateQuestStatus(env);
									}
									return sendQuestDialog(env, checkFailId);
								}
							}
							break;
					}
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