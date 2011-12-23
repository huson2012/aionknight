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

package quest.miragent_holy_templar_quest;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _3939PersistenceAndLuck extends QuestHandler
{
	private final static int	questId	= 3939;
	private final static int[]	npcs = {203701, 203780, 203781, 700537, 203752};

	public _3939PersistenceAndLuck()
	{
		super(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		
		if(defaultQuestNoneDialog(env, 203701, 4762))
			return true;

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 203780:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10000:
							return defaultCloseDialog(env, 0, 1);
						case 34:
							return defaultQuestItemCheck(env, 2, 3, false, 10000, 10001, 182206099, 1);
					}
				}	break;
				case 203781:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 1354:
							if(player.getInventory().decreaseKinah(3400000))
								return defaultCloseDialog(env, 1, 2, 122001274, 1, 0, 0);
							else
								return sendQuestDialog(env, 1438);
					}
				} break;
				case 700537:
					if(env.getDialogId() == -1 && player.getEquipment().getEquippedItemsByItemId(122001274).size() > 0)
						return defaultQuestUseNpc(env, 2, 3, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true);
					break;
				case 203752:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10255:
							if(player.getInventory().getItemCountByItemId(186000080) >= 1)
								return defaultCloseDialog(env, 3, 0, true, false, 0, 0, 186000080, 1);
							else
								return sendQuestDialog(env, 2120);
					}
				} break;
			}
		}
		return defaultQuestRewardDialog(env, 203701, 10002);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203701).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}
}
