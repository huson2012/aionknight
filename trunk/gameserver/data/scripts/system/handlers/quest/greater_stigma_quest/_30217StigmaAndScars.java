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

package quest.greater_stigma_quest;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;

public class _30217StigmaAndScars	extends QuestHandler
{
	private final static int questId = 30217;
	
	public _30217StigmaAndScars()
	{
		super (questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		//Instanceof
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		// ------------------------------------------------------------
        // NPC Quest :
		// Reemul start
		if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 798909) // Reemul Start
			{
				if (env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if (qs == null)
			return false;
		
		int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				// Pilomenes
				case 798941:
					if (var == 0)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1011);
							case 10000:
								qs.setQuestVar(1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
					// Faithful Utra summoned
				case 799506:
					if (var == 1)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1352);
							case 10001:
								qs.setQuestVar(2);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
						}
					}
				case 798909:
					if (var == 2)
					{
						switch (env.getDialogId())
						{
							case 26:
								return sendQuestDialog(env, 1693);
							case 34:
								if (player.getInventory().getItemCountByItemId(182209618) < 1)
								{
									return sendQuestDialog(env, 10001);
								}
								else if (player.getInventory().getItemCountByItemId(182209619) < 1)
								{
									return sendQuestDialog(env, 10001);
								}
								player.getInventory().removeFromBagByItemId(182209618, 1);
								player.getInventory().removeFromBagByItemId(182209619, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
						}
					}
				return false;
			}
		}
		return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(798909).addOnQuestStart(questId); //Reemul start
		qe.setNpcQuestData(798941).addOnTalkEvent(questId); //Pilomenes 
		qe.setNpcQuestData(799506).addOnTalkEvent(questId); //Faithful Responded Ultra summoned
		qe.setNpcQuestData(798909).addOnTalkEvent(questId); //Reemul finish
	}
}