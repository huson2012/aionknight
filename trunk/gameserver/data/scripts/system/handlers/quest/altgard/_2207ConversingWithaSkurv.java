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

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

public class _2207ConversingWithaSkurv extends QuestHandler
{
	private final static int	questId	= 2207;

	public _2207ConversingWithaSkurv()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203590).addOnQuestStart(questId);
		qe.setNpcQuestData(203590).addOnTalkEvent(questId);
		qe.setNpcQuestData(203591).addOnTalkEvent(questId);
		qe.setNpcQuestData(203557).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203590)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if(ItemService.addItems(player, Collections.singletonList(new QuestItems(182203257, 1))))
						return defaultQuestStartDialog(env);
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		if(qs == null)
			return false;
		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203591)
			{
				if(var == 0)
				{
					if(env.getDialogId() == 26)
						return sendQuestDialog(env, 1352);
					else if(env.getDialogId() == 10000)
					{
						qs.setQuestVar(1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				}
				if(var == 2 || var == 3)
				{
					if(env.getDialogId() == 26)
						return sendQuestDialog(env, 2375);
					else if(env.getDialogId() == 1009)
					{
						qs.setQuestVar(3);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return defaultQuestEndDialog(env);
					}
					else
						return defaultQuestEndDialog(env);
				}
			}
			else if(targetId == 203557)
			{
				if(var == 1)
				{
					if(env.getDialogId() == 26)
						return sendQuestDialog(env, 1693);
					else if(env.getDialogId() == 10001)
					{
						qs.setQuestVar(2);
						updateQuestStatus(env);
						player.getInventory().removeFromBagByItemId(182203257, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
				}
			}
		}
		return false;
	}
}