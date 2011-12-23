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

import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _1205ANewSkill extends QuestHandler
{
	private final static int	questId	= 1205;

	public _1205ANewSkill()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		int[] npcs = {203087, 203088, 203089, 203090};
		qe.addQuestLvlUp(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		if (!lvlCheck)
			return false;
		if(qs != null)
			return false;
		if(QuestService.startQuest(env, QuestStatus.START))
		{
			qs = player.getQuestStateList().getQuestState(questId);
			qs.setStatus(QuestStatus.REWARD);
			switch(PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass()))
			{
				case WARRIOR:
					qs.setQuestVar(1);
					break;
				case SCOUT:
					qs.setQuestVar(2);
					break;
				case MAGE:
					qs.setQuestVar(3);
					break;
				case PRIEST:
					qs.setQuestVar(4);
					break;
			}
			updateQuestStatus(env);
		}
		return true;
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.REWARD)
			return false;		
		switch(PlayerClass.getStartingClassFor(player.getCommonData().getPlayerClass()))
		{
			case WARRIOR:
				return defaultQuestRewardDialog(env, 203087, 1011, 0);
			case SCOUT:
				return defaultQuestRewardDialog(env, 203088, 1352, 1);
			case MAGE:
				return defaultQuestRewardDialog(env, 203089, 1693, 2);
			case PRIEST:
				return defaultQuestRewardDialog(env, 203090, 2034, 3);
		}
		return false;
	}
}