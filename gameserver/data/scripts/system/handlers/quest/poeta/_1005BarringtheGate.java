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

package quest.poeta;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _1005BarringtheGate extends QuestHandler
{

	private final static int	questId	= 1005;

	public _1005BarringtheGate()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		int[] talkNpcs = {203067, 203081, 790001, 203085, 203086, 700080, 700081, 700082, 700083};
		qe.addQuestLvlUp(questId);
		for (int id : talkNpcs)
			qe.setNpcQuestData(id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203067:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							if(var == 8)
								defaultQuestMovie(env, 171);
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203081:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
					break;
				case 790001:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
					break;
				case 203085:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							return defaultCloseDialog(env, 3, 4);
					}
					break;
				case 203086:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10004:
							return defaultCloseDialog(env, 4, 5);
					}
					break;
				case 700081:
					return defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700082:
					return defaultQuestUseNpc(env, 6, 7, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700083:
					return defaultQuestUseNpc(env, 7, 8, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 700080:
					return defaultQuestUseNpc(env, 8, 9, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
			}
			
		}
		return defaultQuestRewardDialog(env, 203067, 2716);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int[] quests = {1001, 1002, 1003, 1004};
		return defaultQuestOnLvlUpEvent(env, quests, true);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		switch(var)
		{
			case 5:
				qs.setQuestVar(6);
				break;
			case 6:
				qs.setQuestVar(7);
				break;
			case 7:
				qs.setQuestVar(8);
				break;
			case 8:
				defaultQuestMovie(env, 21);
				qs.setStatus(QuestStatus.REWARD);
				break;
		}
		updateQuestStatus(env);
	}
}