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

package quest.abyss_entry;

import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.flyring.FlyRingTemplate;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.world.WorldMapType;

public class _1044TestingFlightSkills extends QuestHandler
{
	private final static int questId = 1044;

	public _1044TestingFlightSkills()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203901:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				}
				break;
				case 203930:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(player.getQuestTimerOn())
								return sendQuestDialog(env, 3143);
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 9)
								return sendQuestDialog(env, 1693);
							else if(var == 8)
								return sendQuestDialog(env, 3057);
						case 1353:
							if(var == 1)
							{
								defaultQuestMovie(env, 40);
								return sendQuestDialog(env, 1353);
							}
						case 10001:
							if((var == 1 || var == 8))
							{
								defaultCloseDialog(env, var, 2);
								QuestService.questTimerStart(env, 100);
								return true;
							}
						case 10255:
							if(var == 9)
								return defaultCloseDialog(env, 9, 0, true, false);
					}
				}
				break;
			}
		}
		return defaultQuestRewardDialog(env, 203901, 10002);
	}

	@Override
	public HandlerResult onFlyThroughRingEvent(QuestCookie env, FlyRing ring)
	{
		FlyRingTemplate template = ring.getTemplate();

		if(template.getMap() != WorldMapType.ELTNEN.getId())
			return HandlerResult.UNKNOWN;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return HandlerResult.FAILED;

		int var = qs.getQuestVarById(0);
		if (var < 2 || var > 7)
			return HandlerResult.FAILED;
		
		if(ring.getName().equals("ELTNEN_FORTRESS_210020000_1") && var == 2
			|| ring.getName().equals("ELTNEN_FORTRESS_210020000_2") && var == 3
			|| ring.getName().equals("ELTNEN_FORTRESS_210020000_3") && var == 4
			|| ring.getName().equals("ELTNEN_FORTRESS_210020000_4") && var == 5
			|| ring.getName().equals("ELTNEN_FORTRESS_210020000_5") && var == 6)
		{
			qs.setQuestVarById(0, var + 1);
			updateQuestStatus(env);
		}
		else if(ring.getName().equals("ELTNEN_FORTRESS_210020000_6") && var == 7)
		{
			QuestService.questTimerEnd(env);
			qs.setQuestVarById(0, 9);
			updateQuestStatus(env);
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		qs.setQuestVarById(0, 8);
		updateQuestStatus(env);
		return true;
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203901).addOnTalkEvent(questId);
		qe.setNpcQuestData(203930).addOnTalkEvent(questId);
		qe.addOnQuestTimerEnd(questId);
		qe.addOnFlyThroughRing(questId);
	}
}