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

package quest.fenris_fangs_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _4938WorkoftheFenrissFangs extends QuestHandler
{
	private final static int	questId	= 4938;
	private final static int[]	npcs = {204053, 798367, 798368, 798369, 798370, 798371, 798372, 798373, 798374, 204075};

	public _4938WorkoftheFenrissFangs()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 204053, 4762, 182207114, 1))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 798367:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
					}
				}	break;
				case 798368:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
					}
				} break;
				case 798369:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10002:
							return defaultCloseDialog(env, 2, 3);
					}
				} break;
				case 798370:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							return defaultCloseDialog(env, 3, 4);
					}
				} break;
				case 798371:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10004:
							return defaultCloseDialog(env, 4, 5);
					}
				} break;
				case 798372:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 2716);
						case 10005:
							return defaultCloseDialog(env, 5, 6);
					}
				} break;
				case 798373:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 6)
								return sendQuestDialog(env, 3057);
						case 10006:
							return defaultCloseDialog(env, 6, 7);
					}
				} break;
				case 798374:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 7)
								return sendQuestDialog(env, 3398);
						case 10007:
							return defaultCloseDialog(env, 7, 8, 182207115, 1, 182207114, 1);
					}
				} break;
				case 204075:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 8)
								return sendQuestDialog(env, 3739);
						case 10255:
							if(player.getInventory().getItemCountByItemId(186000084) >= 1)
								return defaultCloseDialog(env, 8, 0, true, false, 0, 0, 186000084, 1);
							else
								return sendQuestDialog(env, 3825);
					}
				} break;
			}
		}
		return defaultQuestRewardDialog(env, 204053, 10002);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204053).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
}
