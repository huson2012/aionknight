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

package quest.ishalgen;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

/**
 * Исправления в коде квеста - KnightMrak. Оригинальный код - Mr. Poke
 */
public class _2007WheresRaeThisTime extends QuestHandler 
{
	private final static int questId = 2007;
	public _2007WheresRaeThisTime() 
	{
		super(questId);
	}

	@Override
	public void register() 
	{
		int[] talkNpcs = {203516, 203519, 203539, 203552, 203554, 700081, 700082, 700083};
		qe.addQuestLvlUp(questId);
		
		for (int id : talkNpcs)
		qe.setNpcQuestData(id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env) 
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs.getStatus() == QuestStatus.START) 
		{
			switch (targetId) 
			{
				case 203516:
				switch (env.getDialogId()) 
				{
				case 26:
					if (var == 0)
				return sendQuestDialog(env, 1011);
				case 10000:
				if (var == 0) 
				{
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
					break;
					case 203519:
					switch (env.getDialogId()) 
				{
					case 26:
						if (var == 1)
					return sendQuestDialog(env, 1352);
					case 10001:
					if (var == 1) 
				{
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
				break;
				case 203539:
				switch (env.getDialogId()) 
			{
				case 26:
					if (var == 2)
				return sendQuestDialog(env, 1693);
				case 1694:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 55));
				break;
				case 10002:
					if (var == 2) 
				{
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
				break;
				case 203552:
				switch (env.getDialogId()) 
			{
				case 26:
					if (var == 3)
				return sendQuestDialog(env, 2034);
				case 10003:
				if (var == 3) 
				{
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
				break;
				case 203554:
				switch (env.getDialogId()) 
			{
				case 26:				
					if (var == 4)
						return sendQuestDialog(env, 2375);
					else if (var == 8)
						return sendQuestDialog(env, 2716);
				case 10004:
				
				if (var == 4) 
				{
					qs.setQuestVar(5);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
				break;
				case 10005:
					if (var == 8) 
				{
					qs.setQuestVar(9);
					updateQuestStatus(env);
					qs.setQuestVar(8);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
				break;
				case 700081:
					if (var == 5) 
				{
					destroy(6, env);
					return false;
				}
				break;
				case 700082:
					if (var == 6) 
				{
					destroy(7, env);
					return false;
				}
				break;
				case 700083:
					if (var == 7) 
				{
					destroy(-1, env);
					return false;
				}
				break;
			}
				} 
					else if (qs.getStatus() == QuestStatus.REWARD) 
				{
					if (targetId == 203516) 
				{
					if (env.getDialogId() == -1) 
					{
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 58));
						return sendQuestDialog(env, 3057);
					} 
						else
					return defaultQuestEndDialog(env);
				}
					}
					return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env) 
	{
		int[] quests = {2001, 2002, 2003, 2004, 2005, 2006};
		return defaultQuestOnLvlUpEvent(env, quests);
		}

	private void destroy(final int var, final QuestCookie env) 
	{
		final int targetObjectId = env.getVisibleObject().getObjectId();
		final Player player = env.getPlayer();
		
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() 
		{

		@Override	
		public void run() 
		{
			if (player.getTarget().getObjectId() != targetObjectId)
			return;

			PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
			PacketSendUtility.broadcastPacket(player.getTarget(), new SM_EMOTION((Creature) player.getTarget(), EmotionType.EMOTE, 128, 0));
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			
			switch (var) 
			{
				case 6:
				case 7:
					qs.setQuestVar(var);
				break;
				case -1:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 56));
					qs.setQuestVar(8);
				break;
			}
				updateQuestStatus(env);
			}
		}, 3000);
	}
} 