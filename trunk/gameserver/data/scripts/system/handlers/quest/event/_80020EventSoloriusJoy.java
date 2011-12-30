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

package quest.event;

import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.QuestEngine;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class _80020EventSoloriusJoy extends QuestHandler
{
	private final static int	questId	= 80020;
	private final static int[]	npcs = {799769, 799768, 203170, 203140};

	public _80020EventSoloriusJoy()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if ((qs == null || qs.getStatus() == QuestStatus.NONE) && !onLvlUpEvent(env))
			return false;
		
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());

		if(qs == null || qs.getStatus() == QuestStatus.NONE || 
		   qs.getStatus() == QuestStatus.COMPLETE && qs.getCompleteCount() < template.getMaxRepeatCount())
		{
			if(env.getTargetId() == 799769)
			{
				if(env.getDialogId() == -1 || env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestNoneDialog(env, 799769, 182214012, 1);
			}
			return false;
		}
		
		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START)
		{
			if(env.getTargetId() == 799768)
			{
				if(env.getDialogId() == -1 || env.getDialogId() == 26)
					return sendQuestDialog(env, 1352);
				else if(env.getDialogId() == 10000)
				{
					defaultCloseDialog(env, 0, 1, 182214013, 2, 182214012, 1);
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(env.getTargetId() == 203170 && var == 1)
			{
				if(env.getDialogId() == -1 || env.getDialogId() == 26)
					return sendQuestDialog(env, 1693);
				else if(env.getDialogId() == 1694)
				{
					int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.EMOTE, 7,
						targetObjectId), true);
					return sendQuestDialog(env, 1694);
				}
				else if(env.getDialogId() == 10001)
				{
					defaultCloseDialog(env, 1, 2, 0, 0, 182214013, 1);
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
			else if(env.getTargetId() == 203140 && var == 2)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 2034);
				else if(env.getDialogId() == 2035)
				{
					int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.EMOTE, 30,
						targetObjectId), true);
					return sendQuestDialog(env, 2035);
				}
				else if(env.getDialogId() == 10002)
					return defaultCloseDialog(env, 2, 3, true, false, 0, 0, 0, 182214013, 1);
				else
					return defaultQuestStartDialog(env);
			}
		}

		return defaultQuestRewardDialog(env, 799769, 2375);
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		Calendar now = Calendar.getInstance();
		Calendar cal1 = new GregorianCalendar(now.get(Calendar.YEAR), 11, 15);
		Calendar cal2 = new GregorianCalendar(now.get(Calendar.YEAR) + 1, 0, 5);
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (cal1.before(now) && cal2.after(now))
		{
			if(!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
				return false;

			return true;
		} 
		else if (qs != null)
		{
			// Set as expired
			QuestEngine.getInstance().deleteQuest(player, questId);
			PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId));
			player.getController().updateNearbyQuests();
		}
		return false;
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799769).addOnQuestStart(questId);
		for(int npc: npcs)
			qe.setNpcQuestData(npc).addOnTalkEvent(questId);
	}

}
