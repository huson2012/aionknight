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

package quest.haramel;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _18501ShugoIndemnity extends QuestHandler
{
	private final static int	questId	= 18501;

	public _18501ShugoIndemnity()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(799522).addOnQuestStart(questId);
		qe.setNpcQuestData(799522).addOnTalkEvent(questId);
		qe.setNpcQuestData(799523).addOnTalkEvent(questId);
		qe.setNpcQuestData(700833).addOnTalkEvent(questId);
		qe.setNpcQuestData(700951).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(targetId == 799522)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
			
		else if(targetId == 799523)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2375);
				else if(env.getDialogId() == 34)
				{
					if(QuestService.collectItemCheck(env, true))
					{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
							return sendQuestDialog(env, 5);
					}
					else
						return sendQuestDialog(env, 2716);	
				}
				else
					return defaultQuestStartDialog(env);
			}
			
			else if(qs.getStatus() == QuestStatus.REWARD)
				return defaultQuestEndDialog(env);
		}
		else if(targetId == 700833)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
				return true;
		}
		else if(targetId == 700951)
		{
			if(qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
				return true;
		}
		return false;
	}
}