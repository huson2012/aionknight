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

package quest.heiron;

import gameserver.controllers.PortalController;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;

public class _1063BrigadierIndratu extends QuestHandler
{
	private final static int	questId	= 1063;
	private final static int[]	npc_ids	= { 204500, 700271, 700361, 203700 };
	private int instanceId = 0;
	
	public _1063BrigadierIndratu()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(214159).addOnKillEvent(questId);// Brigadier indratu	
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		int[] quests = {1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1062};
		return defaultQuestOnLvlUpEvent(env, quests, true);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203700)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		
		if(targetId == 204500)
		{
			switch(env.getDialogId())
			{
				case 26:
					if(var == 0)
						return sendQuestDialog(env, 1011);
					if(var == 2)
						return sendQuestDialog(env, 1693);
					break;
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					break;
				case 10255:
					if(var == 2)
					{
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
					break;
			}
		}
		else if(targetId == 700271)
		{
			if (env.getDialogId() == -1 && qs.getQuestVarById(0) == 1)
			{
				if (player.getPlayerGroup() == null)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
					return false;
				}
				
				WorldMapTemplate world = DataManager.WORLD_MAPS_DATA.getTemplate(310090000);
				int mapname = DataManager.WORLD_MAPS_DATA.getTemplate(310090000).getMapNameId();
				if (!InstanceService.canEnterInstance(player, world.getInstanceMapId(), 0))
				{
					int timeinMinutes = InstanceService.getTimeInfo(player).get(world.getInstanceMapId())/60;
					if (timeinMinutes >= 60 )
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR(mapname, timeinMinutes/60));
					else	
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN(mapname, timeinMinutes));
					
					return false;
				}

				WorldMapInstance newInstance = InstanceService.getRegisteredInstance(310090000, player.getPlayerGroup().getGroupId());
				if (newInstance == null)
				{
					newInstance = InstanceService.getNextAvailableInstance(310090000);
					InstanceService.registerGroupWithInstance(newInstance, player.getPlayerGroup());
				}
				instanceId = newInstance.getInstanceId();
				
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
				
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run()
					{
						if(qs.getQuestVarById(0) == 1)
						{
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
							TeleportService.teleportTo(player, 310090000, instanceId, 561.36f, 333.81f, 1015.818f, 36);
							PortalController.setInstanceCooldown(player, 310090000, instanceId);
						}
					}
				}, 3000);
			}
			return false;
		}
		else if(targetId == 700361)
		{
			if (env.getDialogId() == -1 && qs.getQuestVarById(0) == 2)
			{
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
				
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
						TeleportService.teleportTo(player, 210040000, 1875.37f, 2584.94f, 140.73f, 34);
					}
				}, 3000);
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVarById(0) != 1)
			return false;

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
			
		if(targetId != 214159)
			return false;

		qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
		updateQuestStatus(env);
		
		return true;
	}
	
	@Override
	public boolean onActionItemEvent(QuestCookie env)
	{
		int target = env.getTargetId();
		return target == 700271 || target == 700361;
	}
}