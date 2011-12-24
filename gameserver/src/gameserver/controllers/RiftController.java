/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package gameserver.controllers;

import gameserver.configs.main.CustomConfig;
import gameserver.model.ChatType;
import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_RIFT_ANNOUNCE;
import gameserver.network.aion.serverpackets.SM_RIFT_STATUS;
import gameserver.services.RespawnService;
import gameserver.services.TeleportService;
import gameserver.spawn.RiftSpawnManager;
import gameserver.spawn.RiftSpawnManager.RiftEnum;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapInstance;
import gameserver.world.WorldType;

public class RiftController extends NpcController
{
	private boolean isMaster = false;
	private SpawnTemplate slaveSpawnTemplate;
	private SpawnTemplate masterSpawnTemplate;
	private Npc slave;
	private Integer maxEntries;
	private Integer maxLevel;
	private int usedEntries;
	private boolean isAccepting;
	private RiftEnum riftTemplate;

	/**
	 * Used to create master rifts or slave rifts (slave == null)
	 * 
	 * @param slaveSpawnTemplate
	 */
	public RiftController(Npc slave, SpawnTemplate spawnTemplate, RiftSpawnManager.RiftEnum riftTemplate)
	{
		this.riftTemplate = riftTemplate;
		if(slave != null)//master rift should be created
		{
			this.slave = slave;
			this.slaveSpawnTemplate = slave.getSpawn();
			this.masterSpawnTemplate = spawnTemplate;
			this.maxEntries = riftTemplate.getEntries();
			this.maxLevel = riftTemplate.getMaxLevel();
			isMaster = true;
			isAccepting = true;
		}
	}

	@Override
	public void onDialogRequest(Player player)
	{
		if(CustomConfig.RIFT_RACE)
		{
			Race race = player.getCommonData().getRace();
			WorldType world = player.getWorldType();
			if(race == Race.ASMODIANS && world == WorldType.ELYSEA || race == Race.ELYOS && world == WorldType.ASMODAE)
			{
				PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Rifts have been disabled for opposing races.", ChatType.ANNOUNCEMENTS));
				return;
			}
		}

		if(!isMaster && !isAccepting)
			return;

		RequestResponseHandler responseHandler = new RequestResponseHandler(getOwner()){
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				if(!isAccepting)
					return;

				int worldId = slaveSpawnTemplate.getWorldId();
				float x = slaveSpawnTemplate.getX();
				float y = slaveSpawnTemplate.getY();
				float z = slaveSpawnTemplate.getZ();

				TeleportService.teleportTo(responder, worldId, x, y, z, 0);
				usedEntries++;

				if(usedEntries >= maxEntries)
				{
					isAccepting = false;

					RespawnService.scheduleDecayTask(getOwner());
					RespawnService.scheduleDecayTask(slave);
				}
				PacketSendUtility.broadcastPacket(getOwner(), new SM_RIFT_STATUS(getOwner().getObjectId(), usedEntries, maxEntries, maxLevel));
				sendUpdate();

			}

			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				//do nothing
			}
		};
		boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_USE_RIFT, responseHandler);
		if(requested)
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_USE_RIFT, 0));
	}

	@Override
	public void see(VisibleObject object)
	{
		if(!isMaster)
			return;

		if(object instanceof Player)
		{
			PacketSendUtility.sendPacket((Player) object, new SM_RIFT_STATUS(getOwner().getObjectId(), usedEntries, maxEntries, maxLevel));
		}
	}

	/**
	 * @param activePlayer
	 */
	public void sendMessage(Player activePlayer)
	{
		if((isMaster) && (getOwner().isSpawned()))
		{
			PacketSendUtility.sendPacket(activePlayer, new SM_RIFT_ANNOUNCE(33, getOwner().getObjectId().intValue(), riftTemplate, masterSpawnTemplate, RiftSpawnManager.getRemaningTime()));
			PacketSendUtility.sendPacket(activePlayer, new SM_RIFT_ANNOUNCE(13, getOwner().getObjectId().intValue(), usedEntries, RiftSpawnManager.getRemaningTime()));
		}
	}

	public void sendAnnounce()
	{
		if((isMaster) && (getOwner().isSpawned()))
		{
			WorldMapInstance worldInstance = getOwner().getPosition().getMapRegion().getParent();
			worldInstance.doOnAllPlayers(new Executor<Player>(){
				@Override
				public boolean run(Player player)
				{
					if(player.isSpawned())
					{
						PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(9, 0, RiftSpawnManager.getRiftsSize(), player.getCommonData().getRace()));
						RiftController.this.sendMessage(player);
						PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(9, 1, 1, player.getCommonData().getRace()));
					}
					return true;
				}
			});
		}
	}

	public void sendUpdate()
	{
		WorldMapInstance worldInstance = getOwner().getPosition().getMapRegion().getParent();
		worldInstance.doOnAllPlayers(new Executor<Player>(){
			public boolean run(Player player)
			{
				if(player.isSpawned())
					PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(13, getOwner().getObjectId(), usedEntries, RiftSpawnManager.getRemaningTime()));
				return true;
			}
		});
	}
}
