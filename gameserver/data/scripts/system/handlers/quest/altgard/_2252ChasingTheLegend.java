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

package quest.altgard;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _2252ChasingTheLegend extends QuestHandler
{
	private final static int	questId	= 2252;
	private Npc npc;

	public _2252ChasingTheLegend()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203646).addOnQuestStart(questId);
		qe.setNpcQuestData(203646).addOnTalkEvent(questId);
		qe.setNpcQuestData(700060).addOnTalkEvent(questId);
		qe.setNpcQuestData(210634).addOnKillEvent(questId);
		qe.addOnDie(questId);
	}
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(defaultQuestNoneDialog(env, 203646, 182203235, 1))
			return true;
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 203646)
			{
				if(env.getDialogId() == 26)
					if(var == 0)
						return sendQuestDialog(env, 2034);
					else if(var == 1)
					{
						qs.setQuestVar(0);
						updateQuestStatus(env);
						return sendQuestDialog(env, 1693);
					}
			}
			else if(env.getTargetId() == 700060 && env.getDialogId() == -1)
				return defaultQuestUseNpc(env, 0, 1, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
		}
		return defaultQuestRewardDialog(env, 203646, 1352);
	}
	
	@Override
	public boolean onDieEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		if(qs.getQuestVars().getQuestVars() == 0)
		{
			qs.setQuestVar(1);
			updateQuestStatus(env);
			npc.getController().onDelete();
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 210634, 0, 1))
		{
			QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		else
			return false;
	}
	
	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		npc = (Npc)QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 210634, 2407.54f, 1267f, 257.33f, (byte) 47, true);
	}
}