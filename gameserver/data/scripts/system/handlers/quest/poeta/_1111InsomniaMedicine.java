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

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _1111InsomniaMedicine extends QuestHandler
{
	private final static int	questId	= 1111;

	public _1111InsomniaMedicine()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203075).addOnQuestStart(questId);
		int[] npcs = {203075, 203061};
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 203075))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203061)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(var == 0)
							return sendQuestDialog(env, 1352);
						else if(var == 1)
							return sendQuestDialog(env, 1353);
					case 34:
						return defaultQuestItemCheck(env, 0, 1, false, 1353, 1693);
					case 10000:
						return defaultCloseDialog(env, 1, 2, true, false, 182200222, 1, 0, 0);
					case 10001:
						return defaultCloseDialog(env, 1, 3, true, false, 182200221, 1, 0, 0);
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 203075)
			{
				if(env.getDialogId() == -1)
				{
					if(var == 2)
						return sendQuestDialog(env, 2375, 0, 0, 182200222, 1);
					else if(var == 3)
						return sendQuestDialog(env, 2716, 0, 0, 182200221, 1);
				}
				else
					return defaultQuestEndDialog(env, var - 2);
			}
		}
		return false;
	}
}