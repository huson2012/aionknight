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
import gameserver.services.QuestService;
import gameserver.world.zone.ZoneName;

public class _1100KaliosCall extends QuestHandler
{

	private final static int	questId	= 1100;

	public _1100KaliosCall()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203067).addOnTalkEvent(questId);
		qe.setQuestEnterZone(ZoneName.AKARIOS_VILLAGE).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203067)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1009:
						return defaultCloseDialog(env, 0, 0, true, true);
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(env.getTargetId() == 203067)
			{
				if(env.getDialogId() == 18)
				{
					int[] ids = {1003, 1004, 1005};
					for (int id : ids)
					{
						QuestService.startQuest(new QuestCookie(env.getVisibleObject(), player, id, env.getDialogId()), QuestStatus.LOCKED);
					}
				}
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.AKARIOS_VILLAGE)
			return false;
		if(qs != null)
			return false;
		QuestService.startQuest(env, QuestStatus.START);
		return true;
	}
}