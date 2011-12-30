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

package quest.ascension;

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

public class _2904DispatchtoAltgard extends QuestHandler
{

	private final static int	questId	= 2904;
	public _2904DispatchtoAltgard()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204083).addOnQuestStart(questId);
		qe.setNpcQuestData(204083).addOnTalkEvent(questId);
		qe.setNpcQuestData(204191).addOnTalkEvent(questId);
		qe.setNpcQuestData(203559).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 204083)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if(ItemService.addItems(player, Collections.singletonList(new QuestItems(182207004, 1))))
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
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204191:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1352);
							break;
						case 10000:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
								return true;
							}
					}
				}
				case 203559:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 2375);
							}
					}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203559)
			{
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}