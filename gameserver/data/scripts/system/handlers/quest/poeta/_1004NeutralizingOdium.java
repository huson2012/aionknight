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

public class _1004NeutralizingOdium extends QuestHandler
{
	private final static int	questId	= 1004;
	private final static int[]	npcs = {203082, 700030, 790001, 203067};

	public _1004NeutralizingOdium()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;

		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203082:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 5)
								return sendQuestDialog(env, 2034);
						case 1013:
							if(var == 0)
								return defaultQuestMovie(env, 19);
						case 10000:	
							return defaultCloseDialog(env, 0, 1);
						case 10002:
							return defaultCloseDialog(env, 5, 0, true, false);
					}
					break;
				case 700030:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 1, 2, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false) || defaultQuestUseNpc(env, 4, 5, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false));
					break;
				case 790001:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1352);
							else if (var == 3)
								return sendQuestDialog(env, 1693);
							else if (var == 11)
								return sendQuestDialog(env, 1694);
						case 10001:
							return defaultCloseDialog(env, 2, 3);
						case 10002:
							return defaultCloseDialog(env, 11, 4, 182200006, 1, 182200005, 1);
						case 34:
							return defaultQuestItemCheck(env, 3, 11, false, 1694, 1779);
					}
					break;
			}
		}
		return defaultQuestRewardDialog(env, 203067, 0);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(var == 1)
		{
			if(defaultQuestGiveItem(env, 182200005, 1))
				qs.setQuestVar(2);
		}
		if(var == 4)
		{
			defaultQuestRemoveItem(env, 182200006, 1);
			qs.setQuestVar(5);
		}
		updateQuestStatus(env);
	}
}