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

package quest.daevanion_quest;

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _1989ASagesTeachings extends QuestHandler
{
	private final static int	questId	= 1989;
	private final static int[]	npcs	= {203771, 203704, 203705, 203706, 203707};

	public _1989ASagesTeachings()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203771).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 203771))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);

		if(qs.getStatus() == QuestStatus.START)
		{
			PlayerClass playerClass = player.getCommonData().getPlayerClass();
			switch(env.getTargetId())
			{
				case 203704:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR)
									return sendQuestDialog(env, 1352);
								else
									return sendQuestDialog(env, 1438);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203705:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.ASSASSIN || playerClass == PlayerClass.RANGER)
									return sendQuestDialog(env, 1693);
								else
									return sendQuestDialog(env, 1779);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203706:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER)
									return sendQuestDialog(env, 2034);
								else
									return sendQuestDialog(env, 2120);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				case 203707:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
							{
								if(playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.CHANTER)
									return sendQuestDialog(env, 2375);
								else
									return sendQuestDialog(env, 2461);
							}
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203771:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 2716);
							else if(var == 2)
								return sendQuestDialog(env, 3057);
							else if(var == 3)
							{
								if(player.getCommonData().getDp() < 4000)
									return sendQuestDialog(env, 3484);	
								else
									return sendQuestDialog(env, 3398);
							}
							else if(var == 4)
							{
								if(player.getCommonData().getDp() < 4000)
									return sendQuestDialog(env, 3825);	
								else
									return sendQuestDialog(env, 3739);
							}
						case 1009:
							if(var == 3 || var == 4)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								defaultQuestMovie(env, 105);
								player.getCommonData().setDp(0);
								return sendQuestDialog(env, 5);
							}
						case 10001:
							if(defaultCloseDialog(env, 1, 2))
								return sendQuestDialog(env, 3057);
						case 10003:
							return defaultCloseDialog(env, 2, 3);
						case 10004:
							return defaultCloseDialog(env, 2, 4);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 203771, 0);
	}
}
