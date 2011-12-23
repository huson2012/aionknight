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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;

public class _2947FollowingThrough extends QuestHandler
{
	private final static int	questId	= 2947;
	private final static int[]	npc_ids = {204053, 204301, 204089, 700368, 700369, 700268};
	private final static int[]	mob_ids = {212396, 212611, 212408, 213583, 213584};

	public _2947FollowingThrough()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		if(!super.defaultQuestOnDialogInitStart(env))
			return false;
		
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204053:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10010:
							return defaultCloseDialog(env, 0, 1);
						case 10011:
							return defaultCloseDialog(env, 0, 4);
						case 10012:
							return defaultCloseDialog(env, 0, 9);
						case 34:
							if(var == 1 || var == 4 || var == 9)
							{
								qs.setQuestVarById(0, 0);
								updateQuestStatus(env);
								return sendQuestDialog(env, 2375);
							}
					}
				}	break;
				case 204301:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 2)
								return sendQuestDialog(env, 3398);
							else if(var == 7)
								return sendQuestDialog(env, 3739);
							else
								return defaultQuestItemCheck(env, 9, 0, true, 7, 4080);
						case 10001:
							return defaultCloseDialog(env, 1, 2);
						case 1009:
							if(var == 2)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
							if(var == 7)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 6);
							}
					}
				}
					break;
				case 204089:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 1693);
							else if(var == 5)
								return sendQuestDialog(env, 2034);
						case 10002:
							if(defaultCloseDialog(env, 4, 5))
							{
								TeleportService.teleportTo(player, 320090000, 1, 276, 293, 163, (byte)90, 500);
								return true;
							}
							else
								return false;
						case 10003:
							if(var == 5)
							{
								qs.setQuestVarById(0, 7);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
								return true;
							}
							else
								return false;
					}
				}
					break;
				case 700368:
				case 700369:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false));
					break;
				case 700268:
					if(env.getDialogId() == -1)
						return (defaultQuestUseNpc(env, 9, 10, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true));
					break;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(var == 2)
				return defaultQuestRewardDialog(env, 204301, 3739);
			else if(var == 7)
				return defaultQuestRewardDialog(env, 204301, 3739, 1);
			else if(var == 9)
				return defaultQuestRewardDialog(env, 204301, 3739, 2);
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		if(defaultQuestOnKillEvent(env, 212396, 0, 3, 1) || defaultQuestOnKillEvent(env, 212611, 0, 3, 2)
			|| defaultQuestOnKillEvent(env, 212408, 0, 3, 3) || defaultQuestOnKillEvent(env, 212409, 0, 3, 3)
			|| defaultQuestOnKillEvent(env, 213583, 0, 10, 4) || defaultQuestOnKillEvent(env, 213584, 0, 10, 4))
			return true;
		else
			return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}

	@Override
	public void QuestUseNpcInsideFunction(QuestCookie env)
	{
		Player player = env.getPlayer();
		if(env.getTargetId() == 700368)
			TeleportService.teleportTo(player, 320090000, 1, 276, 293, 163, (byte)90, 500);
		else if(env.getTargetId() == 700369)
			TeleportService.teleportTo(player, 120010000, 1, 982, 1556, 210, (byte)90, 500);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc_id: npc_ids)
		qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
		for(int mob_id: mob_ids)
		qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
	}
}