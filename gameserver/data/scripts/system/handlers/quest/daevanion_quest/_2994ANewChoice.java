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

package quest.daevanion_quest;

import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _2994ANewChoice extends QuestHandler
{
	private final static int	questId	= 2994;
	private final static int	Items[][] = {{1013, 1034, 1055, 1076, 5103, 1098, 1119, 1140, 1161, 1183},
		{100000723, 100900554, 101300538, 100200673, 101700594, 100100568, 101500566, 100600608, 100500572, 115000826}};

	public _2994ANewChoice()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204077).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int	removeItem = 0;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = 0;
		
		if(qs != null)
			var = qs.getQuestVarById(0);
		
		if(qs == null || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(env.getTargetId() == 204077)
			{
				switch(env.getDialogId())
				{
					case 26:
						return sendQuestDialog(env, 1011);
					case 1013:
					case 1034:
					case 1055:
					case 1076:
					case 5103:
					case 1098:
					case 1119:
					case 1140:
					case 1161:
					case 1183:
						int i = 0;
						for(int id: Items[0])
						{
							if(id == env.getDialogId())
								break;
							i++;
						}
						removeItem = Items[1][i];
						if(qs == null)
						{
							qs = new QuestState(questId, QuestStatus.COMPLETE, 0, 0);
							player.getQuestStateList().addQuest(questId, qs);
						}
						else
							qs.setStatus(QuestStatus.COMPLETE);
						qs.setQuestVar(i);
						
						if(player.getInventory().getItemCountByItemId(removeItem) > 0)
							return sendQuestDialog(env, 1013);
						else
							return sendQuestDialog(env, 1352);
					case 10000:
					case 10001:
					case 10002:
					case 10003:
						if(player.getInventory().getItemCountByItemId(186000041) > 0)
						{
							if(qs == null)
							{
								qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
								player.getQuestStateList().addQuest(questId, qs);
							}
							else
								qs.setStatus(QuestStatus.REWARD);
							defaultQuestRemoveItem(env, Items[1][var], 1);
							defaultQuestRemoveItem(env, 186000041, 1);
							var = env.getDialogId() - 10000;
							qs.setQuestVar(var);
							return sendQuestDialog(env, var + 5);
						}
						else
							return sendQuestDialog(env, 1009);
				}
			}
		}
		
		if(qs == null)
			return false;
		
		return defaultQuestRewardDialog(env, 204077, 0, var);
	}
}
