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

public class _1003IllegalLogging extends QuestHandler
{

	private final static int	questId	= 1003;
	private final static int[]	mob_ids	= {210096, 210149, 210150, 210151, 210092, 210154};
	
	public _1003IllegalLogging()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203081).addOnTalkEvent(questId);
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(210160).addOnKillEvent(questId);
		for (int mob_id : mob_ids)
			qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
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

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203081)
			{
				switch(env.getDialogId())
				{
					case 26:
						if (var == 0)
							return sendQuestDialog(env, 1011);
						else if (var == 13)
							return sendQuestDialog(env, 1352);
					case 10000:
						return defaultCloseDialog(env, 0, 1);
					case 10001:
						return defaultCloseDialog(env, 13, 14);
				}
			}
		}
		return defaultQuestRewardDialog(env, 203081, 0);
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, mob_ids, 1, 13) || defaultQuestOnKillEvent(env, 210160, 14, 16) || defaultQuestOnKillEvent(env, 210160, 16, true))
			return true;
		else
			return false;
	}
}