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

public class _1127AncientCube extends QuestHandler
{
	private final static int	questId	= 1127;

	public _1127AncientCube()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		int[] npcs = {798008, 700001};
		qe.setNpcQuestData(798008).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(defaultQuestNoneDialog(env, 798008))
			return true;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 700001:
					if(env.getDialogId() == -1)
						return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false);
				case 798008:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 2375);
						case 34:
							return defaultQuestItemCheck(env, 1, 0, true, 5, 2716);
					}
			}
		}
		return defaultQuestRewardDialog(env, 798008, 0);
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(defaultQuestGiveItem(env, 182200215, 1))
		{
			qs.setQuestVar(1);
			updateQuestStatus(env);
		}
	}
}