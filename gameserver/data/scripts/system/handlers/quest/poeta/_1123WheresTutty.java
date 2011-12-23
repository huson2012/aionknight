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
import gameserver.world.zone.ZoneName;

public class _1123WheresTutty extends QuestHandler
{
	private final static int	questId	= 1123;
	
	public _1123WheresTutty()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(790001).addOnTalkEvent(questId);
		qe.setNpcQuestData(790001).addOnQuestStart(questId);
		qe.setQuestEnterZone(ZoneName.Q1123).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(defaultQuestNoneDialog(env, 790001))
			return true;
		if(qs == null)
			return false;
		return defaultQuestRewardDialog(env, 790001, 1352);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.Q1123)
			return false;
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		defaultQuestMovie(env, 11);
		qs.setStatus(QuestStatus.REWARD);
		updateQuestStatus(env);
		return true;
	}
}