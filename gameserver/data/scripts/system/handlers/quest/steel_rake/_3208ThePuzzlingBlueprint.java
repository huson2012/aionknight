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

package quest.steel_rake;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _3208ThePuzzlingBlueprint extends QuestHandler
{
	private final static int	questId	= 3208;

	public _3208ThePuzzlingBlueprint()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 730195))
			return true;
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 798026:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
						case 10000:
							return defaultCloseDialog(env, 0, 1, true, false);
					}
				case 203830:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1693);
						case 10001:
							return defaultCloseDialog(env, 1, 2, true, false);
					}
			}
		}
		return defaultQuestRewardDialog(env, 798320, 2375);
	}

	@Override
	public void register()
	{
		int[] npcs = {730195, 798026, 203830, 798320};
		qe.setNpcQuestData(730195).addOnQuestStart(questId);
		for(int npc: npcs)
		qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
}