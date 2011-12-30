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

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

public class _2290GrokensEscape extends QuestHandler
{

	private final static int	questId	= 2290;
	
	private boolean executorReturn = true;

	public _2290GrokensEscape()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203608).addOnQuestStart(questId);
		qe.setNpcQuestData(203608).addOnTalkEvent(questId);
		qe.setNpcQuestData(700178).addOnTalkEvent(questId);
		qe.setNpcQuestData(203607).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203608)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if (env.getDialogId() == 1002)
				{
					if (QuestService.startQuest(env, QuestStatus.START))
					{
						Npc npc = (Npc)env.getVisibleObject();
						npc.getMoveController().setNewDirection(1219.15f, 1212f, 247.37f);
						npc.getMoveController().schedule();
						return sendQuestDialog(env, 1003);
					}
				}
				else
					return defaultQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700178:
				{
					if (qs.getQuestVarById(0) == 0 && env.getDialogId() == -1)
					{
						executorReturn = true;
						
						player.getKnownList().doOnAllNpcs(new Executor<Npc>(){
							@Override
							public boolean run(Npc obj)
							{
								if (obj.getNpcId() != 203608)
									return true;
								if (MathUtil.getDistance(player.getX(), player.getY(), player.getZ(),obj.getX(), obj.getY(), obj.getZ()) > 4)
								{
									executorReturn = false;
									return false;
								}
								obj.getController().onDie(null);
								obj.getController().onDespawn(false);
								return true;
							}
						}, true);
						
						if (!executorReturn)
						{
							return false;
						}
						
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
							1));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
							targetObjectId), true);
						ThreadPoolManager.getInstance().schedule(new Runnable(){
							@Override
							public void run()
							{
								if(!player.isTargeting(targetObjectId))
									return;
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
									targetObjectId, 3000, 0));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
									targetObjectId), true);
								qs.setQuestVarById(0, 3);
								updateQuestStatus(env);
							}
						}, 3000);
					}
				}
				break;
				case 203607:
				{
					if (qs.getQuestVarById(0) == 3)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 1693);
						else if(env.getDialogId() == 1009)
							{
								player.getInventory().removeFromBagByItemId(182203208, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return defaultQuestEndDialog(env);
							}
							else
								return defaultQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203607)
				return defaultQuestEndDialog(env);
		}
		return false;
	}
}