/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package quest.verteron;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;

import java.util.Collections;

public class _1162_AltenosWeddingRing extends QuestHandler
{
	private final static int	questId	= 1162;

	public _1162_AltenosWeddingRing()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(203095).addOnQuestStart(questId);
		qe.setNpcQuestData(203095).addOnTalkEvent(questId);
		qe.setNpcQuestData(203093).addOnTalkEvent(questId);
		qe.setNpcQuestData(700005).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203095)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else
					return defaultQuestStartDialog(env);
			}
		}

		if(qs == null)
			return false;

		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700005:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(player.getInventory().getItemCountByItemId(182200563) == 0)
							{
								if(!ItemService.addItems(player, Collections
									.singletonList(new QuestItems(182200563, 1))))
								{
									return true;
								}
							}

							final int targetObjectId = env.getVisibleObject().getObjectId();
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
								targetObjectId, 3000, 1));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2,
								0, targetObjectId), true);
							ThreadPoolManager.getInstance().schedule(new Runnable(){
								@Override
								public void run()
								{
									if(!player.isTargeting(targetObjectId))
										return;

									PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
										targetObjectId, 3000, 0));
									PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player,
										EmotionType.START_LOOT, 0, targetObjectId), true);
									QuestState qs = player.getQuestStateList().getQuestState(questId);
									qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
									updateQuestStatus(env);
									PacketSendUtility.broadcastPacket(player.getTarget(), new SM_EMOTION(
										(Creature) player.getTarget(), EmotionType.DIE, 128, 0));
								}
							}, 3000);
							return true;
						}
					}
				}
				case 203093:
				case 203095:
				{
					if(qs.getQuestVarById(0) == 1)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						player.getInventory().removeFromBagByItemId(182200563, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(),
							10));
						return true;
					}
				}
					return false;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203095)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else
					return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}