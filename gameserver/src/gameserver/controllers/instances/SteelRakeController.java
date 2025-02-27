/*
/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.controllers.instances;

import gameserver.ai.events.Event;
import gameserver.controllers.NpcController;
import gameserver.controllers.PortalController;
import gameserver.controllers.attack.AttackStatus;
import gameserver.dataholders.DataManager;
import gameserver.model.EmotionType;
import gameserver.model.TaskId;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.group.PlayerGroup;
import gameserver.model.templates.NpcTemplate;
import gameserver.model.templates.WorldMapTemplate;
import gameserver.model.templates.portal.ExitPoint;
import gameserver.model.templates.portal.PortalTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.TeleportService;
import gameserver.skill.SkillEngine;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.exceptionhandlers.exception_enums;
import gameserver.world.WorldMapInstance;

public class SteelRakeController extends NpcController
{
	private VisibleObject target = null;
	
	@Override
	public void onRespawn()
	{
		getOwner().unsetState(CreatureState.DEAD);
		getOwner().getAggroList().clear();
		
		cancelTask(TaskId.DECAY);
		Npc owner = getOwner();

		// Set state from npc templates
		if(owner.getObjectTemplate().getState() != 0)
			owner.setState(owner.getObjectTemplate().getState());
		else
			owner.setState(CreatureState.NPC_IDLE);

		switch (owner.getNpcId())
		{
			// List of npc ids
			case exception_enums.NPC_INSTANCE_SR_SPAWN_T1:
			case exception_enums.NPC_INSTANCE_SR_SPAWN_T2:
			case exception_enums.NPC_INSTANCE_SR_SPAWN_T3:
			case exception_enums.NPC_INSTANCE_SR_SPAWN_T4:
			{
				owner.getLifeStats().setCurrentHpPercent(10);
				
			}
			break;
			default:
				owner.getLifeStats().setCurrentHpPercent(100);
		}
		
		owner.getAi().handleEvent(Event.RESPAWNED);

		if(owner.getSpawn().getNpcFlyState() != 0)
		{
			owner.setState(CreatureState.FLYING);
		}
		
	
	}
	
	@Override
	public void onDialogRequest(final Player player)
	{
		getOwner().getAi().handleEvent(Event.TALK);
		
		// Steel rake teleporters
		NpcTemplate npctemplate = DataManager.NPC_DATA.getNpcTemplate(getOwner().getNpcId());
		if (npctemplate.getTitleId() == 370118)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
			return;
		}
		
		switch (getOwner().getNpcId())
		{
			// Central Deck Mobile Cannon
			case exception_enums.NPC_INSTANCE_SR_DECK:
			{
				if (player.getInventory().getItemCountByItemId(185000052) < 1) // Largimark's Flint
					return;
				final int defaultUseTime = 3000;
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), 
					getOwner().getObjectId(), defaultUseTime, 1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), 
							getOwner().getObjectId(), defaultUseTime, 0));
						
						player.getInventory().removeFromBagByItemId(185000052, 1);
						
						// Sets target for cannon						
						player.getKnownList().doOnAllNpcs(new Executor<Npc>(){
							@Override
							public boolean run(Npc object)
							{
								if (object.getNpcId() != exception_enums.NPC_INSTANCE_SR_SPAWN_T2)
									return true;
								
								if (Math.abs(object.getX()-526) < 1 && Math.abs(object.getY()-508) < 1)
								{
										target = (VisibleObject)object;
										return false;
								}
								return true;
							}
						}, true);
						
						SkillEngine.getInstance().getSkill(getOwner(), 18572, 1, getOwner()).useSkill();
						
						// Kills mobs
						player.getKnownList().doOnAllNpcs(new Executor<Npc>(){
							@Override
							public boolean run (Npc object)
							{
								if (MathUtil.isIn3dRange(object, target , 10))
									object.getController().onAttack(object, object.getLifeStats().getMaxHp() + 1, AttackStatus.NORMALHIT, true);
								return true;
							}
						}, true);
					}
				}, defaultUseTime);
				break;
			}				
			// Suspicious Cannon
			case exception_enums.NPC_INSTANCE_SR_CANNON:
				PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 73001, 0));
				break;
		}
		
	}
	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId)
	{
		Npc npc = getOwner();
		int targetObjectId = npc.getObjectId();
		
		// Steel rake teleporter
		if (dialogId == 1012 && (npc.getNpcId() == exception_enums.NPC_INSTANCE_SR_TP_ELYOS || npc.getNpcId() == exception_enums.NPC_INSTANCE_SR_TP_ASMO))
		{
			int completedquestid = 0;
			switch (player.getCommonData().getRace())
			{
				case ASMODIANS:
					completedquestid = 4200;
					break;
				case ELYOS:
					completedquestid = 3200;
					break; 
			}
			QuestState qstel = player.getQuestStateList().getQuestState(completedquestid);
			if (qstel == null || qstel.getStatus() != QuestStatus.COMPLETE)
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1097));
				return;
			}
			if (player.getPlayerGroup() == null)
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1182));
				return;
			}
			
			// Check instance cool time
			// TODO: Show proper name for instance
			WorldMapTemplate world = DataManager.WORLD_MAPS_DATA.getTemplate(300100000);
			if (!InstanceService.canEnterInstance(player, world.getInstanceMapId(), 0))
			{
				int timeinMinutes = InstanceService.getTimeInfo(player).get(world.getInstanceMapId())/60;
				if (timeinMinutes >= 60 )
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_HOUR(401255, timeinMinutes/60));
				else	
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_ENTER_INSTANCE_COOL_TIME_MIN(401255, timeinMinutes));
				
				return;
			}
			
			PlayerGroup group = player.getPlayerGroup();
			WorldMapInstance instance = InstanceService.getRegisteredInstance(300100000, group.getGroupId());
			if (instance == null)
			{
				instance = InstanceService.getNextAvailableInstance(300100000);
				InstanceService.registerGroupWithInstance(instance, group);
			}
			
			PortalTemplate portalTemplate = DataManager.PORTAL_DATA.getPortalTemplate(getOwner().getNpcId());
			ExitPoint exit = null;
			for (ExitPoint point : portalTemplate.getExitPoint())
			{
				if (point.getRace() == null || point.getRace().equals(group.getGroupLeader().getCommonData().getRace()))
					exit = point;
			}
			TeleportService.teleportTo(player, exit.getMapId(), instance.getInstanceId(),
				exit.getX(),exit.getY(),exit.getZ(), 0);
			PortalController.setInstanceCooldown(player, 300100000, instance.getInstanceId());
        }
	}	
}